package com.okm1208.document.manager.service;

import com.okm1208.document.account.repository.AccountRepository;
import com.okm1208.document.common.exception.BadRequestException;
import com.okm1208.document.common.msg.ErrorMessageProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @author Nick ( okm1208@gmail.com )
 * @created 2021-05-18
 */
@Service
public class DocumentValidator {

    @Autowired
    private AccountRepository accountRepository;

    public void createValidate(List<Long> approvalAccountNoList){
        if(CollectionUtils.isEmpty(approvalAccountNoList)){
            throw BadRequestException.of(ErrorMessageProperties.CREATE_ERROR_01);
        }

        for(Long accountNo : approvalAccountNoList){
            accountRepository.findById(accountNo)
                    .orElseThrow(() ->BadRequestException.of(ErrorMessageProperties.CREATE_ERROR_02));
        }
    }


}
