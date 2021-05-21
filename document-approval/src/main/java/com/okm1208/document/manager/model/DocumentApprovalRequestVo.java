package com.okm1208.document.manager.model;

import com.okm1208.document.common.model.ApproveType;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author Nick ( okm1208@gmail.com )
 * @created 2021-05-17
 */
@Data
public class DocumentApprovalRequestVo {
    @NotNull
    private ApproveType approveType;

    private String comment;
}
