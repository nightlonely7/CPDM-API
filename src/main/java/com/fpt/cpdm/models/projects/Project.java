package com.fpt.cpdm.models.projects;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Project {

    @EqualsAndHashCode.Include
    private Integer id;

    private String name;

    private String alias;

}
