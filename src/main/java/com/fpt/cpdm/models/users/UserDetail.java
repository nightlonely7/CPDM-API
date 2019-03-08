package com.fpt.cpdm.models.users;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fpt.cpdm.models.NameOnly;

import java.time.LocalDate;
import java.time.LocalDateTime;

public interface UserDetail {

    Integer getId();

    String getDisplayName();

    String getFullName();

    String getEmail();

    String getGender();

    LocalDate getBirthday();

    String getPhone();

    String getAddress();

    NameOnly getRole();

    NameOnly getDepartment();

    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    LocalDateTime getCreatedTime();
}
