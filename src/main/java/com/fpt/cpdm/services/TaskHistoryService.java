package com.fpt.cpdm.services;

import com.fpt.cpdm.entities.TaskEntity;

public interface TaskHistoryService {
    void save(TaskEntity taskEntity);
    void findById(Integer id);
}
