package com.example.LogDB.users;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import static jakarta.persistence.GenerationType.SEQUENCE;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.util.Collection;
import java.util.Collections;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;


@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@Entity
@Table(
        name = "users",
        schema = "public",
        uniqueConstraints ={
            @UniqueConstraint(
                    name="unique username",
                    columnNames="user_name"
            ),
            @UniqueConstraint(
                    name="unique email",
                    columnNames="email"
            )
        })
public class Users implements UserDetails{                                      //Class for mapping users  of the Application to the relevant relation of the Database. This  
                                                                                //Class implements the UserDetails Interface so as to provide the appropriate authentication
    @Id                                                                         //and authorization methods  for the implementation of Spring Security. Although the usename
    @SequenceGenerator(                                                         //is the main authentication attribute, we demand that the email is also unique, so as to be
            name = "users_id_seq",                                              //able to send the registration email only to the correct Application user.  
            sequenceName = "users_id_seq",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = SEQUENCE,
            generator = "users_id_seq"
    )
    @Column(
            name = "user_id",
            updatable = false
    )
    private Long user_id;
    @Column(name = "first_name",
            nullable = false)
    private String first_name;
    @Column(name = "last_name",
            nullable = false)
    private String last_name;
    @Column(name = "user_name",
            nullable = false)
    private String userName;
    @Column(name = "mail",
            nullable = false)
    private String mail;
    @Column(name = "password",
            nullable = false)
    private String password;
    @Enumerated(EnumType.STRING)
    @Column(name = "role",
            nullable = false)
    private UsersRole role;
    @Column(name = "locked",
            columnDefinition = "boolean not null default false")
    private Boolean locked;
    @Column(name = "enabled",
            columnDefinition = "boolean not null default false")
    private Boolean enabled;


    public Users(String first_name, String last_name, String userName, String mail, String password, UsersRole role) {
        this.first_name = first_name;
        this.last_name = last_name;
        this.userName = userName;
        this.mail = mail;
        this.password = password;
        this.role = role;
        this.locked = false;
        this.enabled = false;
    }
 

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(role.name());
        return Collections.singletonList(authority);
    }
    
    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return userName;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !locked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }
}
