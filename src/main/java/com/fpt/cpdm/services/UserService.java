package com.fpt.cpdm.services;

import com.fpt.cpdm.models.Role;
import com.fpt.cpdm.models.departments.Department;
import com.fpt.cpdm.models.users.User;
import com.fpt.cpdm.models.users.UserBasic;
import com.fpt.cpdm.models.users.UserDisplayName;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService, CRUDService<User> {

    List<User> findAllByDisplayNameContaining(String displayName);

    User findByEmail(String email);

    UserBasic findBasicByEmail(String email);

    List<UserDisplayName> findDisplayNameByDepartmentAndRole(Department department, Role role);
}
