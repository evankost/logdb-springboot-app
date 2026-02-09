package com.example.LogDB.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


@Configuration
public class PassswordEncode {                                                  //Class for providing password encoding: The BCrypt encoder has benn chosen.
    
    @Bean
    public BCryptPasswordEncoder Encoder() {return new BCryptPasswordEncoder();}
    
}
