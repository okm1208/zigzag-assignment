package com.okm1208.document.common.entity.convert;

import com.okm1208.document.common.model.DocumentType;

import javax.persistence.AttributeConverter;

/**
 * @author Nick ( okm1208@gmail.com )
 * @created 2021-05-17
 */
public class DocumentTypeConverter implements AttributeConverter<DocumentType, String> {

    @Override
    public DocumentType convertToEntityAttribute(String s) {
        return DocumentType.ofDocumentType(s);
    }

    @Override
    public String convertToDatabaseColumn(DocumentType documentType) {
        return documentType.getType();
    }
}
