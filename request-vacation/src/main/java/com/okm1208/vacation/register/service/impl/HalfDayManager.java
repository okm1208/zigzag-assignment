package com.okm1208.vacation.register.service.impl;

import com.okm1208.vacation.account.repository.AccountRepository;
import com.okm1208.vacation.common.entity.VacationHistory;
import com.okm1208.vacation.common.entity.VacationInfo;
import com.okm1208.vacation.common.enums.VacationType;
import com.okm1208.vacation.common.exception.BadRequestException;
import com.okm1208.vacation.common.utils.HolidayChecker;
import com.okm1208.vacation.register.model.ApplyRegisterDto;
import com.okm1208.vacation.register.model.VacationRegisterDto;
import com.okm1208.vacation.register.service.VacationManager;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static com.okm1208.vacation.common.msg.ErrorMessageProperties.*;

/**
 * @author Nick ( okm1208@gmail.com )
 * @created 2021-05-15
 */
public class HalfDayManager extends VacationManager {

    public HalfDayManager(AccountRepository accountRepository){
        super(accountRepository);
    }

    @Override
    protected List<ApplyRegisterDto> validate(VacationRegisterDto registerDto , VacationInfo vacationInfo){
        final LocalDate startDt = registerDto.getStartDt();

        //공휴일 체크
        if(HolidayChecker.isHoliday(registerDto.getStartDt())){
            throw BadRequestException.of(REGISTER_ERROR_01);
        }

        BigDecimal useDays = vacationInfo.getVacationHistoryList()
                .stream()
                .filter(v->v.getRegDt().equals(startDt))
                .map(v->v.getVacationType().getUseDays())
                .reduce(BigDecimal.ZERO,BigDecimal::add);

        BigDecimal registerDays = registerDto.getVacationType().getUseDays();

        //휴가를 등록할 날짜에 추가로 등록 가능 여부 체크
        if(useDays.add(registerDays).compareTo(BigDecimal.ONE) > 0){
            throw BadRequestException.of(REGISTER_ERROR_02);
        }

        BigDecimal remainingDays = vacationInfo.getRemainingDays();

        //남은 연차 체크
        if(registerDays.compareTo(remainingDays) > 0){
            throw BadRequestException.of(REGISTER_ERROR_03);
        }

        return Arrays.asList(ApplyRegisterDto
                .builder()
                .regDt(startDt)
                .vacationType(registerDto.getVacationType())
                .build());
    }

    @Override
    @Transactional
    protected void apply(List<ApplyRegisterDto> applyRegisterDtoList, VacationInfo vacationInfo) {
        ApplyRegisterDto applyRegisterDto = applyRegisterDtoList.get(0);

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

    @Override
    protected VacationType getType() {
        return VacationType.HALF_DAY_LEAVE;
    }
}
