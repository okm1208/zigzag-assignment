package com.okm1208.document.manager.repository;

import com.okm1208.document.common.entity.Account;
import com.okm1208.document.common.entity.Approval;
import com.okm1208.document.common.entity.Document;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * @author Nick ( okm1208@gmail.com )
 * @created 2021-05-17
 */
public interface DocumentRepository extends JpaRepository<Document, Long> {
    Optional<List<Document>> findAllByAccount(Account account);
}
