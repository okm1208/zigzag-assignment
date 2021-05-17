package com.okm1208.vacation.common.exception.handler;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.okm1208.vacation.common.exception.CustomBusinessException;
import com.okm1208.vacation.common.model.DefaultErrorResponse;
import com.okm1208.vacation.common.model.ErrorResponse;
import com.okm1208.vacation.common.model.ErrorResponseEntity;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.TypeMismatchException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;
import java.util.Map;

/**
 * @author Nick ( okm1208@gmail.com )
 * @created 2021-05-15
 */

@Controller
@Slf4j
public class CustomErrorController implements ErrorController {
    @Autowired
    private ErrorAttributes errorAttributes;

    @Override
    public String getErrorPath() {
        return "/error";
    }

    @RequestMapping("/error")
    @ResponseBody
    public ResponseEntity<ErrorResponseEntity> handleError(HttpServletRequest request) {

        ServletWebRequest servletWebRequest = new ServletWebRequest(request);
        Throwable error = errorAttributes.getError(servletWebRequest);

        Map<String, Object> errorResult = errorAttributes.getErrorAttributes(servletWebRequest, ErrorAttributeOptions.of(ErrorAttributeOptions.Include.MESSAGE));

        if(error instanceof CustomBusinessException){
            CustomBusinessException businessException = (CustomBusinessException) error;
            ErrorResponse errorResponse = businessException.getErrorResponse();

            return this.handleException(errorResponse , businessException.getMessage());
        }else{

            HttpStatus errorHttpStatus = null;
            Integer errorStatus = errorResult != null ? (Integer)errorResult.get("status") : null;
            if(errorStatus != null){
                errorHttpStatus =  HttpStatus.valueOf(errorStatus);
            }
            ErrorResponse errorResponse = findCommonErrorCodeByDefaultException(error , errorHttpStatus);
            return this.handleException(errorResponse , errorResponse.getErrorMessage() );
        }
    }


    protected ErrorResponse findCommonErrorCodeByDefaultException(Throwable error , HttpStatus errorHttpStatus ){

        if(error instanceof MethodArgumentNotValidException
                || error instanceof MissingServletRequestParameterException
                || error instanceof MissingServletRequestPartException
                || error instanceof TypeMismatchException
                || error instanceof HttpMessageNotReadableException
                || error instanceof ConstraintViolationException
                || error instanceof ServletRequestBindingException
                || error instanceof BindException
                || error instanceof MethodArgumentNotValidException
                || error instanceof InvalidFormatException
                || errorHttpStatus == HttpStatus.BAD_REQUEST) {

            return DefaultErrorResponse.BAD_REQUEST;
        }
        else if(error instanceof NoHandlerFoundException
                || errorHttpStatus == HttpStatus.NOT_FOUND ){
            return DefaultErrorResponse.NOT_FOUND;
        }
        else if(error instanceof HttpRequestMethodNotSupportedException
                || errorHttpStatus == HttpStatus.METHOD_NOT_ALLOWED ){
            return DefaultErrorResponse.METHOD_NOT_ALLOWED;
        }
        else if(error instanceof HttpMediaTypeNotAcceptableException){
            return DefaultErrorResponse.NOT_ACCEPTABLE;
        }
        else if(error instanceof HttpMediaTypeNotSupportedException){
            return  DefaultErrorResponse.UNSUPPORTED_MEDIA_TYPE;
        }
        else if(error instanceof AccessDeniedException
                || errorHttpStatus == HttpStatus.FORBIDDEN){
            return DefaultErrorResponse.FORBIDDEN;
        }
        else if(errorHttpStatus == HttpStatus.UNAUTHORIZED){
            return DefaultErrorResponse.UNAUTHORIZED;
        }
        else {
            return DefaultErrorResponse.INTERNAL_SERVER_ERROR;
        }
    }

    protected ResponseEntity<ErrorResponseEntity> handleException(ErrorResponse errorResponse , String errorMessage) {
        ErrorResponseEntity errorResponseEntity = new ErrorResponseEntity();
        errorResponseEntity.setType(errorResponse.getErrorType());
        errorResponseEntity.setMessage((!StringUtils.isEmpty(errorMessage)) ? errorMessage : errorResponse.getErrorMessage());
        return new ResponseEntity(errorResponseEntity, errorResponse.getStatus());
    }

}
