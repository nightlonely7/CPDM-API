package com.fpt.cpdm.services;

import com.fpt.cpdm.forms.tasks.issues.TaskIssueForm;
import com.fpt.cpdm.models.tasks.task_issues.TaskIssueDetail;

import java.util.List;

public interface TaskIssueService {

    List<TaskIssueDetail> readAll(Integer taskId);

    TaskIssueDetail create(Integer taskId, TaskIssueForm taskIssueForm);

    TaskIssueDetail update(Integer id, TaskIssueForm taskIssueForm);

    void delete(Integer id);
}
