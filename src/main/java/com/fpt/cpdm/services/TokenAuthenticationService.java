package com.fpt.cpdm.services;

import org.springframework.security.core.Authentication;

import javax.servlet.http.HttpServletRequest;

public interface TokenAuthenticationService {

    String getToken(String username);
    Authentication getAuthentication(HttpServletRequest request);
}
