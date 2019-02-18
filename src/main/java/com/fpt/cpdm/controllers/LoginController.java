package com.fpt.cpdm.controllers;

import com.fpt.cpdm.models.users.Credential;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
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

import java.util.Date;

@Controller
@RequestMapping("/")
public class LoginController {

    private static final Long EXPIRATION_TIME = 864_000_000L; // 10 days
    private static final String SECRET = "CPDM";
    private static final String TOKEN_PREFIX = "Bearer";
    private static final String HEADER_STRING = "Authorization";

    @Autowired
    AuthenticationManager authenticationManager;

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody Credential credential) {
        System.out.println(credential);
        UsernamePasswordAuthenticationToken authReq
                = new UsernamePasswordAuthenticationToken(credential.getEmail(), credential.getPassword());
        Authentication auth = authenticationManager.authenticate(authReq);
        if (auth != null) {
            String JWT = Jwts.builder()
                    .setSubject(auth.getName())
                    .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                    .signWith(SignatureAlgorithm.HS512, SECRET)
                    .compact();
            return ResponseEntity.ok(JWT);
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }
}
