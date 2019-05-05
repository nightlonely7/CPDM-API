package com.fpt.cpdm.forms.tasks;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fpt.cpdm.models.IdOnlyForm;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class TaskCreateForm {

    @NotNull
    @Size(min = 4, max = 50)
    private String title;

    private String summary;

    private String description;

    @FutureOrPresent
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime startTime;

    @NotNull
    @FutureOrPresent
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime endTime;

    @NotNull
    private Integer priority;

    @NotNull
    private IdOnlyForm executor;

    @NotNull
    private IdOnlyForm project;

    private IdOnlyForm parentTask;

    private List<IdOnlyForm> relatives;

}
