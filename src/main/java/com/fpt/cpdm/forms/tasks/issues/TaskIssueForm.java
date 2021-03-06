package com.fpt.cpdm.forms.tasks.issues;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class TaskIssueForm {

    @NotNull
    @Size(min = 4, max = 50)
    private String summary;

    private String description;
}
