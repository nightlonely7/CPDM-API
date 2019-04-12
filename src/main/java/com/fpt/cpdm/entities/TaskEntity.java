package com.fpt.cpdm.entities;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Entity(name = "TaskEntity")
@Table(name = "task")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@ToString(callSuper = true)
public class TaskEntity extends BaseEntity {

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

    @Basic
    @Column(name = "completed_time")
    @DateTimeFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    private LocalDateTime completedTime;

    @Basic
    @Column(name = "priority")
    private Integer priority;

    @Basic
    @Column(name = "status")
    private String status;

    @Basic
    @Column(name = "last_modified_time")
    @DateTimeFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    private LocalDateTime lastModifiedTime;

    @ManyToOne
    @JoinColumn(name = "project_id", referencedColumnName = "id")
    private ProjectEntity project;

    @ManyToOne
    @JoinColumn(name = "parent_task_id", referencedColumnName = "id")
    private TaskEntity parentTask;

    @ManyToOne
    @JoinColumn(name = "creator_id", referencedColumnName = "id")
    private UserEntity creator;

    @ManyToOne
    @JoinColumn(name = "executor_id", referencedColumnName = "id")
    private UserEntity executor;

    @ManyToMany
    @JoinTable(name = "tasks_relatives",
            joinColumns = @JoinColumn(name = "task_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"))
    private List<UserEntity> relatives;

    @Basic
    @Column(name = "available")
    private Boolean available;

    @Basic
    @OneToMany(mappedBy = "task")
    private List<TaskIssueEntity> issues;

    @ManyToMany
    @JoinTable(name = "tasks_documents",
            joinColumns = @JoinColumn(name = "task_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "document_id", referencedColumnName = "id"))
    private List<DocumentEntity> documents;

    @PrePersist
    public void onPersist() {
        this.createdTime = LocalDateTime.now();
        this.lastModifiedTime = LocalDateTime.now();
        if (this.createdTime.plus(30, ChronoUnit.SECONDS).isAfter(this.startTime)) {
            this.status = "Working";
        } else {
            this.status = "Waiting";
        }
        this.available = Boolean.TRUE;
    }

    @PreUpdate
    public void onUpdate() {
        this.lastModifiedTime = LocalDateTime.now();
    }
}
