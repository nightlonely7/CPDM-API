package com.fpt.cpdm.models.users;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fpt.cpdm.models.NameDisplayNameOnly;
import com.fpt.cpdm.models.NameOnly;

import java.time.LocalDateTime;

public interface UserSummary {

    Integer getId();

    String getDisplayName();

    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    LocalDateTime getCreatedTime();

    String getFullName();

    Boolean getGender();

    String getEmail();

    NameOnly getDepartment();

    NameDisplayNameOnly getRole();

}
