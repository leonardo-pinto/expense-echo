package com.backend.expenseecho.filter;

import com.backend.expenseecho.security.JwtTokenProvider;
import com.backend.expenseecho.security.UserInfoUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private JwtTokenProvider jwtService;
    private UserInfoUserDetailsService userDetailsService;

    public JwtAuthFilter(JwtTokenProvider jwtService, @Lazy UserInfoUserDetailsService userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (!hasAuthorizationBearer(request)) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = getAccessToken(request);
        String email = jwtService.extractEmail(token);

        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(email);
            if (!jwtService.validateToken(token, userDetails)) {
                filterChain.doFilter(request, response);
                return;
            }
            setAuthenticationContext(userDetails, request, token);
        }
        filterChain.doFilter(request, response);
    }

    private boolean hasAuthorizationBearer(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        return authHeader != null && authHeader.startsWith("Bearer ");
    }

    private String getAccessToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        return authHeader.split(" ")[1].trim();
    }

    private void setAuthenticationContext(UserDetails userDetails, HttpServletRequest request, String token) {
        String userId = jwtService.extractUserId(token);
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userId, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authToken);
    }
}
