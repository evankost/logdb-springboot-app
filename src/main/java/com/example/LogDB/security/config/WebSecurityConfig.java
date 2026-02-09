package com.example.LogDB.security.config;

import com.example.LogDB.jwt.JwtFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {                                                //Class for Security configuration, giving instructions to the Security Component of the
                                                                                //Application on handling the HTTP requests. At first the default login page of the API  
    private final JwtFilter jwtfilter;                                          //is set to be accessible by everyone and the Cross Site Request Forgery is disabled so 
    private final AuthenticationProvider authenticationProvider;                //to avoid unwanted user action denials. Then, all the requests are authenticated except 
    private final LogoutHandler logoutHandler;                                  //they request one of the White Listed URLs. Afterwards, the JWT filter is set to be the 
    private static final String[] WHITE_LIST={"/login","/logout","/register",   //first filter of the filter-chain, using the authentication provider that was created as
        "/register/confirm","/registersend",                                    //a bean for that reason. In the end, the defult logout actions are defined : They consist
        "/authenticate/authenticate","/authenticate/refresh-token"};            //of the logoutHandler for revoking the JWT tokens, the clearing of Context Holder and the 
                                                                                //redirection to the default logout page.
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .formLogin(form -> form.loginPage("/login").defaultSuccessUrl("/home", true).permitAll())
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests((authorize) -> authorize
                .requestMatchers(WHITE_LIST).permitAll()
                .anyRequest().authenticated())
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtfilter, UsernamePasswordAuthenticationFilter.class)
                .logout(logout -> logout.logoutUrl("/logout-action").permitAll()
                .addLogoutHandler(logoutHandler)
                .logoutSuccessHandler((request, response, authentication) ->
                {SecurityContextHolder.clearContext();response.sendRedirect("/logout");}));
               
             
        return http.build();
    }
    
 
}
