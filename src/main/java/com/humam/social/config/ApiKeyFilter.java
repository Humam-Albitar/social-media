package com.humam.social.config;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Component
public class ApiKeyFilter extends OncePerRequestFilter {

    private static final String API_KEY_HEADER_NAME = "X-API-KEY";
    @Value("${app.api-key}")
    private String STATIC_API_KEY;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        List<String> filterExcludedApis = Arrays.asList("/swagger-ui","/v3/api-docs");
        // Bypass API key check for Swagger UI and API docs
        if (!filterExcludedApis.stream()
                .noneMatch(uri -> request.getRequestURI().contains(uri))) {
            filterChain.doFilter(request, response);
            return;
        }

        String apiKey = request.getHeader(API_KEY_HEADER_NAME);

        if (STATIC_API_KEY.equals(apiKey)) {
            filterChain.doFilter(request, response);  // API key is valid, proceed with the request
        } else {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.getWriter().write("Invalid API Key");
            return;  // Stop processing the request
        }
    }
}
