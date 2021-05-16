package com.okm1208.vacation.common.entity;

import com.okm1208.vacation.common.entity.convert.VacationTypeConverter;
import com.okm1208.vacation.common.entity.pk.VacationHistoryPk;
import com.okm1208.vacation.common.enums.VacationType;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @author Nick (okm1208@gmail.com)
 * @created 2021-05-14
 */

@Entity
@Table(name = "account_vacation_history")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VacationHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long historyNo;

    @Column
    private Long accountNo;

    @Column(length = 20, nullable = false)
    @Convert(converter = VacationTypeConverter.class)
    private VacationType vacationType;

    @Column(nullable = false)
    private LocalDate regDt;

    @Column(length = 200, nullable = true)
    private String comment;

    @ManyToOne
    @JoinColumn(name = "accountNo", insertable = false , updatable = false)
    private VacationInfo vacationInfo;

}
