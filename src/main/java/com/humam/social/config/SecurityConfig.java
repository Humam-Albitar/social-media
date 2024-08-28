package com.humam.social.config;

import com.humam.social.security.JwtAuthenticationEntryPoint;
import com.humam.social.security.JwtAuthenticationFilter;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.OncePerRequestFilter;

@Configuration
@EnableMethodSecurity
@SecurityScheme(
        name = "Bear Authentication",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "bearer"
)
@SecurityScheme(
        name = "X-API-KEY",
        type = SecuritySchemeType.APIKEY,
        in = SecuritySchemeIn.HEADER,
        paramName = "X-API-KEY"
)
public class SecurityConfig {

    private UserDetailsService userDetailsService;

    private JwtAuthenticationEntryPoint authenticationEntryPoint;

    private JwtAuthenticationFilter authenticationFilter;

    private  ApiKeyFilter apiKeyFilter;

    public SecurityConfig(UserDetailsService userDetailsService,
                          JwtAuthenticationEntryPoint authenticationEntryPoint,
                          JwtAuthenticationFilter authenticationFilter, ApiKeyFilter apiKeyFilter){
        this.userDetailsService = userDetailsService;
        this.authenticationEntryPoint = authenticationEntryPoint;
        this.authenticationFilter = authenticationFilter;
        this.apiKeyFilter = apiKeyFilter;
    }

    @Bean
    public static PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.csrf(csrf -> csrf.disable())
                .authorizeHttpRequests((authorize) ->
                        //authorize.anyRequest().authenticated()
                        authorize
//                                .requestMatchers(HttpMethod.GET, "/api/**").permitAll()
                                .requestMatchers("/api/v1/auth/**").permitAll()
                                .requestMatchers("/swagger-ui/**").permitAll()
                                .requestMatchers("/v3/api-docs/**").permitAll()
                                .anyRequest().authenticated()

                ).exceptionHandling( exception -> exception
                        .authenticationEntryPoint(authenticationEntryPoint)
                ).sessionManagement( session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                );
        http.addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

}
