package com.fpt.cpdm.services;

import com.fpt.cpdm.models.tasks.task_files.TaskFileSummary;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface TaskFileService {

    TaskFileSummary create(Integer taskId, MultipartFile file);

    List<TaskFileSummary> findSummaryByTask_Id(Integer id);
}
