package com.example.LogDB.authentication;

import com.example.LogDB.jwt.JwtService;
import com.example.LogDB.jwt.token.JwtToken;
import com.example.LogDB.jwt.token.JwtTokenRepository;
import com.example.LogDB.users.Users;
import com.example.LogDB.users.UsersRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {                                            //Class that implements the JWT Authentication Service
    
    private final UsersRepository userepository;
    private final JwtTokenRepository tokenrepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationmanager;
    

    
    public AuthenticationResponse authenticate(AuthenticationRequest request){  //Method for authenticating a request using AuthenticationManager.
        authenticationmanager.authenticate(                                     //If the authentication succeeds, the user details are retrieved 
                new UsernamePasswordAuthenticationToken(                        //from the Database and a new JWT Token is generated for that User.
                        request.getUserName(),request.getPassword()));          //Afterwards the token is saved in the Database and is sent back to  
        Users user = userepository.findByUserName(                              //the User in the Authentication response header.
                request.getUserName()).orElseThrow();
        String jwttoken = jwtService.generateToken(user);
        JwtToken JWTtoken = new JwtToken(jwttoken,false,false,user);
        tokenrepository.save(JWTtoken);
        return AuthenticationResponse.builder().accessToken(jwttoken).build();             
    }
    
    private void saveUserToken(Users user, String jwttoken) {                   //Method for associating a JWT token with a specific User and store it in the 
        JwtToken token = new JwtToken(jwttoken,false,false,user);               //Database. 
        tokenrepository.save(token);
  }
    
    private void revokeAllUserTokens(Users user) {                              //Method for revoking all JWT Tokens from a User so as to not be valid anymore.
        List<JwtToken> validUserTokens = 
                tokenrepository.findAllValidTokenByUser(user.getUser_id().intValue());
        if (validUserTokens.isEmpty())return;
        validUserTokens.forEach(token -> {token.setExpired(true);token.setRevoked(true);});
        tokenrepository.saveAll(validUserTokens);
  }
    
    
    public void refreshToken(                                                   //Method for revoking all Users Tokens: At first extracts "Authorization" header of                                          
            HttpServletRequest request,                                         //the request and checks for a Bearer token.If the token does not exist or it not of
            HttpServletResponse response) throws IOException {                  //Bearer type , the request-response are passed to the /next filter in the security 
                                                                                //filterchain. If a Bearer token exists , the username portion of JWT is extracted.
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION); //If it is not null and the User is not authenticated, the User details  are fetched 
        final String accessToken;                                               //from the User details  are fetched from the Database and the token is then checked
        final String username;                                                  //for validity. If the token is not expired (cannot be revoked as the User has already 
                                                                                //been authenticated) and belongs to the specific User, all the existing User's JWT   
        if (authHeader == null ||!authHeader.startsWith("Bearer ")) {return;}   //are revoked and a new one is generated. Finally it returns the former valid access 
        accessToken= authHeader.substring(7);                                   //token and the refreshed token to the User in the Authentication response header.
        username = jwtService.extractUsername(accessToken);
        if (username != null) {
            Users user = userepository.findByUserName(username).orElseThrow();
        if (jwtService.TokenValid(accessToken, user)) {
            String refreshedToken = jwtService.generateToken(user);
            revokeAllUserTokens(user);
            saveUserToken(user, refreshedToken);
        var authResponse = AuthenticationResponse.builder().accessToken(refreshedToken)
                .refreshToken(accessToken).build();
        new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
      }
    }
  }
}
