package com.okm1208.vacation.manager.controller;

import com.okm1208.vacation.common.entity.VacationInfo;
import com.okm1208.vacation.common.exception.BadRequestException;
import com.okm1208.vacation.common.exception.DataNotFoundException;
import com.okm1208.vacation.common.model.CommonResponse;
import com.okm1208.vacation.common.msg.ErrorMessageProperties;
import com.okm1208.vacation.common.utils.AuthUtils;
import com.okm1208.vacation.manager.model.*;
import com.okm1208.vacation.manager.repository.VacationInfoRepository;
import com.okm1208.vacation.manager.service.VacationRegisterManager;
import com.okm1208.vacation.manager.service.VacationRegisterManagerFactory;
import com.okm1208.vacation.manager.service.impl.VacationCancelManager;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.stream.Collectors;

/**
 * @author Nick ( okm1208@gmail.com )
 * @created 2021-05-15
 */
@Slf4j
@RestController
@RequestMapping("/vacations")
@Api(tags = "휴가 등록 APIs")
public class VacationController {

    @Autowired
    VacationInfoRepository vacationInfoRepository;

    @Autowired
    VacationCancelManager vacationCancelManager;

    @ApiOperation(value = "조회 API")
    @GetMapping
    public CommonResponse<VacationSearchResponseVo> search(){

        VacationInfo vacationInfo =
                vacationInfoRepository.findOneByAccountId(AuthUtils.getAccountIdFromSecurityContext());

        if(vacationInfo == null ){
            throw DataNotFoundException.of(ErrorMessageProperties.EMPTY_DATA);
        }

        VacationSearchResponseVo responseVo = new VacationSearchResponseVo();
        responseVo.setOccursDays(vacationInfo.getOccursDays());
        responseVo.setRemainingDays(vacationInfo.getRemainingDays());
        responseVo.setUseDays(vacationInfo.getUseDays());
        responseVo.setHistory(
                vacationInfo.getVacationHistoryList().stream().map(
                        v-> VacationHistoryVo.builder()
                                    .no(v.getHistoryNo())
                                    .regDt(v.getRegDt())
                                    .comment(v.getComment())
                                    .vacationType(v.getVacationType().getDesc())
                                    .build()
                        ).collect(Collectors.toList())
        );
        return CommonResponse.success(responseVo);
    }
    @ApiOperation(value = "등록 API")
    @PostMapping
    public CommonResponse<VacationRegisterResponseVo> register(@RequestBody @Valid VacationRegisterRequestVo request){
        if( !request.isValidRequest() ){
            throw BadRequestException.of(ErrorMessageProperties.REGISTER_ERROR_04);
        }
        VacationRegisterDto vacationRegisterDto = VacationRegisterDto.builder()
                .accountId(AuthUtils.getAccountIdFromSecurityContext())
                .comment(request.getComment())
                .startDt(request.getStartDt())
                .endDt(request.getEndDt())
                .vacationType(request.getVacationType())
                .build();

        VacationRegisterManager vacationRegisterManager =
                VacationRegisterManagerFactory.getVacationManager(request.getVacationType());

        vacationRegisterManager.register(vacationRegisterDto);

        VacationInfo findVacationInfo =
                vacationInfoRepository.findOneByAccountId(vacationRegisterDto.getAccountId());

        return CommonResponse.success(VacationRegisterResponseVo
                .builder()
                .occursDays(findVacationInfo.getOccursDays())
                .remainingDays(findVacationInfo.getRemainingDays())
                .useDays(findVacationInfo.getUseDays())
                .build());
    }

    @ApiOperation(value = "취소 API")
    @DeleteMapping(value = "/{no}")
    public CommonResponse cancel(@PathVariable("no") Long no){
        vacationCancelManager.cancel(AuthUtils.getAccountIdFromSecurityContext(), no, LocalDate.now());

        return CommonResponse.success();
    }
}
