package com.okm1208.document.common.entity.convert;

import com.okm1208.document.common.model.ApproveStatusType;

import javax.persistence.AttributeConverter;

/**
 * @author Nick ( okm1208@gmail.com )
 * @created 2021-05-17
 */
public class ApproveStatusTypeConverter implements AttributeConverter<ApproveStatusType, String> {

    @Override
    public ApproveStatusType convertToEntityAttribute(String s) {
        return ApproveStatusType.ofApprovalStatusType(s);
    }

    @Override
    public String convertToDatabaseColumn(ApproveStatusType approveStatusType) {
        return approveStatusType.getType();
    }
}
