package com.okm1208.vacation.common.entity.convert;

import com.okm1208.vacation.common.enums.VacationType;

import javax.persistence.AttributeConverter;

/**
 * @author Nick (okm1208@gmail.com)
 * @created 2021-05-14
 */
public class VacationTypeConverter implements AttributeConverter<VacationType,String> {

    @Override
    public String convertToDatabaseColumn(VacationType vacationType) {
        return vacationType.getType();
    }

    @Override
    public VacationType convertToEntityAttribute(String type) {
        return VacationType.ofVacationType(type);
    }
}
