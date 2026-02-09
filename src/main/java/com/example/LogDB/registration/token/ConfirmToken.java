
package com.example.LogDB.registration.token;

import com.example.LogDB.users.Users;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import static jakarta.persistence.GenerationType.SEQUENCE;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "confirmtoken",schema = "public")
public class ConfirmToken {                                                     //Class for mapping Registration tokens to the relevant relation of the Database. Allong 
                                                                                //with the token name, the creation-expiration-confirmation dates are stored and every
                                                                                //token is attached to a User. One User may have more than one Registration tokens as 
    @Id                                                                         //long as he/she has not confirmed the email before expiration date and registers again.
    @SequenceGenerator(
            name = "confirmtoken_token_id_seq",
            sequenceName = "confirmtoken_token_id_seq",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = SEQUENCE,
            generator = "confirmtoken_token_id_seq"
    )
    @Column(
            name = "token_id",
            updatable = false
    )
    private Long token_id;
    @Column(name = "token",
            nullable = false)
    private String token;
    @Column(name = "created",
            nullable = false)
    private LocalDateTime created;
    @Column(name = "expires",
            nullable = false)
    private LocalDateTime expires;
    @Column(name = "confirmed")
    private LocalDateTime confirmed;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            nullable = false,
            name = "user_id")
    private Users user;

    public ConfirmToken(String token, LocalDateTime created, LocalDateTime expires, LocalDateTime confirmed, Users user) {
        this.token = token;
        this.created = created;
        this.expires = expires;
        this.confirmed = confirmed;
        this.user = user;
    }

    
    
    
    
}
