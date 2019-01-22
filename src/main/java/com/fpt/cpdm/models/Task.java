package com.fpt.cpdm.models;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Task {

    @EqualsAndHashCode.Include
    private Integer id;

    @NotNull
    private String title;

    @NotNull
    private String summary;
}
