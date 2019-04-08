package com.fpt.cpdm.repositories;

import com.fpt.cpdm.entities.DocumentEntity;
import com.fpt.cpdm.entities.TaskEntity;
import com.fpt.cpdm.entities.UserEntity;
import com.fpt.cpdm.models.tasks.TaskBasic;
import com.fpt.cpdm.models.tasks.TaskDetail;
import com.fpt.cpdm.models.tasks.TaskSummary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface TaskRepository extends JpaRepository<TaskEntity, Integer> {

    TaskDetail findDetailById(Integer id);

    Page<TaskSummary> findSummaryByTitleContaining(String title, Pageable pageable);

    Page<TaskSummary> findSummaryByTitleAndSummaryAndDescriptionContaining(String title, String summary,
                                                                           String description, Pageable pageable);

    TaskSummary findSummaryById(Integer id);

    Page<TaskSummary> findAllSummaryByCreatorAndAvailableTrue(UserEntity creator, Pageable pageable);

    Page<TaskSummary> findAllSummaryByExecutorAndAvailableTrue(UserEntity executor, Pageable pageable);

    Page<TaskSummary> findAllSummaryByRelativesAndAvailableTrue(UserEntity relative, Pageable pageable);

    @Query("select t from TaskEntity t where " +
            "(:creator is null or t.creator = :creator) and " +
            "(:executor is null or t.executor = :executor) and " +
            "(:relative is null or :relative member of t.relatives) and " +
            "(:title is null or t.title like %:title%) and " +
            "(:summary is null or t.summary like %:summary%) and " +
            "(:description is null or t.description like %:description%) and " +
            "(:createdTimeFrom is null or t.createdTime >= :createdTimeFrom) and " +
            "(:createdTimeTo is null or t.createdTime <= :createdTimeTo) and " +
            "(:startTimeFrom is null or t.startTime >= :startTimeFrom) and " +
            "(:startTimeTo is null or t.startTime <= :startTimeTo) and " +
            "(:endTimeFrom is null or t.endTime >= :endTimeFrom) and " +
            "(:endTimeTo is null or t.endTime <= :endTimeTo) and " +
            "(:projectId is null or t.project.id = :projectId) and " +
            "((:status) is null or t.status in (:status)) and " +
            "(t.available = true)")
    Page<TaskSummary> advanceSearch(
            @Param("creator") UserEntity creator,
            @Param("executor") UserEntity executor,
            @Param("relative") UserEntity relative,
            @Param("title") String title,
            @Param("summary") String summary,
            @Param("description") String description,
            @Param("createdTimeFrom") LocalDateTime createdTimeFrom,
            @Param("createdTimeTo") LocalDateTime createdTimeTo,
            @Param("startTimeFrom") LocalDateTime startTimeFrom,
            @Param("startTimeTo") LocalDateTime startTimeTo,
            @Param("endTimeFrom") LocalDateTime endTimeFrom,
            @Param("endTimeTo") LocalDateTime endTimeTo,
            @Param("projectId") Integer projectId,
            @Param("status") List<String> status,
            Pageable pageable);

    Boolean existsByCreatorOrExecutorOrRelatives(UserEntity creator, UserEntity executor, UserEntity relative);

    Page<TaskSummary> findAllByParentTask_Id(Integer taskId, Pageable pageable);

    List<TaskSummary> findAllSummaryByDocuments(DocumentEntity documentEntity);

    List<TaskEntity> findAllByDocuments(DocumentEntity documentEntity);

    List<TaskBasic> findAllBasicByExecutorAndProject_Id(UserEntity executor, Integer projectId);

    boolean existsByExecutorAndStatusAndStartTimeIsBetween(UserEntity userEntity, String status, LocalDateTime fromTime, LocalDateTime toTime);

    boolean existsByExecutorAndStatusAndStartTimeIsBeforeAndEndTimeIsAfter(UserEntity userEntity, String status, LocalDateTime fromTime, LocalDateTime fromTime2);

    List<TaskSummary> findAllByExecutorAndStatusAndStartTimeIsBetween(UserEntity userEntity, String status, LocalDateTime fromTime, LocalDateTime toTime);

    List<TaskSummary> findAllByExecutorAndStatusAndStartTimeIsBeforeAndEndTimeIsAfter(UserEntity userEntity, String status, LocalDateTime fromTime, LocalDateTime fromTime2);
}
