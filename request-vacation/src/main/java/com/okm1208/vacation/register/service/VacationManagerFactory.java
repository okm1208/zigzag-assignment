package com.okm1208.vacation.register.service;

import com.okm1208.vacation.common.enums.VacationType;
import com.okm1208.vacation.common.exception.InternalServerException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Nick ( okm1208@gmail.com )
 * @created 2021-05-16
 */
@Slf4j
@Service
public class VacationManagerFactory {

    private static final Map<VacationType,VacationManager> vacationManagerCache = new HashMap<>();

    @Autowired
    private List<VacationManager> vacationManagerList;

    @PostConstruct
    public void initVacationManagerServiceCache(){
        for(VacationManager vacationManager : vacationManagerList){
            vacationManagerCache.put(vacationManager.getType(),vacationManager);
        }
    }

    public static VacationManager getVacationManager(VacationType vacationType){
        VacationManager vacationManager = vacationManagerCache.get(vacationType);

        if(vacationManager == null){
            if(VacationType.HALF_AND_HALF_LEAVE.equals(vacationType)){
                vacationManager = vacationManagerCache.get(VacationType.HALF_DAY_LEAVE);
            }
            if(vacationManager == null){
                throw InternalServerException.of("잘못된 서비스 접근 입니다.");
            }
        }
        return vacationManager;
    }

}
