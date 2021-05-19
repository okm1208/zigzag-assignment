package com.okm1208.document.entity;

import com.okm1208.document.account.repository.AccountRepository;
import com.okm1208.document.common.entity.Account;
import com.okm1208.document.common.entity.Approval;
import com.okm1208.document.common.entity.Document;
import com.okm1208.document.common.model.ApproveStatusType;
import com.okm1208.document.common.model.ApproveType;
import com.okm1208.document.common.model.DocumentType;
import com.okm1208.document.manager.repository.ApprovalRepository;
import com.okm1208.document.manager.repository.DocumentRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNotNull;

/**
 * @author Nick ( okm1208@gmail.com )
 * @created 2021-05-17
 */

@DataJpaTest
public class EntityTests {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ApprovalRepository approvalRepository;

    @Autowired
    private DocumentRepository documentRepository;

    @Test
    public void 문서_등록_및_삭제_테스트(){
        Account account = accountRepository.findByAccountId("admin").get();

        String title = "법인 카드 사용 문서";

        account.getRegDocumentList().add(Document
                .builder()
                .account(account)
                .title(title)
                .content("결제 문서 입니다.")
                .type(DocumentType.NORMAL)
                .approveStatus(ApproveStatusType.WAITING)
                .approvalList(new ArrayList<>())
                .build());

        accountRepository.save(account);

        assertNotNull(account.getRegDocumentList());
        assertThat(account.getRegDocumentList().size(), is(1));
        assertThat(account.getRegDocumentList().get(0).getType(), equalTo(DocumentType.NORMAL));
        assertThat(account.getRegDocumentList().get(0).getTitle(), equalTo(title));

        account.getRegDocumentList().remove(account.getRegDocumentList().get(0));

        accountRepository.save(account);

        assertNotNull(account.getRegDocumentList());
        assertThat(account.getRegDocumentList().size(), is(0));
    }

    @Test
    public void 문서_결제_등록_및_삭제_테스트(){
        Account account = accountRepository.findByAccountId("admin").get();
        Account account2 = accountRepository.findByAccountId("admin2").get();

        //문서 등록
        String title = "일반 결제 문서";

        Document document = Document
                .builder()
                .account(account)
                .title(title)
                .content(title)
                .type(DocumentType.NORMAL)
                .approveStatus(ApproveStatusType.WAITING)
                .approvalList(new ArrayList<>())
                .build();
        account.getRegDocumentList().add(document);

        documentRepository.save(document);

        Approval approval = Approval
                .builder()
                .account(account)
                .document(document)
                .approveType(ApproveType.WAITING)
                .orderNo(1L)
                .build();
        document.getApprovalList().add(approval);
        account.getApprovalList().add(approval);

        approvalRepository.save(approval);

        Approval approval2 = Approval
                .builder()
                .account(account2)
                .document(document)
                .approveType(ApproveType.WAITING)
                .orderNo(2L)
                .build();
        document.getApprovalList().add(approval2);
        account2.getApprovalList().add(approval2);

        approvalRepository.save(approval2);

        List<Approval> approvalList = approvalRepository.findAll();

        assertNotNull(approvalList);
        assertThat(approvalList.size(), is(2));
    }
}
