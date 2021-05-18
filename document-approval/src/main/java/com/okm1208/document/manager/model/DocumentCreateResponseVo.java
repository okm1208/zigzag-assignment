package com.okm1208.document.manager.model;

import com.okm1208.document.common.entity.Document;
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
public class DocumentCreateResponseVo {
    private Long documentNo;

    public DocumentCreateResponseVo(Long documentNo){
        this.documentNo = documentNo;
    }


}
