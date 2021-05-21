package com.okm1208.document.manager.service;

import com.okm1208.document.common.model.DocumentType;
import com.okm1208.document.manager.model.DocumentCreateRequestVo;
import com.okm1208.document.manager.model.DocumentSearchResponseVo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.Arrays;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@Import(value = { DocumentService.class, DocumentValidator.class} )
@DataJpaTest
public class DocumentSearchTests {

    @Autowired
    private DocumentService documentService;

    final Long adminNo = 1L;
    final String adminId = "admin";

    final Long admin2No = 2L;
    final String admin2Id = "admin2";

    final Long admin3No = 3L;
    final String admin3Id = "admin3";

    @Test
    public void 조회_테스트() {
        DocumentCreateRequestVo requestVo = DocumentCreateRequestVo
                .builder()
                .approvalAccountNoList(Arrays.asList(adminNo))
                .content("내용!!")
                .title("결제 올립니다.")
                .type(DocumentType.NORMAL)
                .build();
        documentService.create(adminId, requestVo);

        // CASE : 생성 문서 1개 ,  결제 문서 1개
        DocumentSearchResponseVo responseVo = documentService.search(adminId);

        assertThat(responseVo.getOutboxDocumentList().size(), is(1));
        assertThat(responseVo.getInboxDocumentList().size(), is(1));
        assertThat(responseVo.getArchiveDocumentList().size(), is(0));


        requestVo = DocumentCreateRequestVo
                .builder()
                .approvalAccountNoList(Arrays.asList(adminNo,admin3No))
                .content("내용222!!")
                .title("결제 올립니다.")
                .type(DocumentType.NORMAL)
                .build();

        //생성 문서 1개
        documentService.create(admin2Id, requestVo);

        responseVo = documentService.search(admin2Id);
        assertThat(responseVo.getOutboxDocumentList().size(), is(1));
        assertThat(responseVo.getInboxDocumentList().size(), is(0));
        assertThat(responseVo.getArchiveDocumentList().size(), is(0));


        //결제 진행 해야할 문서 1
        responseVo = documentService.search(admin3Id);
        assertThat(responseVo.getOutboxDocumentList().size(),is(0));
        assertThat(responseVo.getInboxDocumentList().size(), is(1));
        assertThat(responseVo.getArchiveDocumentList().size(), is(0));
    }

}