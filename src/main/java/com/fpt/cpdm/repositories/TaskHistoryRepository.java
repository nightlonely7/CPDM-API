package com.fpt.cpdm.repositories;

import com.fpt.cpdm.entities.TaskHistoryEntity;
import com.fpt.cpdm.models.tasks.histories.TaskHistorySummary;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskHistoryRepository extends JpaRepository<TaskHistoryEntity, Integer> {
    List<TaskHistorySummary> findAllSummaryByTask_Id(Integer taskId);
}
