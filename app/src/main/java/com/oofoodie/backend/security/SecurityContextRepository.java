package com.oofoodie.backend.security;

import com.oofoodie.backend.util.SecurityCipher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpCookie;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
public class SecurityContextRepository implements ServerSecurityContextRepository{

    @Value("${authentication.accessTokenCookieName}")
    private String accessTokenCookieName;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Override
    public Mono<Void> save(ServerWebExchange swe, SecurityContext sc) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Mono<SecurityContext> load(ServerWebExchange swe) {
        ServerHttpRequest request = swe.getRequest();
        String authToken = getJwtFromCookie(request);
        if (authToken != null) {
            Authentication auth = new UsernamePasswordAuthenticationToken(authToken, authToken);
            return this.authenticationManager.authenticate(auth).map((authentication) -> new SecurityContextImpl(authentication));
        } else {
            return Mono.empty();
        }
    }

    private String getJwtFromCookie(ServerHttpRequest request) {
        MultiValueMap<String, HttpCookie> cookies = request.getCookies();
        List<HttpCookie> httpCookies = cookies.get(accessTokenCookieName);
        if (cookies.size() > 0) {
            for (HttpCookie cookie : httpCookies) {
                if (cookie.getName().equals(accessTokenCookieName)) {
                    String accessToken = cookie.getValue();
                    if (accessToken == null) return null;

                    return SecurityCipher.decrypt(cookie.getValue());
                }
            }
        }

        return null;
    }

}
