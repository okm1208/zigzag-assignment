package com.okm1208.document.manager.service;

import com.okm1208.document.common.entity.Document;
import com.okm1208.document.common.exception.AuthorityException;
import com.okm1208.document.common.exception.BadRequestException;
import com.okm1208.document.common.exception.DataNotFoundException;
import com.okm1208.document.common.model.ApproveStatusType;
import com.okm1208.document.common.model.ApproveType;
import com.okm1208.document.common.model.DocumentType;
import com.okm1208.document.common.msg.ErrorMessageProperties;
import com.okm1208.document.manager.model.DocumentCreateRequestVo;
import com.okm1208.document.manager.repository.DocumentRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Arrays;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Import(value = { DocumentService.class, DocumentValidator.class } )
@DataJpaTest
public class DocumentCreateTests {

    @Autowired
    private DocumentService documentService;

    @Autowired
    private DocumentRepository documentRepository;


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
        Long documentNo = documentService.create(adminId,requestVo);
        Document document = documentRepository.findById(documentNo).orElseThrow(()->new RuntimeException("find document error"));

        assertThat(document.getAccount().getAccountNo(),is(adminNo));
        assertThat(document.getApprovalList().size(), is(1));
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
        Long documentNo = documentService.create(adminId,requestVo);

        Document document = documentRepository.findById(documentNo).orElseThrow(()->new RuntimeException("find document error"));

        assertThat(document.getAccount().getAccountNo(),is(adminNo));
        assertThat(document.getApprovalList().size(), is(3));
    }


    @Test
    public void 문서_상태_결정_테스트(){

        ApproveStatusType resultApproveStatusType = ReflectionTestUtils.invokeMethod(documentService,
                "findApproveStatusType" ,
                Arrays.asList(ApproveType.WAITING, ApproveType.WAITING, ApproveType.WAITING));

        assertThat(resultApproveStatusType,is(ApproveStatusType.WAITING));

        resultApproveStatusType = ReflectionTestUtils.invokeMethod(documentService,
                        "findApproveStatusType" ,
        Arrays.asList(ApproveType.APPROVE, ApproveType.APPROVE, ApproveType.WAITING));
        assertThat(resultApproveStatusType,is(ApproveStatusType.APPROVING));


        resultApproveStatusType = ReflectionTestUtils.invokeMethod(documentService,
                "findApproveStatusType" ,
                Arrays.asList(ApproveType.APPROVE, ApproveType.APPROVE, ApproveType.APPROVE));
        assertThat(resultApproveStatusType,is(ApproveStatusType.APPROVE));


        resultApproveStatusType = ReflectionTestUtils.invokeMethod(documentService,
                "findApproveStatusType" ,
                Arrays.asList(ApproveType.APPROVE, ApproveType.APPROVE, ApproveType.REJECT));
        assertThat(resultApproveStatusType,is(ApproveStatusType.REJECT));

        resultApproveStatusType = ReflectionTestUtils.invokeMethod(documentService,
                "findApproveStatusType" ,
                Arrays.asList(ApproveType.APPROVE, ApproveType.REJECT, ApproveType.WAITING));
        assertThat(resultApproveStatusType,is(ApproveStatusType.REJECT));
    }

    @Test
    public void 문서_결제_테스트(){
        DocumentCreateRequestVo requestVo = DocumentCreateRequestVo
                .builder()
                .approvalAccountNoList(Arrays.asList(adminNo, admin3No))
                .content("내용!!")
                .title("결제 올라갑니다.")
                .type(DocumentType.NORMAL)
                .build();
        Long documentNo = documentService.create(adminId,requestVo);


        // CASE 1 : 잘못된 결제 상태 요청
        BadRequestException badRequestThrown = assertThrows(
                BadRequestException.class,
                () ->  documentService.approve(adminId, documentNo, ApproveType.WAITING, "")
        );
        assertThat(badRequestThrown.getMessage(), is(ErrorMessageProperties.APPROVE_ERROR_05));


        // CASE 2: 존재 하지 않은 문서 요청
        DataNotFoundException dataNotRequestThrown = assertThrows(
                DataNotFoundException.class,
                () ->   documentService.approve(adminId, Long.MAX_VALUE, ApproveType.APPROVE, "")
        );
        assertThat(dataNotRequestThrown.getMessage(), is(ErrorMessageProperties.EMPTY_DATA));


        // CASE 3: 문서 결제 권한 없음
        AuthorityException authorityRequestThrown = assertThrows(
                AuthorityException.class,
                () ->  documentService.approve(admin2Id, documentNo, ApproveType.APPROVE, "")
        );
        assertThat(authorityRequestThrown.getMessage(), is(ErrorMessageProperties.APPROVE_ERROR_01));

        // CASE 4 : 성공
        documentService.approve(adminId, documentNo, ApproveType.APPROVE, "");
        Document applyDocument = documentRepository.findById(documentNo).orElseThrow(()-> new RuntimeException("존재하지 않은 문서"));
        assertThat(applyDocument.getApproveStatus(), is(ApproveStatusType.APPROVING));


        documentService.approve(admin3Id, documentNo, ApproveType.APPROVE, "");
        applyDocument = documentRepository.findById(documentNo).orElseThrow(()-> new RuntimeException("존재하지 않은 문서"));
        assertThat(applyDocument.getApproveStatus(), is(ApproveStatusType.APPROVE));
    }
}