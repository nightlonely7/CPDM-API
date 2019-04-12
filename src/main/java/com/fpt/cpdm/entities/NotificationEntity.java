package com.fpt.cpdm.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity(name = "notification")
@Table(name = "notification")
@Data
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@ToString(callSuper = true)

public class NotificationEntity extends BaseEntity{
    private String title;
    private String detail;
    private UserEntity user;
}
