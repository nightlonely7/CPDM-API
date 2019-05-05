package com.fpt.cpdm.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity(name = "DocumentEntity")
@Table(name = "document")
@Data
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@ToString(callSuper = true)
public class DocumentEntity extends BaseEntity {

    @Basic
    @Column(name = "title")
    private String title;

    @Basic
    @Column(name = "summary")
    private String summary;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "description")
    private String description;

    @ManyToOne
    @JoinColumn(name = "project_id", referencedColumnName = "id")
    private ProjectEntity project;

    @ManyToMany(mappedBy = "documents")
    private List<TaskEntity> tasks;

    @Basic
    @Column(name = "created_time")
    @DateTimeFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    private LocalDateTime createdTime;

    @Basic
    @Column(name = "start_time")
    @DateTimeFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    private LocalDateTime startTime;

    @Basic
    @Column(name = "end_time")
    @DateTimeFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    private LocalDateTime endTime;

    @ManyToMany
    @JoinTable(name = "documents_relatives",
            joinColumns = @JoinColumn(name = "document_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"))
    private List<UserEntity> relatives;

    @Basic
    @Column(name = "last_modified_time")
    @DateTimeFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    private LocalDateTime lastModifiedTime;

    @ManyToOne
    @JoinColumn(name = "creator_id", referencedColumnName = "id")
    private UserEntity creator;

    @Basic
    @Column(name = "available")
    private Boolean available;

    @PrePersist
    private void onPersist() {
        this.createdTime = LocalDateTime.now();
        this.lastModifiedTime = LocalDateTime.now();
        this.available = Boolean.TRUE;
    }

    @PreUpdate
    private void onUpdate() {
        this.lastModifiedTime = LocalDateTime.now();
    }

}
