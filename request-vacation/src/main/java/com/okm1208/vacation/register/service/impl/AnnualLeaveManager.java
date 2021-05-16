package com.okm1208.vacation.register.service.impl;

import com.okm1208.vacation.account.repository.AccountRepository;
import com.okm1208.vacation.common.entity.VacationHistory;
import com.okm1208.vacation.common.entity.VacationInfo;
import com.okm1208.vacation.common.enums.VacationType;
import com.okm1208.vacation.common.exception.BadRequestException;
import com.okm1208.vacation.common.utils.DateRange;
import com.okm1208.vacation.common.utils.HolidayChecker;
import com.okm1208.vacation.register.model.ApplyRegisterDto;
import com.okm1208.vacation.register.model.VacationRegisterDto;
import com.okm1208.vacation.register.service.VacationManager;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static com.okm1208.vacation.common.msg.ErrorMessageProperties.*;

/**
 * @author Nick ( okm1208@gmail.com )
 * @created 2021-05-15
 */

public class AnnualLeaveManager extends VacationManager {

    public AnnualLeaveManager(AccountRepository accountRepository){
        super(accountRepository);
    }

    @Override
    protected List<ApplyRegisterDto> validate(VacationRegisterDto registerDto, VacationInfo vacationInfo){
        final LocalDate startDt = registerDto.getStartDt();
        final LocalDate endDt = registerDto.getEndDt();

        DateRange dates = new DateRange(startDt,endDt);

        //공휴일 체크
        List<LocalDate> applyDateList =
                        dates.stream()
                        .filter(date->!HolidayChecker.isHoliday(date))
                        .collect(Collectors.toList());

        if(CollectionUtils.isEmpty(applyDateList)){
            throw BadRequestException.of(REGISTER_ERROR_01);
        }

        //등록된 연차와 비교
        List<LocalDate> registerDateList =
                vacationInfo.getVacationHistoryList()
                        .stream()
                        .map(VacationHistory::getRegDt)
                        .collect(Collectors.toList());

        List<LocalDate> finalApplyDateList =
                applyDateList.stream().filter(applyDate->
                        !registerDateList.stream().anyMatch(registerDate->registerDate.equals(applyDate))
        ).collect(Collectors.toList());

        if(CollectionUtils.isEmpty(finalApplyDateList)){
            throw BadRequestException.of(REGISTER_ERROR_02);
        }

        //남은 연차일수 비교
        BigDecimal remainingDays = vacationInfo.getRemainingDays();
        BigDecimal registerDays = BigDecimal.valueOf(finalApplyDateList.stream().count());

        if(registerDays.compareTo(remainingDays) > 0){
            throw BadRequestException.of(REGISTER_ERROR_03);
        }

        return finalApplyDateList.stream().map(v->
            ApplyRegisterDto.builder()
                    .regDt(v)
                    .vacationType(VacationType.ANNUAL_LEAVE)
                    .build()
        ).collect(Collectors.toList());
    }

    @Override
    @Transactional
    protected void apply(List<ApplyRegisterDto> applyRegisterDtoList, VacationInfo vacationInfo) {

        applyRegisterDtoList =
                applyRegisterDtoList.stream()
                        .sorted(Comparator.comparing(ApplyRegisterDto::getRegDt))
                        .collect(Collectors.toList());

        for(ApplyRegisterDto applyRegisterDto : applyRegisterDtoList){
            Long maxHistoryNo = vacationInfo.getVacationHistoryList()
                    .stream()
                    .map(v->v.getHistoryNo())
                    .max(Long::compareTo)
                    .orElse(0L);

            VacationHistory applyVacationHistory = VacationHistory.
                    builder()
                    .vacationType(applyRegisterDto.getVacationType())
                    .vacationInfo(vacationInfo)
                    .accountNo(vacationInfo.getAccountNo())
                    .historyNo(maxHistoryNo+1)
                    .regDt(applyRegisterDto.getRegDt())
                    .build();

            vacationInfo.getVacationHistoryList().add(applyVacationHistory);

            BigDecimal remainingDays = vacationInfo.getRemainingDays();
            BigDecimal useDays = vacationInfo.getUseDays();

            vacationInfo.setRemainingDays(remainingDays.subtract(applyRegisterDto.getVacationType().getUseDays()));
            vacationInfo.setUseDays(useDays.add(applyRegisterDto.getVacationType().getUseDays()));
        }

    }

    @Override
    protected VacationType getType() {
        return VacationType.ANNUAL_LEAVE;
    }
}
