package com.securitish.safebox.Service;

import io.jsonwebtoken.Claims;

import javax.servlet.http.HttpServletRequest;

public interface TokenService {
    String generateToken(String username);
    Claims validateToken(HttpServletRequest request);
}
