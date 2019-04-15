package com.fpt.cpdm.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;

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
    @Column(name = "available", nullable = false)
    private Boolean available;

    @PrePersist
    public void prePersist() {
        this.available = Boolean.TRUE;
    }
}
