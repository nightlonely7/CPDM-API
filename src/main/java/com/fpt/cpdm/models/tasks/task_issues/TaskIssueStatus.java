package com.fpt.cpdm.models.tasks.task_issues;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TaskIssueStatus {
    private Integer total;
    private Integer completed;
}
