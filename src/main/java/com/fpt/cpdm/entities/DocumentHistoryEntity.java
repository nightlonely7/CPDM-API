package com.fpt.cpdm.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity(name = "DocumentHistory")
@Table(name = "document_history")
@Data
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@ToString(callSuper = true)
public class DocumentHistoryEntity extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "document_id", referencedColumnName = "id")
    private DocumentEntity document;

    @Basic
    @Lob
    @Column(name = "data")
    private String data;

    @ManyToOne
    @JoinColumn(name = "creator_id", referencedColumnName = "id")
    private UserEntity creator;

    @Basic
    @Column(name = "created_time")
    private LocalDateTime createdTime;
}
