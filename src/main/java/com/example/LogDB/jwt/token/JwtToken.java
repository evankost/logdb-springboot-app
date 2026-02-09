package com.example.LogDB.jwt.token;


import com.example.LogDB.users.Users;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import static jakarta.persistence.GenerationType.SEQUENCE;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "jwttoken",schema = "public")
public class JwtToken {                                                         //Class for mapping JWT tokens to the relevant relation of the Database. A JWT token object
                                                                                //consists of the JWT token String, type and the revoked & expired statuses. The JWT tokens
    @Id                                                                         //are stored in the Database for the management of login and logout procedures of a User.
    @SequenceGenerator(
            name = "jwttoken_token_id_seq",
            sequenceName = "jwttoken_token_id_seq",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = SEQUENCE,
            generator = "jwttoken_token_id_seq"
    )
    @Column(
            name = "token_id",
            updatable = false
    )
    public Integer token_id;

    @Column(name = "token",
           nullable = false)
    public String token;

    @Column(name = "tokentype",
            nullable = false)
    @Enumerated(EnumType.STRING)
    public JwtTokenType tokenType = JwtTokenType.BEARER;

    @Column(name = "revoked",
            nullable = false)
    public boolean revoked;
    
    @Column(name = "expired",
            nullable = false)    
    public boolean expired;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    public Users user;

    public JwtToken(String token, boolean revoked, boolean expired, Users user) {
        this.token = token;
        this.revoked = revoked;
        this.expired = expired;
        this.user = user;
    }
  
  
}
