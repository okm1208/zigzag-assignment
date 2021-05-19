package com.okm1208.document.common.entity;

import com.okm1208.document.common.entity.convert.ApproveConverter;
import com.okm1208.document.common.model.ApproveType;
import lombok.*;

import javax.persistence.*;

/**
 * @author Nick ( okm1208@gmail.com )
 * @created 2021-05-17
 */

@Table(name="approval")
@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false, doNotUseGetters = true, of = {"approvalNo"})
public class Approval {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long approvalNo;

    @ManyToOne
    @JoinColumn(name = "accountNo")
    private Account account;

    @ManyToOne
    @JoinColumn(name = "documentNo")
    private Document document;

    @Column(nullable = false)
    private Long orderNo;

    @Column
    @Convert(converter = ApproveConverter.class)
    private ApproveType approveType;
}
