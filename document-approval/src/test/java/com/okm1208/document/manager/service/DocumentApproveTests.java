package com.okm1208.document.manager.service;

import com.okm1208.document.common.entity.Account;
import com.okm1208.document.common.entity.Approval;
import com.okm1208.document.common.entity.Document;
import com.okm1208.document.common.exception.AuthorityException;
import com.okm1208.document.common.exception.BadRequestException;
import com.okm1208.document.common.model.ApproveType;
import com.okm1208.document.common.msg.ErrorMessageProperties;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Import(value = { DocumentService.class, DocumentValidator.class } )
@DataJpaTest
public class DocumentApproveTests {

    @Autowired
    private DocumentValidator documentValidator;

    @Test
    public void 문서_결제_유효성검사_테스트(){

        //CASE 1 : 결제 권한 없음
        Account approvalRequestAccount = Account.builder()
                .accountNo(1L)
                .build();
        Document document = Document
                .builder()
                .build();

        Approval impossibleApproval = Approval.builder()
                .account(makeMockAccount(2L))
                .document(document)
                .approveType(ApproveType.WAITING)
                .orderNo(1L)
                .build();
        Approval impossibleApproval2 = Approval.builder()
                .account(makeMockAccount(3L))
                .document(document)
                .approveType(ApproveType.WAITING)
                .orderNo(2L)
                .build();
        List<Approval> impossibleApprovalList = new ArrayList<>();
        impossibleApprovalList.add(impossibleApproval);
        impossibleApprovalList.add(impossibleApproval2);

        document.setApprovalList(impossibleApprovalList);

        AuthorityException authorityException = assertThrows(
                AuthorityException.class,
                () ->   documentValidator.approveValidate(approvalRequestAccount,document));
        assertThat(authorityException.getMessage(), is(ErrorMessageProperties.APPROVE_ERROR_01));

        //CASE2 : 결제 불가능한 상태의 문서
        Approval impossibleApproval3 = Approval.builder()
                .account(makeMockAccount(1L))
                .document(document)
                .approveType(ApproveType.APPROVE)
                .orderNo(3L)
                .build();

        impossibleApprovalList.add(impossibleApproval3);
        document.setApprovalList(impossibleApprovalList);

        BadRequestException badRequestException = assertThrows(
                BadRequestException.class,
                () ->   documentValidator.approveValidate(approvalRequestAccount,document));
        assertThat(badRequestException.getMessage(), is(ErrorMessageProperties.APPROVE_ERROR_02));

        //CASE3 : 선 결제 건 처리 여부 확인

        Account approvalRequestAccount2 = Account.builder()
                .accountNo(3L)
                .build();
        badRequestException = assertThrows(
                BadRequestException.class,
                () ->   documentValidator.approveValidate(approvalRequestAccount2,document));
        assertThat(badRequestException.getMessage(), is(ErrorMessageProperties.APPROVE_ERROR_03));

    }
    private Account makeMockAccount(Long accountNo){
        return Account.builder().accountNo(accountNo).build();
    }
}