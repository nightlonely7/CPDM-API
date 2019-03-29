package com.fpt.cpdm.models.projects;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Project {

    @EqualsAndHashCode.Include
    private Integer id;

    private String name;

    private String alias;
}
