package com.fpt.cpdm.repositories;

import com.fpt.cpdm.entities.TaskFileEntity;
import com.fpt.cpdm.models.tasks.task_files.TaskFileSummary;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TaskFilesRepository extends JpaRepository<TaskFileEntity, Integer> {

    Optional<TaskFileSummary> findSummaryById(Integer id);

    List<TaskFileSummary> findAllSummaryByTask_Id(Integer id);
}
