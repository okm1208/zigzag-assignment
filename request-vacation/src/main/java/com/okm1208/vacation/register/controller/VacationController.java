package com.okm1208.vacation.register.controller;

import com.okm1208.vacation.account.repository.AccountRepository;
import com.okm1208.vacation.common.entity.VacationInfo;
import com.okm1208.vacation.common.exception.BadRequestException;
import com.okm1208.vacation.common.model.CommonResponse;
import com.okm1208.vacation.common.utils.AuthUtils;
import com.okm1208.vacation.register.model.VacationRegisterDto;
import com.okm1208.vacation.register.model.VacationRegisterRequestVo;
import com.okm1208.vacation.register.model.VacationRegisterResponseVo;
import com.okm1208.vacation.register.repository.VacationInfoRepository;
import com.okm1208.vacation.register.service.VacationManager;
import com.okm1208.vacation.register.service.VacationManagerFactory;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author Nick ( okm1208@gmail.com )
 * @created 2021-05-15
 */
@Slf4j
@RestController
@RequestMapping("/vacation")
@Api(tags = "휴가 등록 APIs")
public class VacationController {

    @Autowired
    VacationInfoRepository vacationInfoRepository;

    @ApiOperation(value = "등록 API")
    @PostMapping
    public CommonResponse<VacationRegisterResponseVo> register(@RequestBody VacationRegisterRequestVo request){
        if( !request.isValidRequest() ){
            throw new BadRequestException();
        }
        VacationRegisterDto vacationRegisterDto = VacationRegisterDto.builder()
                .accountId(AuthUtils.getAccountIdFromSecurityContext())
                .comment(request.getComment())
                .startDt(request.getStartDt())
                .endDt(request.getEndDt())
                .vacationType(request.getVacationType())
                .build();

        VacationManager vacationManager =
                VacationManagerFactory.getVacationManager(request.getVacationType());

        vacationManager.register(vacationRegisterDto);

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
    @DeleteMapping(value = "/cancel")
    public void cancel(){

    }
}
