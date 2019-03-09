package com.fpt.cpdm.models.users;

import com.fpt.cpdm.models.RoleName;
import com.fpt.cpdm.models.departments.DepartmentDTO;

import java.time.LocalDate;

public interface UserDisplayName {

    Integer getId();

    String getDisplayName();

    String getFullName();

    Boolean getGender();

    String getPassword();

    String getEmail();

    String getPhone();

    String getAddress();

    LocalDate getBirthday();

    RoleName getRole();

    DepartmentDTO getDepartment();

    Boolean getIsEnabled();

}
