package com.fpt.cpdm.repositories;

import com.fpt.cpdm.entities.DocumentEntity;
import com.fpt.cpdm.entities.UserEntity;
import com.fpt.cpdm.models.documents.DocumentSummary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DocumentRepository extends JpaRepository<DocumentEntity, Integer> {

    Page<DocumentSummary> findAllSummaryBy(Pageable pageable);

    Page<DocumentSummary> findAllSummaryByRelatives(UserEntity relative, Pageable pageable);

    Optional<DocumentSummary> findSummaryById(Integer id);
}
