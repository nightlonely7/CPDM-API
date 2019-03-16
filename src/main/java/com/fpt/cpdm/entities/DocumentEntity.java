package com.fpt.cpdm.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity(name = "Document")
@Table(name = "document")
@Data
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@ToString(callSuper = true)
public class DocumentEntity extends BaseEntity {

    @Basic
    @Column(name = "title")
    private String title;

    @Basic
    @Column(name = "name_company")
    private String nameCompany;

    @Basic
    @Column(name = "day_arrived")
    private LocalDateTime dayArrived;

    @Basic
    @Column(name = "summary")
    private String summary;

    @Basic
    @Column(name = "link")
    private String link;

}
