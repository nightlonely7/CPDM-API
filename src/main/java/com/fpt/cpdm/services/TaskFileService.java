package com.fpt.cpdm.services;

import com.fpt.cpdm.forms.tasks.files.TaskFileCreateForm;
import com.fpt.cpdm.forms.tasks.files.TaskFileUpdateForm;
import com.fpt.cpdm.models.tasks.task_files.TaskFileDetail;

import java.util.List;

public interface TaskFileService {

    TaskFileDetail create(Integer taskId, TaskFileCreateForm taskFileCreateForm);

    TaskFileDetail update(Integer id, TaskFileUpdateForm taskFileUpdateForm);

    void delete(Integer id);

    List<TaskFileDetail> findAllSummaryByTask_Id(Integer id);
}
