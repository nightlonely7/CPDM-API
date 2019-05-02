package com.fpt.cpdm.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity(name = "TaskFile")
@Table(name = "task_files")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class TaskFileEntity extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "task_id")
    private TaskEntity task;

    @ManyToOne
    @JoinColumn(name = "creator_id")
    private UserEntity creator;

    @ManyToOne
    @JoinColumn(name = "last_editor_id")
    private UserEntity lastEditor;

    @Basic
    @Column(name = "filename")
    private String filename;

    @Basic
    @Column(name = "detail_filename")
    private String detailFilename;

    @Basic
    @Column(name = "extension")
    private String extension;

    @Basic
    @Lob
    @Column(name = "description")
    private String description;

    @Basic
    @Column(name = "created_time")
    private LocalDateTime createdTime;

    @Basic
    @Column(name = "last_modified_time")
    private LocalDateTime lastModifiedTime;

    @Basic
    @Column(name = "available")
    private Boolean available;

    @PrePersist
    public void prePersist() {
        this.available = Boolean.TRUE;
    }

}
