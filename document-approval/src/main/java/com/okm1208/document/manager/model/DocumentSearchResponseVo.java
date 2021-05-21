package com.okm1208.document.manager.model;

import lombok.Data;

import java.util.List;

/**
 * @author Nick ( okm1208@gmail.com )
 * @created 2021-05-21
 */
@Data
public class DocumentSearchResponseVo {

    List<DocumentVo> inboxDocumentList;
    List<DocumentVo> outboxDocumentList;
    List<DocumentVo> archiveDocumentList;

    public DocumentSearchResponseVo(List<DocumentVo> inboxDocumentList,
                                    List<DocumentVo> outboxDocumentList,
                                    List<DocumentVo> archiveDocumentList){
        this.inboxDocumentList = inboxDocumentList;
        this.outboxDocumentList = outboxDocumentList;
        this.archiveDocumentList = archiveDocumentList;
    }
}
