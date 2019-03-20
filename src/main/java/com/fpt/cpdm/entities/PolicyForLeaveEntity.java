package com.fpt.cpdm.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDate;

@Entity(name = "policyForLeave")
@Table(name = "policy_for_leave")
@Data
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@ToString(callSuper = true)
public class PolicyForLeaveEntity extends BaseEntity{

    @Basic
    @Column(name = "number_of_day_free_check")
    private Integer numberOfDayFreeCheck;

    @Basic
    @Column(name = "created_date")
    private LocalDate createdDate;

    @Basic
    @Column(name = "valid_from_date")
    private LocalDate validFromDate;
}
