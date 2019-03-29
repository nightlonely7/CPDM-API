package com.fpt.cpdm.models.users;


import com.fpt.cpdm.models.NameOnly;

public interface UserBasic {

    Integer getId();

    String getDisplayName();

    NameOnly getRole();

    String getEmail();
}
