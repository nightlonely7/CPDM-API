package com.fpt.cpdm.repositories;

import com.fpt.cpdm.entities.TaskFilesEntity;
import com.fpt.cpdm.models.tasks.task_files.TaskFilesSummary;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskFilesRepository extends JpaRepository<TaskFilesEntity, Integer> {

    List<TaskFilesSummary> findAllSummaryByTask_Id(Integer id);
}
