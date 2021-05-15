package com.okm1208.vacation.common.entity.pk;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author Nick (okm1208@gmail.com)
 * @created 2021-05-14
 */
@NoArgsConstructor
@Data
public class VacationHistoryPk implements Serializable {
    Long historyNo;
    Long accountNo;

}
