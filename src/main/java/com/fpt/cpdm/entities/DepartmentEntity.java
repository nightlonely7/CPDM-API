package com.fpt.cpdm.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity(name = "Department")
@Table(name = "department")
@Data
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@ToString(callSuper = true)
public class DepartmentEntity extends BaseEntity {

    @Basic
    @Column(name = "name", nullable = false, length = 30)
    private String name;

    @Basic
    @Column(name = "alias", nullable = false, length = 30)
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
