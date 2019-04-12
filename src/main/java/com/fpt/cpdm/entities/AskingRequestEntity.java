package com.fpt.cpdm.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity(name = "askingRequest")
@Table(name = "asking_request")
@Data
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@ToString(callSuper = true)
public class AskingRequestEntity extends BaseEntity {

    @Basic
    @Column(name = "content")
    private String content;

    @Basic
    @Column(name = "response")
    private String response;

    @Basic
    @Column(name = "created_date")
    private LocalDate createdDate;

    @Basic
    @Column(name = "status")
    private Integer status;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "receiver_id", referencedColumnName = "id")
    private UserEntity receiver;

    @ManyToMany
    @JoinTable(
            name = "askingRequests_tasks",
            joinColumns = @JoinColumn(name = "asking_request_id",referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "task_id",referencedColumnName = "id"))
    private List<TaskEntity> tasks;

    @PrePersist
    public void onCreated(){
        this.setCreatedDate(LocalDate.now());
    }
}
