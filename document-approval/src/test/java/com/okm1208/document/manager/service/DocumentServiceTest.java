package com.okm1208.document.manager.service;

import com.okm1208.document.account.repository.AccountRepository;
import com.okm1208.document.common.entity.Account;
import com.okm1208.document.common.model.DocumentType;
import com.okm1208.document.manager.model.DocumentCreateRequestVo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.Arrays;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Import(value = { DocumentService.class, DocumentValidator.class } )
@DataJpaTest
public class DocumentServiceTest {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private DocumentService documentService;

    final Long adminNo = 1L;
    final String adminId = "admin";

    final Long admin2No = 2L;
    final String admin2Id = "admin2";

    final Long admin3No = 3L;
    final String admin3Id = "admin3";
    @Test
    public void 문서_생성_및_결제_입력_테스트(){
        //CASE : 셀프 생성 및 결제
        DocumentCreateRequestVo requestVo = DocumentCreateRequestVo
                .builder()
                .approvalAccountNoList(Arrays.asList(adminNo))
                .content("내용!!")
                .title("결제 올립니다.")
                .type(DocumentType.NORMAL)
                .build();
        documentService.create(adminId,requestVo);
        Account approveAccount =
                accountRepository.findById(adminNo).orElseThrow(()->new RuntimeException("find account exception."));

        assertNotNull(approveAccount.getRegDocumentList());
        assertNotNull(approveAccount.getApprovalList());

        assertThat(approveAccount.getRegDocumentList().size(), is(1));
        assertThat(approveAccount.getRegDocumentList().get(0).getAccountNo(), is(adminNo));

        assertThat(approveAccount.getApprovalList().size(), is(1));
        assertThat(approveAccount.getApprovalList().get(0).getAccount().getAccountNo(), is(adminNo));
    }

    @Test
    public void 문서_생성_및_결제_입력_테스트2(){
        //CASE : 타 계정 결제 신청
        DocumentCreateRequestVo requestVo = DocumentCreateRequestVo
                .builder()
                .approvalAccountNoList(Arrays.asList(adminNo, admin3No,admin2No))
                .content("내용!!")
                .title("결제 올라갑니다.")
                .type(DocumentType.NORMAL)
                .build();
        documentService.create(adminId,requestVo);

        Account docCreateAccount =
                accountRepository.findById(adminNo).orElseThrow(()->new RuntimeException("find docCreateAccount exception."));

        assertNotNull(docCreateAccount.getRegDocumentList());
        assertNotNull(docCreateAccount.getApprovalList());
        assertThat(docCreateAccount.getRegDocumentList().size(), is(1));
        assertThat(docCreateAccount.getRegDocumentList().get(0).getAccountNo(), is(adminNo));
        assertThat(docCreateAccount.getApprovalList().size(), is(1));
        assertThat(docCreateAccount.getApprovalList().get(0).getAccount().getAccountNo(), is(adminNo));
        assertThat(docCreateAccount.getApprovalList().get(0).getOrderNo(), is(1L));

        Account approvalAccount =
                accountRepository.findById(admin2No).orElseThrow(()->new RuntimeException("find approvalAccount exception."));

        assertNotNull(approvalAccount.getApprovalList());
        assertThat(approvalAccount.getRegDocumentList().size(), is(0));
        assertThat(approvalAccount.getApprovalList().size(), is(1));
        assertThat(approvalAccount.getApprovalList().get(0).getAccount().getAccountNo(), is(admin2No));
        assertThat(approvalAccount.getApprovalList().get(0).getOrderNo(), is(3L));

        Account approvalAccount2 =
                accountRepository.findById(admin3No).orElseThrow(()->new RuntimeException("find approvalAccount exception."));


        assertNotNull(approvalAccount2.getApprovalList());
        assertThat(approvalAccount2.getRegDocumentList().size(), is(0));
        assertThat(approvalAccount2.getApprovalList().size(), is(1));
        assertThat(approvalAccount2.getApprovalList().get(0).getAccount().getAccountNo(), is(admin3No));
        assertThat(approvalAccount2.getApprovalList().get(0).getOrderNo(), is(2L));
    }

}