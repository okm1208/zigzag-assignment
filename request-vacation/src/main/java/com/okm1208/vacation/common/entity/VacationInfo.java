package com.okm1208.vacation.common.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Nick (okm1208@gmail.com)
 * @created 2021-05-14
 */
@Table(name = "account_vacation_info")
@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VacationInfo {
    @Id
    Long accountNo;

    @MapsId
    @OneToOne
    private Account account;

    @Column(precision = 4, scale = 2,  nullable = false)
    private BigDecimal occursDays;

    @Column(precision = 4, scale = 2 , nullable = false)
    private BigDecimal useDays;

    @Column(precision = 4, scale = 2 , nullable = false)
    private BigDecimal remainingDays;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name ="accountNo")
    private List<VacationHistory> vacationHistoryList = new ArrayList<>();

}
