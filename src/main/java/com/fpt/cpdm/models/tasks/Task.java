package com.fpt.cpdm.models.tasks;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fpt.cpdm.models.documents.Document;
import com.fpt.cpdm.models.users.User;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Task {

    @EqualsAndHashCode.Include
    private Integer id;

    @NotNull
    @Size(min = 4, max = 50)
    private String title;

    private String description;

    @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime createdTime;

    @NotNull
    @FutureOrPresent
    @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime startTime;

    @NotNull
    @FutureOrPresent
    @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime endTime;

    @NotNull
    private Integer priority;

    @NotNull
    private String status;

    private Task parentTask;

    @NotNull
    private User creator;

    @NotNull
    private User executor;

    private List<Document> documents;
}
