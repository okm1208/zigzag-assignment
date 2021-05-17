package com.okm1208.vacation.manager.controller;

import com.okm1208.vacation.account.repository.AccountRepository;
import com.okm1208.vacation.common.entity.Account;
import com.okm1208.vacation.common.entity.VacationHistory;
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
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
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
    AccountRepository accountRepository;

    @Autowired
    VacationInfoRepository vacationInfoRepository;

    @Autowired
    VacationCancelManager vacationCancelManager;

    @ApiOperation(value = "전체 조회 API" , notes = "휴가 정보 및 상세 내역")
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

    @ApiOperation(value = "상세 보기 API" , notes = "등록된 휴가 정보 상세 정보")
    @GetMapping(value = "/{no}")
    public CommonResponse<VacationHistoryVo> view(@PathVariable("no") Long no){
        Account account = accountRepository.findByAccountId(AuthUtils.getAccountIdFromSecurityContext());

        VacationHistory viewHistory =
                account.getVacationInfo()
                        .getVacationHistoryList().stream()
                        .filter(v->v.getHistoryNo().equals(no))
                        .findFirst()
                        .orElse(null );
        if(viewHistory == null){
            throw DataNotFoundException.of(ErrorMessageProperties.EMPTY_DATA);
        }

        return CommonResponse.success(VacationHistoryVo
                .builder()
                .comment(viewHistory.getComment())
                .regDt(viewHistory.getRegDt())
                .no(viewHistory.getHistoryNo())
                .vacationType(viewHistory.getVacationType().getDesc())
                .build());
    }
//    ANNUAL_LEAVE("AL","연차" ,BigDecimal.valueOf(1.0) ),
//    HALF_DAY_LEAVE("HDL","반차",BigDecimal.valueOf(0.5) ),
//    HALF_AND_HALF_LEAVE("HAHL","반반차",BigDecimal.valueOf(0.25)
    @ApiOperation(value = "등록 API", notes = "연차, 반차, 반반차 등록 \n\n vacationType 값 \n'AL' : '연차'\n'HDL' : '반차'\n'HAHL' : '반반차'" +
            "\n\n startDt, endDt 값 : 'yyyy-MM-dd'")

    @PostMapping
    public CommonResponse<VacationRegisterResponseVo> register(@RequestBody @Valid VacationRegisterRequestVo registerRequest){
        if( !registerRequest.isValidRequest() ){
            throw BadRequestException.of(ErrorMessageProperties.REGISTER_ERROR_04);
        }
        VacationRegisterDto vacationRegisterDto = VacationRegisterDto.builder()
                .accountId(AuthUtils.getAccountIdFromSecurityContext())
                .comment(registerRequest.getComment())
                .startDt(registerRequest.getStartDt())
                .endDt(registerRequest.getEndDt())
                .vacationType(registerRequest.getVacationType())
                .build();

        VacationRegisterManager vacationRegisterManager =
                VacationRegisterManagerFactory.getVacationManager(registerRequest.getVacationType());

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

    @ApiOperation(value = "취소 API" , notes = "등록된 휴가 취소")
    @DeleteMapping(value = "/{no}")
    public CommonResponse cancel(@PathVariable("no") Long no){
        vacationCancelManager.cancel(AuthUtils.getAccountIdFromSecurityContext(), no, LocalDate.now());

        return CommonResponse.success();
    }
}
