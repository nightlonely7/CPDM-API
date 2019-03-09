package com.fpt.cpdm.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;

@Entity(name = "TaskFiles")
@Table(name = "task_files")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class TaskFilesEntity extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "task_id")
    private TaskEntity task;

    @Basic
    @Column(name = "filename")
    private String filename;
}
