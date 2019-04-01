package com.fpt.cpdm.forms.documents;

import com.fpt.cpdm.models.IdOnlyForm;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class DocumentCreateForm {

    @NotNull
    @Size(min = 4, max = 30)
    private String title;

    private String summary;

    private String description;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private List<IdOnlyForm> relatives;

    @NotNull
    private IdOnlyForm project;
}
