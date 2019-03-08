package com.fpt.cpdm.services.impl;

import com.fpt.cpdm.models.taskFiles.TaskFilesSummary;
import com.fpt.cpdm.models.tasks.Task;
import com.fpt.cpdm.repositories.TaskFilesRepository;
import com.fpt.cpdm.services.TaskFilesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskFilesServiceImpl implements TaskFilesService {

    private final TaskFilesRepository taskFilesRepository;

    @Autowired
    public TaskFilesServiceImpl(TaskFilesRepository taskFilesRepository) {
        this.taskFilesRepository = taskFilesRepository;
    }

    public List<TaskFilesSummary> findSummaryByTask_Id(Integer id) {
        return taskFilesRepository.findAllSummaryByTask_Id(id);
    }
}
