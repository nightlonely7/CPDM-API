package com.fpt.cpdm.repositories;

import com.fpt.cpdm.entities.DocumentEntity;
import com.fpt.cpdm.entities.TaskEntity;
import com.fpt.cpdm.entities.UserEntity;
import com.fpt.cpdm.models.documents.DocumentDetail;
import com.fpt.cpdm.models.documents.DocumentSummary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DocumentRepository extends JpaRepository<DocumentEntity, Integer> {

    Page<DocumentSummary> findAllSummaryByAndAvailableTrue(Pageable pageable);

    List<DocumentSummary> findAllSummaryByProject_Id(Integer projectId);

    List<DocumentSummary> findAllSummaryByTasks(TaskEntity taskEntity);

    List<DocumentEntity> findAllByTasks(TaskEntity taskEntity);

    Page<DocumentSummary> findAllSummaryByRelatives(UserEntity relative, Pageable pageable);

    List<DocumentSummary> findAllSummaryByProject_IdAndRelatives(Integer projectId, UserEntity relative);

    Optional<DocumentSummary> findSummaryById(Integer id);

    Optional<DocumentDetail> findDetailByIdAndAvailableTrue(Integer id);

    Boolean existsByTitle(String title);

    Boolean existsByIdAndRelativesAndAvailableTrue(Integer id, UserEntity relative);

    Page<DocumentSummary> findAllSummaryByTitleContainingIgnoreCaseAndSummaryContainingIgnoreCaseAndAvailableTrue
            (String title, String summary, Pageable pageable);
}
