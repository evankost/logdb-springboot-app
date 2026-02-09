package com.example.LogDB.jwt;

import com.example.LogDB.jwt.token.JwtTokenRepository;
import com.example.LogDB.users.UsersService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {                           //Class for implementing JWT authentication on HTTP Requests by applying a filter and
                                                                                //more precise by extending a subclass of Filter Class so as to be activated for every
    private final JwtService jwtservice;                                        //request. 
    private final UsersService userservice;
    private final JwtTokenRepository tokenrepository;    
    
    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,        //Method that applies the JWT filter to the HTTP request. At first it extracts the 
                                    @NonNull HttpServletResponse response,      //"Authorization" header of the request and checks for a Bearer token. If the token
                                    @NonNull FilterChain filterChain)           //does not exist or it not of Bearer type, the request-response are passed to the 
                                    throws ServletException, IOException {      //next filter in the security filterchain. If a Bearer token exists, the username 
                                                                                //portion of JWT is extracted. If it is not null and the User is not authenticated,
                                                                                //the User details  are fetched from the Database and the token is then checked for
        final String authHeader = request.getHeader("Authorization");           //validity.If the token is not expired nor revoked and belongs to the specific User,
        final String jwt;                                                       //an authentication token is produced and the security context holder gets updated.
        final String username;                                                  //In the end it passes the request-response to the next filter in the filter chain. 
        
        
        if (authHeader == null ||!authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
        return;
       }
    
        jwt = authHeader.substring(7);
        username = jwtservice.extractUsername(jwt);
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails user = this.userservice.loadUserByUsername(username);
            var tokenValid = tokenrepository.findByToken(jwt).map(
                    t -> !t.isExpired() && !t.isRevoked()).orElse(false);
            if (jwtservice.TokenValid(jwt,user) && tokenValid) {
                UsernamePasswordAuthenticationToken authenticationtoken = 
                        new UsernamePasswordAuthenticationToken(user,null,user.getAuthorities());
                authenticationtoken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationtoken);
            }
        }
        filterChain.doFilter(request,response);
    }
}
