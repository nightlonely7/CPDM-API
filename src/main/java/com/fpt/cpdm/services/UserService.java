package com.fpt.cpdm.services;

import com.fpt.cpdm.models.User;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService, CRUDService<User, Long> {

}
