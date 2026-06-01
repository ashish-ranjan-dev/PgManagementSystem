package com.pgmanagement.pgservice.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@Slf4j
public class InternalApiKeyFilter extends OncePerRequestFilter {

    private static final String HEADER_NAME = "X-Internal-API-Key";

    private final String expectedKey;

    public InternalApiKeyFilter(@Value("${app.internal-api-key}") String expectedKey) {
        this.expectedKey = expectedKey;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws ServletException, IOException {

        if (request.getRequestURI().startsWith("/actuator/health")) {
            chain.doFilter(request, response);
            return;
        }

        String providedKey = request.getHeader(HEADER_NAME);

        if (!expectedKey.equals(providedKey)) {
            log.warn("Rejected request to {} — invalid API key", request.getRequestURI());
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType("application/json");
            response.getWriter().write("{\"error\":\"Unauthorized\"}");
            return;
        }

        chain.doFilter(request, response);
    }
}