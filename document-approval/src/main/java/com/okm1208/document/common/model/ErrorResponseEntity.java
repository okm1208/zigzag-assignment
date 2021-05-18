package com.okm1208.document.common.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Nick ( okm1208@gmail.com )
 * @created 2021-05-15
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponseEntity {
    private String type;
    private String message;
}
