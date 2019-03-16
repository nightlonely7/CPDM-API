package com.fpt.cpdm.models.users;

import com.fpt.cpdm.models.NameOnly;

public interface UserForSelect {

    Integer getId();

    String getEmail();

    String getFullName();

    NameOnly getDepartment();

    NameOnly getRole();

}
