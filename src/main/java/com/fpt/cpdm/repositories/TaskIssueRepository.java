package com.fpt.cpdm.repositories;

import com.fpt.cpdm.entities.TaskIssueEntity;
import com.fpt.cpdm.models.tasks.task_issues.TaskIssueDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TaskIssueRepository extends JpaRepository<TaskIssueEntity, Integer> {

    List<TaskIssueDetail> findAllDetailByTask_IdAndAvailableTrue(Integer taskId);

    Optional<TaskIssueDetail> findDetailByIdAndAvailableTrue(Integer id);

    Boolean existsByTask_IdAndCompletedFalseAndAvailableTrue(Integer taskId);

}
