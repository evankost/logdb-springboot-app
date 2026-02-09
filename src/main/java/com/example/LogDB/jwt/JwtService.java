package com.example.LogDB.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;


@Service
public class JwtService {                                                       //Class for manipulating the JWT tokens. The attributes of the Class are provided by the application 
                                                                                //configuration file.
    @Value("${application.security.jwt.secret-key}")
    private String secretKey;
    @Value("${application.security.jwt.expiration}")
    private long jwtExpiration;
    @Value("${application.security.jwt.refresh-token.expiration}")
    private long refreshExpiration;

    
    private Key getSignInKey() {                                                //Method for applying the random generated secret key so as to produce the signing key for
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);                    //the encryption and decryption processes of the JWT token. 
        return Keys.hmacShaKeyFor(keyBytes);
    }
    
       
   public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {//Generic method for extracting a single claim from all the claims of the token.
        final Claims claims = extractClaims(token);
        return claimsResolver.apply(claims);
    }
   
    private Claims extractClaims(String token){                                 //Method for extracting all the fields (claims) of the JWT token with the use of the signinig Key
        return Jwts.parser().setSigningKey(getSignInKey()).build()              //method.
        .parseClaimsJws(token).getBody();
   }
    
    
    public String generateToken(UserDetails userDetails) {                      //Method for generating a token wtihout extra claims.
        return generateToken(new HashMap<>(), userDetails);
    }    
    
    public String generateToken(                                                //Geleral method for calling JWT builder method.         
            Map<String, Object> extraClaims,
            UserDetails userDetails) {
        return buildToken(extraClaims, userDetails, jwtExpiration);
    }
    
    public String generateRefreshToken(UserDetails userDetails) {               //Method for creating a JWT token for a certain User with a new expiration date.              
        return buildToken(new HashMap<>(), userDetails, refreshExpiration);
    }
           
    private String buildToken(Map<String, Object> extraClaims,                  //Method for building a JWT token by setting the username, the issue and expiration dates, as long 
            UserDetails userDetails,long expiration) {                          //as some extra claims. All the above information is encrypted with the signing key.
        return Jwts.builder()
            .setClaims(extraClaims).setSubject(userDetails.getUsername())
            .setIssuedAt(new Date(System.currentTimeMillis()))
            .setExpiration(new Date(System.currentTimeMillis() + expiration))
            .signWith(getSignInKey(), SignatureAlgorithm.HS256).compact();
    }   
    
    public String extractUsername(String jwttoken) {                            //Method for extracting the username field of the JWT token.
        return extractClaim(jwttoken, claims -> claims.getSubject());
    }

    public boolean TokenValid(String token, UserDetails userDetails) {          //Method for validating a token for a certain User by comparing the username and checking the expiration 
        final String username = extractUsername(token);                         //date.
        return (username.equals(userDetails.getUsername())) && !TokenExpired(token);
    }

    private boolean TokenExpired(String token) {                                //Method for checking JWT token expiration status.
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {                              //Method that extracts the expiration date from the token.
        return extractClaim(token, Claims::getExpiration);
    }
}
