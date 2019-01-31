package com.fpt.cpdm.models.documents;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Document {

    @EqualsAndHashCode.Include
    private Integer id;

    private String title;

    private String summary;
}
