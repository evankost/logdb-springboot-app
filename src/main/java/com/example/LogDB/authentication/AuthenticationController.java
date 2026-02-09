package com.example.LogDB.authentication;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.core.Authentication;
import com.example.LogDB.security.LogoutService;

@RestController
@RequestMapping("/authenticate")
@RequiredArgsConstructor
public class AuthenticationController {                                         //Controller Class for JWT Authentication.
    
    private final AuthenticationService service;
    private final LogoutService logoutservice;
   
    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(                 //End Point for JWT Authentication and Token creation.
      @RequestBody AuthenticationRequest request){return ResponseEntity.ok(service.authenticate(request));
    }
    
    @PostMapping("/refresh-token")
    public void refreshToken(                                                   //End Point for JWT Token renewal. 
            HttpServletRequest request,HttpServletResponse response) throws IOException {
        service.refreshToken(request, response);
    }
 
    @PostMapping("/logout")
    public void logout(                                                         //End Point for JWT Authentication Logout.
            HttpServletRequest request,HttpServletResponse response,Authentication authentication) throws IOException {
        logoutservice.logout(request, response, authentication);
    }

}
