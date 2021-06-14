package com.example.springsecuritydemo.jwt;

import com.google.common.base.Strings;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class JwtTokenVerifier extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String authorization = request.getHeader("Authorization");

        if (Strings.isNullOrEmpty(authorization) || !authorization.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authorization.replace("Bearer ", "");
        try {

            String key = "secure".repeat(11);

            Jws<Claims> claimsJws = Jwts.parserBuilder()
                    .setSigningKey(Keys.hmacShaKeyFor(key.getBytes(StandardCharsets.UTF_8)))
                    .build()
                    .parseClaimsJws(token);

            Claims body = claimsJws.getBody();
            String username = body.getSubject();

            List<Map<String, String>> authorities = (List<Map<String, String>>) body.get("authorities");

            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    username,
                    null,
                    authorities
                            .stream()
                            .map(map -> new SimpleGrantedAuthority(map.get("authority")))
                            .collect(Collectors.toList())
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);

            filterChain.doFilter(request, response);

        } catch (JwtException e) {
            throw new IllegalStateException(String.format("Token '%s' cannot be trusted", token), e);
        }
    }
}
