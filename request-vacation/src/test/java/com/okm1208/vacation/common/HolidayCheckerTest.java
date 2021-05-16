package com.okm1208.vacation.common;

import com.okm1208.vacation.common.utils.HolidayChecker;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class HolidayCheckerTest {

    private HolidayChecker holidayChecker = new HolidayChecker();

    @Test
    public void 공휴일_체크_테스트(){
        LocalDate weekendDate = LocalDate.parse("2021-01-01");
        boolean result = ReflectionTestUtils.invokeMethod(holidayChecker, "isPublicHoliday" , weekendDate);
        assertThat(result, is(true));

        weekendDate = LocalDate.parse("2021-02-10");
        result = ReflectionTestUtils.invokeMethod(holidayChecker, "isPublicHoliday" , weekendDate);
        assertThat(result, is(false));

        weekendDate = LocalDate.parse("2021-02-11");
        result = ReflectionTestUtils.invokeMethod(holidayChecker, "isPublicHoliday" , weekendDate);
        assertThat(result, is(true));

        weekendDate = LocalDate.parse("2021-02-12");
        result = ReflectionTestUtils.invokeMethod(holidayChecker, "isPublicHoliday" , weekendDate);
        assertThat(result, is(true));

        weekendDate = LocalDate.parse("2021-12-25");
        result = ReflectionTestUtils.invokeMethod(holidayChecker, "isPublicHoliday" , weekendDate);
        assertThat(result, is(true));
    }

    @Test
    public void 주말_체크_테스트(){
        LocalDate weekendDate = LocalDate.parse("2021-05-15");
        boolean result = ReflectionTestUtils.invokeMethod(holidayChecker, "isWeekend" , weekendDate);
        assertThat(result, is(true));

        weekendDate = LocalDate.parse("2021-05-16");
        result = ReflectionTestUtils.invokeMethod(holidayChecker, "isWeekend" , weekendDate);
        assertThat(result, is(true));

        weekendDate = LocalDate.parse("2021-05-17");
        result = ReflectionTestUtils.invokeMethod(holidayChecker, "isWeekend" , weekendDate);
        assertThat(result, is(false));

        weekendDate = LocalDate.parse("2021-07-25");
        result = ReflectionTestUtils.invokeMethod(holidayChecker, "isWeekend" , weekendDate);
        assertThat(result, is(true));

        weekendDate = LocalDate.parse("2021-07-26");
        result = ReflectionTestUtils.invokeMethod(holidayChecker, "isWeekend" , weekendDate);
        assertThat(result, is(false));

        weekendDate = LocalDate.parse("2021-12-31");
        result = ReflectionTestUtils.invokeMethod(holidayChecker, "isWeekend" , weekendDate);
        assertThat(result, is(false));

        weekendDate = LocalDate.parse("2022-01-01");
        result = ReflectionTestUtils.invokeMethod(holidayChecker, "isWeekend" , weekendDate);
        assertThat(result, is(true));

        weekendDate = LocalDate.parse("2022-01-14");
        result = ReflectionTestUtils.invokeMethod(holidayChecker, "isWeekend" , weekendDate);
        assertThat(result, is(false));
    }

}