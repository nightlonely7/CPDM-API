package com.fpt.cpdm.models.tasks.histories;

import com.fpt.cpdm.models.IdOnlyForm;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TaskHistoryDataVersion1 {

    private String title;
    private String summary;
    private String description;
    private String createdTime;
    private String startTime;
    private String endTime;
    private String completedTime;
    private Integer priority;
    private String status;
    private Integer projectId;
    private Integer parentTaskId;
    private Integer creatorId;
    private Integer executorId;
    private Boolean available;

}
