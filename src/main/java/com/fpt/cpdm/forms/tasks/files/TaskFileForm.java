package com.fpt.cpdm.forms.tasks.files;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class TaskFileForm {
    private MultipartFile file;
    private String filename;
    private String description;
}
