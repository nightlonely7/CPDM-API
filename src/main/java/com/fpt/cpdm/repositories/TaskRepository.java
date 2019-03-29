package com.fpt.cpdm.repositories;

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

import java.time.LocalDate;
import java.util.List;

public interface TaskRepository extends JpaRepository<TaskEntity, Integer> {

    TaskDetail findDetailById(Integer id);

    Page<TaskSummary> findSummaryByTitleContaining(String title, Pageable pageable);

    Page<TaskSummary> findSummaryByTitleAndSummaryAndDescriptionContaining(String title, String summary,
                                                                           String description, Pageable pageable);

    TaskSummary findSummaryById(Integer id);

    Page<TaskSummary> findAllSummaryByRelatives(UserEntity userEntity, Pageable pageable);

    Page<TaskSummary> findAllSummaryByExecutorAndAvailableTrue(UserEntity userEntity, Pageable pageable);

    Page<TaskSummary> findAllSummaryByCreatorAndTitleContainsAndSummaryContainsAndProject_IdAndAvailableTrue
            (UserEntity userEntity, String title, String description, Integer projectId, Pageable pageable);

    @Query("select t from TaskEntity t where " +
            "(:creator is null or t.creator = :creator) and " +
            "(:executor is null or t.executor = :executor) and " +
            "(:relative is null or :relative member of t.relatives) and " +
            "(:title is null or t.title like %:title%) and " +
            "(:summary is null or t.summary like %:summary%) and " +
            "(:projectId is null or t.project.id = :projectId) and" +
            "(t.available = true)")
    Page<TaskSummary> advanceSearch(
            @Param("creator") UserEntity creator,
            @Param("executor") UserEntity executor,
            @Param("relative") UserEntity relative,
            @Param("title") String title,
            @Param("summary") String summary,
            @Param("projectId") Integer projectId,
            Pageable pageable);

    Boolean existsByCreatorOrExecutorOrRelatives(UserEntity creator, UserEntity executor, UserEntity relative);

    Page<TaskSummary> findAllByParentTask_Id(Integer taskId, Pageable pageable);

    List<TaskBasic> findAllBasicByExecutorAndProject_Id(UserEntity executor, Integer projectId);

    boolean existsByExecutorAndStatus(UserEntity userEntity, String status);

    List<TaskSummary> findAllByExecutorAndStatusAndStartTimeGreaterThanEqualOrExecutorAndStatusAndEndTimeLessThanEqual(UserEntity userEntity, Integer status, LocalDate date, UserEntity userEntity2, Integer status2, LocalDate date2);
}
