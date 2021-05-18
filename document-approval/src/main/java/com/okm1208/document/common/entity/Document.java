package com.okm1208.document.common.entity;

import com.okm1208.document.common.entity.convert.ApproveStatusTypeConverter;
import com.okm1208.document.common.entity.convert.DocumentTypeConverter;
import com.okm1208.document.common.model.ApproveStatusType;
import com.okm1208.document.common.model.DocumentType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Nick ( okm1208@gmail.com )
 * @created 2021-05-17
 */
@Table(name="document")
@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Document {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long documentNo;

    @Column(length = 200, nullable = false)
    private String title;

    @Column(nullable = false)
    @Convert(converter = DocumentTypeConverter.class)
    private DocumentType type;

    @Column(length = 1000, nullable = false)
    private String content;

    @OneToMany(mappedBy = "document" , cascade = CascadeType.ALL)
    private List<Approval> approvalList = new ArrayList<>();

    @Column(nullable = false)
    @Convert(converter = ApproveStatusTypeConverter.class)
    private ApproveStatusType approveStatus;

    @Column
    private Long accountNo;

    @ManyToOne
    @JoinColumn(name = "accountNo", insertable = false , updatable = false)
    private Account account;
}

