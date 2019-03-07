package com.fpt.cpdm.services.impl;

import com.fpt.cpdm.models.UserToken;
import com.fpt.cpdm.models.users.UserDisplayName;
import com.fpt.cpdm.repositories.UserRepository;
import com.fpt.cpdm.services.TokenAuthenticationService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TokenAuthenticationServiceImpl implements TokenAuthenticationService {

    private static final Long EXPIRATION_TIME = 864_000_000L; // 10 days
    private static final String SECRET = "CPDM";
    private static final String TOKEN_PREFIX = "Bearer";
    private static final String HEADER_STRING = "Authorization";

    private final UserRepository userRepository;

    @Autowired
    public TokenAuthenticationServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Override
    public UserToken getToken(Authentication auth) {
        List<String> authorities = auth.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority).collect(Collectors.toList());
        String tokenStr = Jwts.builder()
                .setSubject(auth.getName())
                .claim("authorities", authorities)
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS512, SECRET)
                .compact();
        UserToken userToken = new UserToken();
        userToken.setToken(tokenStr);
        return userToken;
    }

    @Override
    public Authentication getAuthentication(HttpServletRequest request) {
        String token = request.getHeader(HEADER_STRING);
        if (token != null) {
            // parse the token.
            Claims claims;
            try {
                claims = Jwts.parser()
                        .setSigningKey(SECRET)
                        .parseClaimsJws(token.replace(TOKEN_PREFIX, ""))
                        .getBody();
            } catch (Exception e) {
                return null;
            }
            String username = claims.getSubject();
            List<String> authorities = (List<String>) claims.get("authorities");
            System.out.println(username);
            System.out.println(authorities);
            if (username != null) {
                return new UsernamePasswordAuthenticationToken(username, null,
                        authorities.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList()));
            }
        }
        return null;
    }
}
