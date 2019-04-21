package com.fpt.cpdm.entities;

import com.fasterxml.jackson.databind.ser.Serializers;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static java.time.temporal.ChronoUnit.DAYS;

@Entity(name = "LeaveRequest")
@Table(name = "leave_request")
@Data
@EqualsAndHashCode(callSuper = true,onlyExplicitlyIncluded = true)
@ToString(callSuper = true)
public class LeaveRequestEntity extends BaseEntity {

    @Basic
    @Column(name = "content")
    private String content;

    @Basic
    @Column(name = "day_off")
    private Integer dayOff;

    @Basic
    @Column(name = "from_date")
    private LocalDate fromDate;

    @Basic
    @Column(name = "to_date")
    private LocalDate toDate;

    @Basic
    @Column(name = "created_date")
    private LocalDate createdDate;

    @Basic
    @Column(name = "status")
    private Integer status;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "approver_id", referencedColumnName = "id")
    private UserEntity approver;

    @PrePersist
    public void onCreated(){
        this.setCreatedDate(LocalDate.now());
        this.setDayOff((int)DAYS.between(this.getFromDate(), this.getToDate()) + 1);
    }
}
