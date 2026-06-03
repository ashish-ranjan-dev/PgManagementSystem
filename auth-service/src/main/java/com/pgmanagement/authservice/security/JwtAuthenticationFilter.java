package com.pgmanagement.authservice.security;

import com.pgmanagement.authservice.model.User;
import com.pgmanagement.authservice.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final String AUTH_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";

    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws ServletException, IOException {

        String header = request.getHeader(AUTH_HEADER);

        // No header → let downstream decide. Public endpoints are fine; protected ones get 401 from Spring Security.
        if (header == null || !header.startsWith(BEARER_PREFIX)) {
            chain.doFilter(request, response);
            return;
        }

        String token = header.substring(BEARER_PREFIX.length()).trim();

        // Invalid token → don't authenticate. Spring Security will return 401 for protected paths.
        if (!jwtUtil.isTokenValid(token)) {
            log.debug("Rejected request to {}: invalid or expired token", request.getRequestURI());
            chain.doFilter(request, response);
            return;
        }

        // Token is valid — extract user ID and verify user still exists in DB (Option B from our design).
        Long userId;
        try {
            userId = jwtUtil.extractUserId(token);
        } catch (Exception e) {
            log.debug("Rejected request to {}: token parse error", request.getRequestURI());
            chain.doFilter(request, response);
            return;
        }

        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) {
            log.warn("Token references userId={} which no longer exists", userId);
            chain.doFilter(request, response);
            return;
        }

        User user = userOpt.get();

        // Build the authentication object Spring Security will use.
        // Principal = userId (Long). Authorities derived from role.
        var authorities = List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()));

        var authentication = new UsernamePasswordAuthenticationToken(
                user.getId(),     // principal
                null,             // credentials (not needed for JWT)
                authorities
        );
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        chain.doFilter(request, response);
    }
}