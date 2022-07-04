package com.securitish.safebox.DTOs;

import com.securitish.safebox.models.SafeBox;

public class TokenDTO {
    private String token;

    public TokenDTO() {
    }

    public static TokenDTO fromEntity(String token) {
        TokenDTO tokenDTO = new TokenDTO();
        if(token != null) tokenDTO.setToken(token);

        return tokenDTO;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
