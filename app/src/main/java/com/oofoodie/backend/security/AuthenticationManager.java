package com.oofoodie.backend.security;

import com.oofoodie.backend.handler.TokenProvider;
import com.oofoodie.backend.repository.UserRepository;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

import static com.oofoodie.backend.constants.Security.AUTHORITIES_KEY;

@Component
public class AuthenticationManager implements ReactiveAuthenticationManager {

    @Autowired
    private TokenProvider tokenProvider;

    @Autowired
    private UserRepository userRepository;

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        String authToken = authentication.getCredentials().toString();
        String username;
        try {
            username = tokenProvider.getUsernameFromToken(authToken);
        } catch (Exception e) {
            username = null;
        }
        if (username != null && ! tokenProvider.isTokenExpired(authToken)) {
            Claims claims = tokenProvider.getAllClaimsFromToken(authToken);
            String finalUsername = username;
            return userRepository.findByUsername(username)
                    .flatMap(user -> {
                        if (user == null) return Mono.empty();
                        List<String> roles = claims.get(AUTHORITIES_KEY, List.class);
                        List<SimpleGrantedAuthority> authorities = roles.stream().map(role -> new SimpleGrantedAuthority(role)).collect(Collectors.toList());
                        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(finalUsername, finalUsername, authorities);
                        SecurityContextHolder.getContext().setAuthentication(new AuthenticatedUser(finalUsername, authorities));
                        return Mono.just(auth);
                    });
        } else {
            return Mono.empty();
        }
    }
}
