package com.fpt.cpdm.services;

import com.fpt.cpdm.models.UserToken;
import org.springframework.security.core.Authentication;

import javax.servlet.http.HttpServletRequest;

public interface TokenAuthenticationService {

    UserToken getToken(Authentication auth);
    Authentication getAuthentication(HttpServletRequest request);
}
