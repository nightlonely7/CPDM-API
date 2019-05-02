package com.fpt.cpdm.models.tasks.histories;

import com.fpt.cpdm.models.tasks.TaskSummary;
import com.fpt.cpdm.models.users.UserDisplayName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TaskHistoryResponse {

    private Integer id;
    private TaskSummary task;
    private UserDisplayName creator;
    private LocalDateTime createdTime;
    private TaskHistoryDataResponse data;

}
