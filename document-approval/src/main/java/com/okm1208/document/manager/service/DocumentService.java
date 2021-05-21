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
import com.okm1208.document.manager.model.DocumentSearchResponseVo;
import com.okm1208.document.manager.model.DocumentVo;
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


    @Transactional(readOnly = true)
    public DocumentSearchResponseVo search(String searchAccountId){

        Account searchAccount = accountRepository.findByAccountId(searchAccountId)
                .orElseThrow(()->AuthenticationException.of(ErrorMessageProperties.ACCOUNT_NOT_FOUND));

        //생성한 문서 중 결제 진행 중인 문서
        List<DocumentVo> outboxDocumentList = searchAccount.getRegDocumentList()
                .stream()
                .filter(document -> ApproveStatusType.WAITING.equals(document.getApproveStatus()))
                .map(document -> new DocumentVo(document))
                .collect(Collectors.toList());

        List<Approval> myApprovalList = searchAccount.getApprovalList();
        //내가 결제를 해야 할 문서
        List<DocumentVo> inboxDocumentList = myApprovalList
                .stream()
                .filter(approval-> ApproveType.WAITING.equals(approval.getApproveType()))
                .map(approval -> new DocumentVo(approval.getDocument()))
                .collect(Collectors.toList());

        //내가 관여한 문서 중 결제가 완료(승인 또는 거절) 된 문서
        List<DocumentVo> archiveDocumentList = myApprovalList
                .stream()
                .map(approval -> new DocumentVo(approval.getDocument()))
                .filter(document-> ApproveStatusType.REJECT.equals(document.getApproveStatus()) ||
                        ApproveStatusType.APPROVE.equals(document.getApproveStatus()))
                .collect(Collectors.toList());

        return new DocumentSearchResponseVo(inboxDocumentList, outboxDocumentList,archiveDocumentList);
    }

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

//        createAccount.getApprovalList().add(document);
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
    public void approve(String approveAccountId,
                        Long documentNo ,
                        ApproveType reqApproveType,
                        String comment){

        if(ApproveType.WAITING.equals(reqApproveType)){
            throw BadRequestException.of(ErrorMessageProperties.APPROVE_ERROR_05);
        }
        Account approvalAccount =
                accountRepository.findByAccountId(approveAccountId).orElseThrow(
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
        possibleApproval.setComment(comment);
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
