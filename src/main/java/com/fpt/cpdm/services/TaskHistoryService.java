package com.fpt.cpdm.services;

import com.fpt.cpdm.entities.TaskEntity;
import com.fpt.cpdm.entities.UserEntity;
import com.fpt.cpdm.models.tasks.histories.TaskHistoryResponse;
import com.fpt.cpdm.models.tasks.histories.TaskHistorySummary;

import java.util.List;

public interface TaskHistoryService {
    void save(TaskEntity taskEntity);
    void save(TaskEntity taskEntity, UserEntity creator);
    List<TaskHistorySummary> findAllSummaryByTask_Id(Integer taskId);
    TaskHistoryResponse findById(Integer id);
}
