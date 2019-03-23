package com.fpt.cpdm.forms.documents;

import com.fpt.cpdm.models.IdOnlyForm;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class DocumentCreateForm {

    @NotNull
    @Size(min = 4, max = 30)
    private String title;

    private String summary;

    @NotNull
    private IdOnlyForm project;
}
