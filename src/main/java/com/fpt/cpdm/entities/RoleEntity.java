package com.fpt.cpdm.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity(name = "Role")
@Table(name = "role")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class RoleEntity extends BaseEntity {

    @Basic
    @Column(name = "name")
    private String name;

}
