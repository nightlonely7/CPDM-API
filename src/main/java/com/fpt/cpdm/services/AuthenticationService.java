package com.fpt.cpdm.services;

import com.fpt.cpdm.entities.UserEntity;

public interface AuthenticationService {
    UserEntity getCurrentLoggedUser();
}
