package com.fpt.cpdm.forms.tasks.files;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class TaskFileUpdateForm {

    private MultipartFile file;

    @NotNull
    @Size(min = 4, max = 30)
    private String filename;

    private String description;
}
