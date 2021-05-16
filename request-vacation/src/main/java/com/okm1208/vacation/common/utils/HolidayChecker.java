package com.okm1208.vacation.common.utils;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Nick ( okm1208@gmail.com )
 * @created 2021-05-15
 *
 * 공휴일 체크 로직
 * 1. 토요일, 일요일
 * 2. 법정 공휴일 , 대체 휴무일 ( 추후, 오픈 API 활용 )
 */
public class HolidayChecker {
    private static Map<LocalDate, Boolean> publicHolidayList = makePublicHoliday();

    public static boolean isHoliday(LocalDate inspectDate){
        if(isWeekend(inspectDate) || isPublicHoliday(inspectDate)){
            return true;
        }
        return false;
    }

    private static boolean isWeekend(LocalDate inspectDate){
        DayOfWeek dayOfWeek = inspectDate.getDayOfWeek();
        if(dayOfWeek == DayOfWeek.SUNDAY || dayOfWeek == DayOfWeek.SATURDAY){
            return true;
        }
        return false;
    }

    private static boolean isPublicHoliday(LocalDate inspectDate){
        if(publicHolidayList.containsKey(inspectDate)){
            return true;
        }
        return false;
    }


    private static Map<LocalDate, Boolean> makePublicHoliday(){
        Map<LocalDate, Boolean> publicHolidayMap = new HashMap<>();
        publicHolidayMap.put(LocalDate.parse("2021-01-01"), true);
        publicHolidayMap.put(LocalDate.parse("2021-02-11"), true);
        publicHolidayMap.put(LocalDate.parse("2021-02-12"), true);
        publicHolidayMap.put(LocalDate.parse("2021-02-13"), true);
        publicHolidayMap.put(LocalDate.parse("2021-03-01"), true);
        publicHolidayMap.put(LocalDate.parse("2021-05-05"), true);
        publicHolidayMap.put(LocalDate.parse("2021-05-19"), true);
        publicHolidayMap.put(LocalDate.parse("2021-06-06"), true);
        publicHolidayMap.put(LocalDate.parse("2021-08-15"), true);
        publicHolidayMap.put(LocalDate.parse("2021-09-20"), true);
        publicHolidayMap.put(LocalDate.parse("2021-09-21"), true);
        publicHolidayMap.put(LocalDate.parse("2021-09-22"), true);
        publicHolidayMap.put(LocalDate.parse("2021-10-03"), true);
        publicHolidayMap.put(LocalDate.parse("2021-10-09"), true);
        publicHolidayMap.put(LocalDate.parse("2021-12-25"), true);
        return publicHolidayMap;
    }




}
