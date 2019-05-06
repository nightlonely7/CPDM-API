package com.fpt.cpdm.repositories;

import com.fpt.cpdm.entities.DocumentEntity;
import com.fpt.cpdm.entities.TaskEntity;
import com.fpt.cpdm.entities.UserEntity;
import com.fpt.cpdm.models.documents.DocumentDetail;
import com.fpt.cpdm.models.documents.DocumentSummary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
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

    @Query("select d from DocumentEntity d where " +
            "(:title is null or d.title like %:title%) and " +
            "(:summary is null or d.summary like %:summary%) and " +
            "(:createdTimeFrom is null or d.createdTime >= :createdTimeFrom) and " +
            "(:createdTimeTo is null or d.createdTime <= :createdTimeTo) and " +
            "(:startTimeFrom is null or d.startTime >= :startTimeFrom) and " +
            "(:startTimeTo is null or d.startTime <= :startTimeTo) and " +
            "(:endTimeFrom is null or d.endTime >= :endTimeFrom) and " +
            "(:endTimeTo is null or d.endTime <= :endTimeTo) and " +
            "(:projectId is null or d.project.id = :projectId) and " +
            "(d.available = true)")
    Page<DocumentSummary> advanceSearch(@Param("title") String title,
                                         @Param("summary") String summary,
                                         @Param("createdTimeFrom") LocalDateTime createdTimeFrom,
                                         @Param("createdTimeTo") LocalDateTime createdTimeTo,
                                         @Param("startTimeFrom") LocalDateTime startTimeFrom,
                                         @Param("startTimeTo") LocalDateTime startTimeTo,
                                         @Param("endTimeFrom") LocalDateTime endTimeFrom,
                                         @Param("endTimeTo") LocalDateTime endTimeTo,
                                         @Param("projectId") Integer projectId,
                                         Pageable pageable);
}
