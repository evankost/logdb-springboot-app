package com.example.LogDB.registration;

import com.example.LogDB.users.UsersRole;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;


@Getter
@EqualsAndHashCode
@ToString
public class RegistrationRequest {                                              //Class for defining the attributes that a potential User haw to provide to the
                                                                                //Application when he submits data in the registration form.
    private final String firstName;
    private final String lastName;
    private final String userName;
    private final String email;
    private final String password;
    private final UsersRole role;

    public RegistrationRequest(String firstName, String lastName, String userName, String email, String password, UsersRole role) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.userName = userName;
        this.email = email;
        this.password = password;
        this.role = role;
    }
}
