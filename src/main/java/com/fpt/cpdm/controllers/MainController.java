package com.fpt.cpdm.controllers;

import com.fpt.cpdm.models.UserToken;
import com.fpt.cpdm.models.users.Credential;
import com.fpt.cpdm.models.users.User;
import com.fpt.cpdm.models.users.UserBasic;
import com.fpt.cpdm.models.users.UserDetail;
import com.fpt.cpdm.services.TokenAuthenticationService;
import com.fpt.cpdm.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.security.Principal;

@Controller
public class MainController {

    private final AuthenticationManager authenticationManager;
    private final TokenAuthenticationService tokenAuthenticationService;
    private final UserService userService;

    @Autowired
    public MainController(AuthenticationManager authenticationManager,
                          TokenAuthenticationService tokenAuthenticationService, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.tokenAuthenticationService = tokenAuthenticationService;
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<UserToken> login(@RequestBody Credential credential) {
        UsernamePasswordAuthenticationToken authReq
                = new UsernamePasswordAuthenticationToken(credential.getEmail(), credential.getPassword());
        Authentication auth = authenticationManager.authenticate(authReq);
        if (auth != null) {
            UserToken userToken = tokenAuthenticationService.getToken(auth);
            return ResponseEntity.ok(userToken);
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @GetMapping("/self")
    public ResponseEntity<UserBasic> self(Principal principal) {

        UserBasic userBasic = userService.findBasicByEmail(principal.getName());

        return ResponseEntity.ok(userBasic);
    }

    @GetMapping("/self/full")
    public ResponseEntity<UserDetail> selfFull(Principal principal) {

        UserDetail userDetail = userService.findDetailByEmail(principal.getName());

        return ResponseEntity.ok(userDetail);
    }


}
