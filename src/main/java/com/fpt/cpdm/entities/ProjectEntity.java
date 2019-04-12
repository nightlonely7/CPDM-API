package com.fpt.cpdm.entities;

import lombok.Data;

import javax.persistence.*;

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
}
