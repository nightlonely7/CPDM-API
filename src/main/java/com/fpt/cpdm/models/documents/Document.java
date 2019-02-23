package com.fpt.cpdm.models.documents;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Document {

    @EqualsAndHashCode.Include
    private Integer id;

    private String title;

    private String nameCompany;

    private LocalDateTime dayArrived;

    private String summary;

    private String link;
}
