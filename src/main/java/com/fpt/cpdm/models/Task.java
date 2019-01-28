package com.fpt.cpdm.models;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Task {

    @EqualsAndHashCode.Include
    private Integer id;

    @NotNull
    private String title;


    private String description;

    @NotNull
    private LocalDateTime createdTime;

    @NotNull
    private LocalDateTime startTime;

    @NotNull
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
