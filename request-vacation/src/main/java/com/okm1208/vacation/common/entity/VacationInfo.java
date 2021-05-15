package com.okm1208.vacation.common.entity;

import lombok.Builder;
import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author Nick (okm1208@gmail.com)
 * @created 2021-05-14
 */
@Table(name = "account_vacation_info")
@Entity
@Data
@Builder
public class VacationInfo {
    @Id
    Long accountNo;

    @MapsId
    @OneToOne
    @JoinColumn(name = "accountNo")
    private Account account;

    @Column(precision = 4, scale = 2,  nullable = false)
    private BigDecimal occursDays;

    @Column(precision = 4, scale = 2 , nullable = false)
    private BigDecimal useDays;

    @Column(precision = 4, scale = 2 , nullable = false)
    private BigDecimal remainingDays;

    @OneToMany
    @JoinColumn(name ="accountNo", referencedColumnName = "accountNo")
    private List<VacationHistory> vacationHistoryList;

}
