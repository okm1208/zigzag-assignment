package com.okm1208.document.common.entity.convert;

import com.okm1208.document.common.model.ApproveStatusType;
import com.okm1208.document.common.model.ApproveType;

import javax.persistence.AttributeConverter;

/**
 * @author Nick ( okm1208@gmail.com )
 * @created 2021-05-17
 */
public class ApproveConverter implements AttributeConverter<ApproveType, String> {

    @Override
    public ApproveType convertToEntityAttribute(String s) {
        return ApproveType.ofApprovalType(s);
    }

    @Override
    public String convertToDatabaseColumn(ApproveType approveType) {
        return approveType.getType();
    }
}
