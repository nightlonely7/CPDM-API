package com.fpt.cpdm.services;

import com.fpt.cpdm.models.Role;

public interface RoleService extends CRUDService<Role> {

    Role findByName(String name);
}
