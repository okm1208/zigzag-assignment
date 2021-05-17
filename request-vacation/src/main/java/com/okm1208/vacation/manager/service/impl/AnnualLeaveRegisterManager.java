package com.okm1208.vacation.manager.service.impl;

import com.okm1208.vacation.account.repository.AccountRepository;
import com.okm1208.vacation.common.entity.VacationHistory;
import com.okm1208.vacation.common.entity.VacationInfo;
import com.okm1208.vacation.common.enums.VacationType;
import com.okm1208.vacation.common.exception.BadRequestException;
import com.okm1208.vacation.common.utils.DateRange;
import com.okm1208.vacation.common.utils.HolidayChecker;
import com.okm1208.vacation.manager.model.ApplyRegisterDto;
import com.okm1208.vacation.manager.model.VacationRegisterDto;
import com.okm1208.vacation.manager.service.VacationRegisterManager;
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

public class AnnualLeaveRegisterManager extends VacationRegisterManager {

    public AnnualLeaveRegisterManager(AccountRepository accountRepository){
        super(accountRepository);
    }

    @Override
    protected List<ApplyRegisterDto> validate(VacationRegisterDto registerDto, VacationInfo vacationInfo){
        final LocalDate startDt = registerDto.getStartDt();
        final LocalDate endDt = registerDto.getEndDt();

        DateRange applyRangeDates = new DateRange(startDt,endDt);

        //공휴일 체크
        List<LocalDate> possibleDateList =
                    applyRangeDates.stream()
                            .filter(date->!HolidayChecker.isHoliday(date))
                            .collect(Collectors.toList());

        if(CollectionUtils.isEmpty(possibleDateList)){
            throw BadRequestException.of(REGISTER_ERROR_01);
        }

        //등록된 연차와 비교
        List<LocalDate> registeredDateList =
                vacationInfo.getVacationHistoryList()
                        .stream()
                        .map(VacationHistory::getRegDt)
                        .collect(Collectors.toList());

        List<LocalDate> finalApplyDateList =
                possibleDateList.stream().filter(applyDate->
                        !registeredDateList.stream().anyMatch(registerDate->registerDate.equals(applyDate))
        ).collect(Collectors.toList());

        if(CollectionUtils.isEmpty(finalApplyDateList)){
            throw BadRequestException.of(REGISTER_ERROR_02);
        }

        //남은 연차 일수 체크
        BigDecimal remainingDays = vacationInfo.getRemainingDays();
        BigDecimal registerDays = BigDecimal.valueOf(finalApplyDateList.stream().count());

        if(registerDays.compareTo(remainingDays) > 0){
            throw BadRequestException.of(REGISTER_ERROR_03);
        }

        return finalApplyDateList.stream().map(v->
            ApplyRegisterDto.builder()
                    .regDt(v)
                    .vacationType(VacationType.ANNUAL_LEAVE)
                    .comment(registerDto.getComment())
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
            VacationHistory applyVacationHistory = VacationHistory.
                    builder()
                    .vacationType(applyRegisterDto.getVacationType())
                    .vacationInfo(vacationInfo)
                    .accountNo(vacationInfo.getAccountNo())
                    .regDt(applyRegisterDto.getRegDt())
                    .comment(applyRegisterDto.getComment())
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
