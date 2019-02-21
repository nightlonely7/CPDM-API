package com.fpt.cpdm.models.departments;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Department {

    @EqualsAndHashCode.Include
    private Integer id;

    private String name;

    private String alias;

}
