package com.okm1208.document.manager.service;

import com.okm1208.document.account.repository.AccountRepository;
import com.okm1208.document.common.entity.Account;
import com.okm1208.document.common.entity.Approval;
import com.okm1208.document.common.entity.Document;
import com.okm1208.document.common.exception.AuthorityException;
import com.okm1208.document.common.exception.BadRequestException;
import com.okm1208.document.common.model.ApproveType;
import com.okm1208.document.common.msg.ErrorMessageProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Comparator;
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

    public void approveValidate(Account approvalAccount, Document document){

        final Long approvalAccountNo = approvalAccount.getAccountNo();

        // 1.문서 결제 자격 확인
        Approval possibleApproval = document.getApprovalList()
                .stream()
                .filter(approval ->approval.getAccount().getAccountNo().equals(approvalAccountNo))
                .findFirst()
                .orElseThrow(()->AuthorityException.of(ErrorMessageProperties.APPROVE_ERROR_01));

        // 2. 결제 문서 상태 확인
        if( !ApproveType.WAITING.equals(possibleApproval.getApproveType())){
            throw BadRequestException.of(ErrorMessageProperties.APPROVE_ERROR_02);
        }

        // 3. 결제 순서 확인
        // 이전 승인 요청들이 모두 결제 승인 상태 이어야 함..
        final Approval impossibleApproval = document.getApprovalList()
                .stream()
                .sorted(Comparator.comparing(Approval::getOrderNo))
                .filter(approval -> approval.getOrderNo() < possibleApproval.getOrderNo()
                                        && !ApproveType.APPROVE.equals(approval.getApproveType()) )
                .findFirst()
                .orElse(null);
        if(impossibleApproval != null){
            switch (impossibleApproval.getApproveType()){
                case WAITING: throw BadRequestException.of(ErrorMessageProperties.APPROVE_ERROR_03);
                case REJECT: throw BadRequestException.of(ErrorMessageProperties.APPROVE_ERROR_04);
                default:  throw BadRequestException.of(ErrorMessageProperties.APPROVE_ERROR_02);
            }
        }
    }
}
