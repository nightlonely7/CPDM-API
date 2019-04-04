package com.fpt.cpdm.models.assignRequests;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fpt.cpdm.models.tasks.Task;
import com.fpt.cpdm.models.users.User;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class AssignRequest {
    @EqualsAndHashCode.Include
    private Integer id;

    private String content;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate fromDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate toDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate createdDate;

    private Integer status;

    private User user;

    private User assignee;

    private User approver;

    private Task task;
}
