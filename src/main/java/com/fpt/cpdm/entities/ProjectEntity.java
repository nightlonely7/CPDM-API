package com.fpt.cpdm.entities;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity(name = "Project")
@Table(name = "project")
@Data
public class ProjectEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Basic
    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @Basic
    @Column(name = "alias", nullable = false, length = 50)
    private String alias;

    @Basic
    @Column(name = "description")
    private String description;

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
    public void prePersist() {
        this.createdTime = LocalDateTime.now();
        this.lastModifiedTime = LocalDateTime.now();
        this.available = Boolean.TRUE;
    }

    @PreUpdate
    public void preUpdate() {
        this.lastModifiedTime = LocalDateTime.now();
    }
}
