package com.okm1208.document.manager.model;

import com.okm1208.document.common.entity.Document;
import com.okm1208.document.common.model.ApproveStatusType;
import com.okm1208.document.common.model.DocumentType;
import lombok.Data;

/**
 * @author Nick ( okm1208@gmail.com )
 * @created 2021-05-21
 */
@Data
public class DocumentVo {

    private Long documentNo;
    private String title;
    private String content;
    private DocumentType documentType;
    private ApproveStatusType approveStatus;

    public DocumentVo(Document document){
        if(document == null){
            throw new IllegalArgumentException("document is not null.");
        }
        this.documentNo = document.getDocumentNo();
        this.title = document.getTitle();
        this.content = document.getContent();
        this.approveStatus = document.getApproveStatus();
    }
}
