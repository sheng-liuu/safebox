package com.securitish.safebox.config;

import com.securitish.safebox.Service.SafeBoxService;
import com.securitish.safebox.models.SafeBox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class SafeBoxUserDetailsService implements UserDetailsService {
    @Autowired
    SafeBoxService safeBoxService;

    @Override
    public UserDetails loadUserByUsername(String username) {
        UserDetails foundUser = null;
        SafeBox safeBox = safeBoxService.findSafeBoxByUser(username);
        if(safeBox != null)
        foundUser = User.builder()
                .username(safeBox.getName())
                .password(passwordEncoder().encode(safeBox.getPassword()))
                .roles("USER")
                .build();

        return foundUser;
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
