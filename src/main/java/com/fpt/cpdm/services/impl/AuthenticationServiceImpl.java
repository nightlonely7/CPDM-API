package com.fpt.cpdm.services.impl;

import com.fpt.cpdm.entities.UserEntity;
import com.fpt.cpdm.repositories.UserRepository;
import com.fpt.cpdm.services.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;

    @Autowired
    public AuthenticationServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserEntity getCurrentLoggedUser() {

        String currentLoggedEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity currentLoggedUser = userRepository.findByEmail(currentLoggedEmail).orElseThrow(
                () -> new UsernameNotFoundException(currentLoggedEmail)
        );
        return currentLoggedUser;
    }
}
