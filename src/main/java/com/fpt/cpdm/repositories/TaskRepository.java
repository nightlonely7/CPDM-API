package com.fpt.cpdm.repositories;

import com.fpt.cpdm.entities.TaskEntity;
import com.fpt.cpdm.entities.UserEntity;
import com.fpt.cpdm.models.tasks.TaskSummary;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<TaskEntity, Integer> {

    List<TaskSummary> findAllSummaryBy();

    TaskSummary findSummaryById(Integer id);

    List<TaskSummary> findAllSummaryByExecutor(UserEntity userEntity);

    List<TaskSummary> findAllSummaryByCreator(UserEntity userEntity);
}
