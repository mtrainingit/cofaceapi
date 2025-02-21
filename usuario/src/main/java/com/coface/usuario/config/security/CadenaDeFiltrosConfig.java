package com.coface.usuario.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class CadenaDeFiltrosConfig {

    private final AuthenticationProvider authenticationProvider;

    public CadenaDeFiltrosConfig(AuthenticationProvider authenticationProvider) {
        this.authenticationProvider = authenticationProvider;
    }

    @Bean
    public SecurityFilterChain cadenaDeFiltros(
            HttpSecurity http,
            AutenticacionJWTFilter autenticacionJWTFilter,
            AuthenticationEntryPoint authenticationEntryPoint
    ) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults())
                .sessionManagement(
                        customizer ->
                                customizer.sessionCreationPolicy(
                                        SessionCreationPolicy.STATELESS
                                )
                )
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(autenticacionJWTFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(
                        authorize ->
                                authorize
                                        .requestMatchers(
                                                HttpMethod.POST,
                                                "/api/v1/usuario",
                                                "/api/v1/autenticacion/login"
                                        )
                                        .permitAll()
                                        .requestMatchers(
                                                HttpMethod.GET,
                                                "/api/v1/usuario",
                                                "/api/v1/usuario/{id}",
                                                "/api/usuario/paginado",
                                                "/api/v1/usuario/reducido"
                                        )
                                        .permitAll()
                                        .anyRequest()
                                        .authenticated()
                )
                .exceptionHandling(
                        customizer ->
                                customizer.authenticationEntryPoint(
                                        authenticationEntryPoint
                                )
                )
                .build();
    }
}
