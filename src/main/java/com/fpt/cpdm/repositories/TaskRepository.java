package com.fpt.cpdm.repositories;

import com.fpt.cpdm.entities.TaskEntity;
import com.fpt.cpdm.entities.UserEntity;
import com.fpt.cpdm.models.tasks.TaskSummary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<TaskEntity, Integer> {

    Page<TaskSummary> findAllSummaryBy(Pageable pageable);

    TaskSummary findSummaryById(Integer id);

    Page<TaskSummary> findAllSummaryByExecutor(UserEntity userEntity, Pageable pageable);

    Page<TaskSummary> findAllSummaryByCreator(UserEntity userEntity, Pageable pageable);

    Page<TaskSummary> findAllSummaryByExecutorAndTitleContaining(UserEntity userEntity, String title, Pageable pageable);

    Page<TaskSummary> findAllSummaryByCreatorAndTitleContaining(UserEntity userEntity, String title, Pageable pageable);

}
