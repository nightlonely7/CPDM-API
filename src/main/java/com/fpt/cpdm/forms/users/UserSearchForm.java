package com.fpt.cpdm.forms.users;

import com.fpt.cpdm.models.departments.Department;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class UserSearchForm {

    private String email;

    private String displayName;

    private String fullName;

    private Department department;

    private LocalDate birthdayFrom;

    private LocalDate birthdayTo;

    private Boolean gender;

}
