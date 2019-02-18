package com.fpt.cpdm.controllers;

import com.fpt.cpdm.models.users.Credential;
import com.fpt.cpdm.services.TokenAuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class LoginController {

    private final AuthenticationManager authenticationManager;

    @Autowired
    public LoginController(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody Credential credential) {
        UsernamePasswordAuthenticationToken authReq
                = new UsernamePasswordAuthenticationToken(credential.getEmail(), credential.getPassword());
        Authentication auth = authenticationManager.authenticate(authReq);
        if (auth != null) {
            return ResponseEntity.ok(TokenAuthenticationService.getToken(auth.getName()));
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }
}
