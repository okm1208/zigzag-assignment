package com.okm1208.document.manager.service;

import com.okm1208.document.account.repository.AccountRepository;
import com.okm1208.document.common.entity.Account;
import com.okm1208.document.common.entity.Approval;
import com.okm1208.document.common.entity.Document;
import com.okm1208.document.common.model.ApproveStatusType;
import com.okm1208.document.common.model.ApproveType;
import com.okm1208.document.manager.model.DocumentCreateRequestVo;
import com.okm1208.document.manager.repository.ApprovalRepository;
import com.okm1208.document.manager.repository.DocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Long create(String createAccountId, DocumentCreateRequestVo documentCreateRequest){
        Account createAccount = accountRepository.findByAccountId(createAccountId);

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


}
