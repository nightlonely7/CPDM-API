package com.fpt.cpdm.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity(name = "assignRequest")
@Table(name = "assign_request")
@Data
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@ToString(callSuper = true)
public class AssignRequestEntity extends BaseEntity {

    @Basic
    @Column(name = "content")
    private String content;

    @Basic
    @Column(name = "from_date")
    private LocalDate fromDate;

    @Basic
    @Column(name = "to_date")
    private LocalDate toDate;

    @Basic
    @Column(name = "created_date")
    private LocalDate createdDate;

    @Basic
    @Column(name = "status")
    private Integer status;

    @Basic
    @Column(name = "started")
    private Boolean started;

    @Basic
    @Column(name = "finished")
    private Boolean finished;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "assignee_id", referencedColumnName = "id")
    private UserEntity assignee;

    @ManyToOne
    @JoinColumn(name = "approver_id", referencedColumnName = "id")
    private UserEntity approver;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "assignRequests_tasks",
            joinColumns = @JoinColumn(name = "assign_request_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "task_id", referencedColumnName = "id"))
    private List<TaskEntity> tasks;

    @PrePersist
    public void onCreated() {
        this.setCreatedDate(LocalDate.now());
        this.setStarted(false);
        this.setFinished(false);
    }
}
