package com.okm1208.vacation.register.controller;

import com.okm1208.vacation.common.exception.BadRequestException;
import com.okm1208.vacation.register.model.VacationRegisterRequestVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @author Nick ( okm1208@gmail.com )
 * @created 2021-05-15
 */
@Slf4j
@RestController
@RequestMapping("/vacation")
@Api(tags = "휴가 등록 APIs")
public class VacationController {

    @ApiOperation(value = "등록 API")
    @PostMapping(value="/register")
    public void register(){

    }

    @ApiOperation(value = "취소 API")
    @DeleteMapping(value = "/cancel")
    public void cancel(){

    }
}
