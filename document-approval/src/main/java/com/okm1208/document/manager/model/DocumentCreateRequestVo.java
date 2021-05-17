package com.okm1208.document.manager.model;

import com.okm1208.document.common.model.DocumentType;
import lombok.Data;

import java.util.List;

/**
 * @author Nick ( okm1208@gmail.com )
 * @created 2021-05-17
 */
@Data
public class DocumentCreateRequestVo {
    private String title;
    private DocumentType type;
    private String content;

    private List<Long> approvalAccountNoList;
}
