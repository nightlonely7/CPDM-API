package com.fpt.cpdm.entities;

import com.fpt.cpdm.utils.Enum;
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

    @Basic
    @Column(name = "parent_comment_id")
    private Integer parentCommentId;

    @PrePersist
    public void onCreated(){
        this.setCreatedDate(LocalDateTime.now());
        this.setLastModifiedDate(LocalDateTime.now());
        this.setStatus(Enum.CommentStatus.New.getCommentStatusCode());
    }

    @PreUpdate
    public void onUpdate(){
        this.setLastModifiedDate(LocalDateTime.now());
    }
}
