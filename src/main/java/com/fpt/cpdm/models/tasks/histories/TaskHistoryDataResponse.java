package com.fpt.cpdm.models.tasks.histories;

import com.fpt.cpdm.models.projects.ProjectDTO;
import com.fpt.cpdm.models.tasks.TaskSummary;
import com.fpt.cpdm.models.users.UserDisplayName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TaskHistoryDataResponse {

    private String title;
    private String summary;
    private String description;
    private LocalDateTime createdTime;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private LocalDateTime completedTime;
    private Integer priority;
    private String status;
    private ProjectDTO project;
    private TaskSummary parentTask;
    private UserDisplayName creator;
    private UserDisplayName executor;
    private Boolean available;

}
