package com.fpt.cpdm.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity(name = "Notification")
@Table(name = "notification")
@Data
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@ToString(callSuper = true)

public class NotificationEntity extends BaseEntity {

    @Basic
    @Column(name = "title")
    private String title;

    @Basic
    @Column(name = "detail")
    private String detail;

    @Basic
    @Column(name = "hidden")
    private boolean hidden;

    @Basic
    @Column(name = "read")
    private boolean read;

    @Basic
    @Column(name = "url")
    private String url;

    @Basic
    @Column(name = "created_time")
    private LocalDateTime createdTime;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "creator_id", referencedColumnName = "id")
    private UserEntity creator;

    @PrePersist
    public void onCreated() {
        this.setCreatedTime(LocalDateTime.now());
        this.setHidden(false);
        this.setRead(false);
    }
}
