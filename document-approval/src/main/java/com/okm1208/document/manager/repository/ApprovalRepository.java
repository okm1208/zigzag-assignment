package com.okm1208.document.manager.repository;

import com.okm1208.document.common.entity.Approval;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Nick ( okm1208@gmail.com )
 * @created 2021-05-17
 */
public interface ApprovalRepository extends JpaRepository<Approval, Long> {
}
