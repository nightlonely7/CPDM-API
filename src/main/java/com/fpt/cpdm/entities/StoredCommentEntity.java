package com.fpt.cpdm.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity(name = "StoredComment")
@Table(name = "stored_comment")
@Data
@EqualsAndHashCode(callSuper = true,onlyExplicitlyIncluded = true)
@ToString(callSuper = true)
public class StoredCommentEntity extends BaseEntity{
    @Basic
    @Column(name = "content")
    private String content;

    @Basic
    @Column(name = "created_date")
    private LocalDateTime createdDate;

    @ManyToOne
    @JoinColumn(name = "comment_id", referencedColumnName = "id")
    private CommentEntity comment;

    @PrePersist
    public void onCreated(){
        this.setCreatedDate(LocalDateTime.now());
    }
}
