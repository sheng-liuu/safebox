package com.securitish.safebox.config;

import com.securitish.safebox.Service.TokenService;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.securitish.safebox.ApplicationConstants.*;

public class JwtAuthorizationFilter extends OncePerRequestFilter {
    @Autowired
    private TokenService tokenService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authenticationHeader = request.getHeader(AUTHORIZATION_HEADER);
        if(request.getRequestURI().startsWith("/api/v1/safebox") && request.getRequestURI().endsWith("/items")) {
            if (authenticationHeader == null || !authenticationHeader.startsWith(AUTHORIZATION_PREFIX)) {
                response.setStatus(HttpStatus.UNAUTHORIZED.value(), TOKEN_VOID);
            }
            else {
                Claims claims = tokenService.validateToken(request);
                if(claims != null) filterChain.doFilter(request, response);
                else {
                    response.setStatus(HttpStatus.UNAUTHORIZED.value(), TOKEN_EXPIRED);
                }
            }
        } else {
            filterChain.doFilter(request, response);
        }
    }
}