package com.example.LogDB.security;

import com.example.LogDB.users.UsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class ApplicationConfig {
    
    private final UsersService userservice;
    
    @Bean
    public AuthenticationManager authenticationManager(                         //Method that extracts the information of the Authentication Manager for the given configuration. 
            AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {                                  //Method that sets the password encoder.
        return new PassswordEncode().Encoder();
    }

    @Bean
    public AuthenticationProvider AuthProvider() {                              //Method for setting the Password Encoder and the Users Details Service for the AuthenticationProvider 
        DaoAuthenticationProvider authprovider =new DaoAuthenticationProvider();
        authprovider.setPasswordEncoder(passwordEncoder());
        authprovider.setUserDetailsService(userservice);
        return authprovider;
    }
}
