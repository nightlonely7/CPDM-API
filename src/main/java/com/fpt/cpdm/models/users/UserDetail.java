package com.fpt.cpdm.models.users;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fpt.cpdm.models.NameIdOnly;

import java.time.LocalDate;
import java.time.LocalDateTime;

public interface UserDetail {

    Integer getId();

    String getDisplayName();

    String getFullName();

    String getEmail();

    Boolean getGender();

    LocalDate getBirthday();

    String getPhone();

    String getAddress();

    NameIdOnly getRole();

    NameIdOnly getDepartment();

    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    LocalDateTime getCreatedTime();
}
