package com.fpt.cpdm.services;

import com.fpt.cpdm.models.users.User;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService, CRUDService<User> {

    User findByEmail(String email);
}
