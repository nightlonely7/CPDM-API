package com.fpt.cpdm.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity(name = "Comment")
@Table(name = "comment")
@Data
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@ToString(callSuper = true)
public class CommentEntity extends BaseEntity{

    @Basic
    @Column(name = "content")
    private String content;

    @Basic
    @Column(name = "created_date")
    private LocalDateTime createdDate;

    @Basic
    @Column(name = "last_modified_date")
    private LocalDateTime lastModifiedDate;

    @Basic
    @Column(name = "status")
    private Integer status;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "task_id", referencedColumnName = "id")
    private TaskEntity task;

    @PrePersist
    public void onCreated(){
        this.setCreatedDate(LocalDateTime.now());
        this.setLastModifiedDate(LocalDateTime.now());
    }

    @PreUpdate
    public void onUpdate(){
        this.setLastModifiedDate(LocalDateTime.now());
    }
}
