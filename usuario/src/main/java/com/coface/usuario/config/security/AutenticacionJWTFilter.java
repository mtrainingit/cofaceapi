package com.coface.usuario.config.security;

import com.coface.usuario.utils.JWTUtility;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class AutenticacionJWTFilter extends OncePerRequestFilter {

    private final JWTUtility jwtUtility;

    public AutenticacionJWTFilter(JWTUtility jwtUtility) {
        this.jwtUtility = jwtUtility;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            String subject = null;
            if (jwtUtility.validarToken(token)) {
                subject = jwtUtility.getSubject(token);
            }
            if (subject != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                User user = new User(
                        jwtUtility.getSubject(token),
                        "none",
                        Stream.of(jwtUtility.getClaim(token, "scopes"))
                                .map(i -> new SimpleGrantedAuthority(i.toString()))
                                .collect(Collectors.toList())
                );
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                        user,
                        null,
                        user.getAuthorities()
                );
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        }
        filterChain.doFilter(request, response);
    }
}
