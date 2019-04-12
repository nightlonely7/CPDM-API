package com.fpt.cpdm.entities;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity(name = "TaskIssue")
@Table(name = "task_issue")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@ToString(callSuper = true)
public class TaskIssueEntity extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "task_id", referencedColumnName = "id")
    private TaskEntity task;

    @Basic
    @Column(name = "summary")
    private String summary;

    @Basic
    @Column(name = "description")
    private String description;

    @Basic
    @Column(name = "completed")
    private Boolean completed;

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
    private void prePersist() {
        this.createdTime = LocalDateTime.now();
        this.lastModifiedTime = LocalDateTime.now();
        this.available = Boolean.TRUE;
        this.completed = Boolean.FALSE;
    }

    @PreUpdate
    private void preUpdate() {
        this.lastModifiedTime = LocalDateTime.now();
    }
}
