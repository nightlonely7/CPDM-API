package com.fpt.cpdm.services;

import com.fpt.cpdm.models.Token;
import org.springframework.security.core.Authentication;

import javax.servlet.http.HttpServletRequest;

public interface TokenAuthenticationService {

    Token getToken(String username);
    Authentication getAuthentication(HttpServletRequest request);
}
