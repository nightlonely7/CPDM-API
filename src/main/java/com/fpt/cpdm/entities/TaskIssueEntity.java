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
    @Column(name = "detail")
    private String detail;

    @Basic
    @Column(name = "weight")
    private Integer weight;

    @Basic
    @Column(name = "status")
    private String status;

    @Basic
    @Column(name = "created_time")
    private LocalDateTime createdTime;

    @Basic
    @Column(name = "available")
    private Boolean available;

    @PrePersist
    private void onCreate() {
        this.createdTime = LocalDateTime.now();
        this.available = Boolean.TRUE;
    }
}
