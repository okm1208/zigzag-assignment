package com.okm1208.document.manager.model;

import com.okm1208.document.common.model.DocumentType;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author Nick ( okm1208@gmail.com )
 * @created 2021-05-17
 */
@Data
@Builder
public class DocumentCreateRequestVo {
    @NotNull
    private String title;
    @NotNull
    private DocumentType type;
    @NotNull
    private String content;

    @NotEmpty
    private List<Long> approvalAccountNoList;
}
