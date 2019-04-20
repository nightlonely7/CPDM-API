package com.fpt.cpdm.models.projects;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public interface ProjectDTO {

    Integer getId();

    String getName();

    String getAlias();

}
