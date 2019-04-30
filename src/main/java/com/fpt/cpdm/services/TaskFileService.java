package com.fpt.cpdm.services;

import com.fpt.cpdm.forms.tasks.files.TaskFileForm;
import com.fpt.cpdm.models.tasks.task_files.TaskFileDetail;

import java.util.List;

public interface TaskFileService {

    TaskFileDetail create(Integer taskId, TaskFileForm taskFileForm);

    List<TaskFileDetail> findSummaryByTask_Id(Integer id);
}
