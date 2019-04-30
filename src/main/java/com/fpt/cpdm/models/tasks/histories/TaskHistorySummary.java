package com.fpt.cpdm.models.tasks.histories;

import com.fpt.cpdm.models.tasks.TaskSummary;
import com.fpt.cpdm.models.users.UserDisplayName;

import java.time.LocalDateTime;

public interface TaskHistorySummary {

    Integer getId();
    TaskSummary getTask();
    UserDisplayName getCreator();
    LocalDateTime getCreatedTime();

}
