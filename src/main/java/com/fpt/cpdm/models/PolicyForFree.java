package com.fpt.cpdm.models;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class PolicyForFree {
    private Integer numberOfDayOffFreeCheck;
    private LocalDate createdDate;
    private LocalDate validFromDate;
}
