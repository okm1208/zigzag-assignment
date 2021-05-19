package com.okm1208.document.manager.service;

import com.okm1208.document.account.repository.AccountRepository;
import com.okm1208.document.common.entity.Account;
import com.okm1208.document.common.entity.Approval;
import com.okm1208.document.common.entity.Document;
import com.okm1208.document.common.exception.AuthenticationException;
import com.okm1208.document.common.exception.AuthorityException;
import com.okm1208.document.common.exception.BadRequestException;
import com.okm1208.document.common.exception.DataNotFoundException;
import com.okm1208.document.common.model.ApproveStatusType;
import com.okm1208.document.common.model.ApproveType;
import com.okm1208.document.common.msg.ErrorMessageProperties;
import com.okm1208.document.manager.model.DocumentCreateRequestVo;
import com.okm1208.document.manager.repository.ApprovalRepository;
import com.okm1208.document.manager.repository.DocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Nick ( okm1208@gmail.com )
 * @created 2021-05-18
 */
@Service
public class DocumentService {

    @Autowired
    private DocumentValidator documentValidator;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private DocumentRepository documentRepository;

    @Autowired
    private ApprovalRepository approvalRepository;

    @Transactional
    public Long create(String createAccountId, DocumentCreateRequestVo documentCreateRequest){
        Account createAccount = accountRepository.findByAccountId(createAccountId)
                .orElseThrow(()->AuthenticationException.of(ErrorMessageProperties.ACCOUNT_NOT_FOUND));

        documentValidator.createValidate(documentCreateRequest.getApprovalAccountNoList());

        Document document = Document
                .builder()
                .approveStatus(ApproveStatusType.WAITING)
                .content(documentCreateRequest.getContent())
                .title(documentCreateRequest.getTitle())
                .accountNo(createAccount.getAccountNo())
                .account(createAccount)
                .type(documentCreateRequest.getType())
                .approvalList(new ArrayList<>())
                .build();

        createAccount.getRegDocumentList().add(document);
        documentRepository.save(document);

        Long orderNo = 0L;
        for(Long accountNo : documentCreateRequest.getApprovalAccountNoList().stream().distinct().collect(Collectors.toList())){
            Account approvalAccount;
            //자기 자신도 결제승인자로 저장 가능
            if(accountNo == createAccount.getAccountNo()){
                approvalAccount = createAccount;
            }else{
                approvalAccount = accountRepository.findById(accountNo).get();
            }
            Approval approval = Approval
                                    .builder()
                                    .account(approvalAccount)
                                    .approveType(ApproveType.WAITING)
                                    .orderNo(orderNo+1)
                                    .document(document)
                                    .account(approvalAccount)
                                    .build();

            document.getApprovalList().add(approval);
            approvalRepository.save(approval);
            orderNo++;
        }
        return document.getDocumentNo();
    }


    //문서 결제
    @Transactional
    public void approve(String createAccountId, Long documentNo , ApproveType reqApproveType){

        if(ApproveType.WAITING.equals(reqApproveType)){
            throw BadRequestException.of(ErrorMessageProperties.APPROVE_ERROR_05);
        }
        Account approvalAccount =
                accountRepository.findByAccountId(createAccountId).orElseThrow(
                        ()->AuthenticationException.of(ErrorMessageProperties.ACCOUNT_NOT_FOUND));

        Document approvalDocument = documentRepository.findById(documentNo).orElseThrow(
                ()-> DataNotFoundException.of(ErrorMessageProperties.EMPTY_DATA));

        documentValidator.approveValidate(approvalAccount, approvalDocument);

        Approval possibleApproval = approvalDocument.getApprovalList()
                .stream()
                .filter(approval->approval.getAccount().getAccountNo().equals(approvalAccount.getAccountNo()))
                .findFirst()
                .orElseThrow(()-> AuthorityException.of(ErrorMessageProperties.APPROVE_ERROR_01));


        possibleApproval.setApproveType(reqApproveType);

        approvalDocument.setApproveStatus(
                findApproveStatusType(
                        approvalDocument
                                .getApprovalList()
                                .stream()
                                .map(Approval::getApproveType)
                                .collect(Collectors.toList()))
        );
    }

    private ApproveStatusType findApproveStatusType(List<ApproveType> approveTypeList){
        if( approveTypeList.stream().allMatch(approveType -> ApproveType.WAITING.equals(approveType))){
            return ApproveStatusType.WAITING;
        }else if(approveTypeList.stream().allMatch(approveType -> ApproveType.APPROVE.equals(approveType))){
            return ApproveStatusType.APPROVE;
        }else if(approveTypeList.stream().anyMatch(approveType -> ApproveType.REJECT.equals(approveType))){
            return ApproveStatusType.REJECT;
        }else{
            return ApproveStatusType.APPROVING;
        }
    }
}
