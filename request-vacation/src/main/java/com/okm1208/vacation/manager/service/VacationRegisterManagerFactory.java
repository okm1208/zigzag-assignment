package com.okm1208.vacation.manager.service;

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
public class VacationRegisterManagerFactory {

    private static final Map<VacationType, VacationRegisterManager> vacationManagerCache = new HashMap<>();

    @Autowired
    private List<VacationRegisterManager> vacationRegisterManagerList;

    @PostConstruct
    public void initVacationManagerServiceCache(){
        for(VacationRegisterManager vacationRegisterManager : vacationRegisterManagerList){
            vacationManagerCache.put(vacationRegisterManager.getType(), vacationRegisterManager);
        }
    }

    public static VacationRegisterManager getVacationManager(VacationType vacationType){
        VacationRegisterManager vacationRegisterManager = vacationManagerCache.get(vacationType);

        if(vacationRegisterManager == null){
            if(VacationType.HALF_AND_HALF_LEAVE.equals(vacationType)){
                vacationRegisterManager = vacationManagerCache.get(VacationType.HALF_DAY_LEAVE);
            }
            if(vacationRegisterManager == null){
                throw InternalServerException.of("잘못된 서비스 접근 입니다.");
            }
        }
        return vacationRegisterManager;
    }

}
