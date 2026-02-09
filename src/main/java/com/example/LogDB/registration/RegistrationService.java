package com.example.LogDB.registration;

import com.example.LogDB.email.EmailSender;
import com.example.LogDB.registration.token.ConfirmToken;
import com.example.LogDB.registration.token.ConfirmTokenService;
import com.example.LogDB.users.Users;
import com.example.LogDB.users.UsersService;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@AllArgsConstructor
public class RegistrationService {                                              //Class for handling the Registration requests and the confirmation of Registration tokens. 

    private final UsersService userservice;
    private final EmailValidator emailValidator;
    private final ConfirmTokenService confirmtokenservice;
    private final EmailSender emailsender;

    
    public String register(RegistrationRequest request) {                       //Method for handling the Registration requests. At first checks the email for a valid form :
        boolean isValidmail = emailValidator.test(request.getEmail());          //If the email is valid signs up the user and if the signing procedure is successful, sends 
        if (isValidmail){                                                       //an email with the Registration token. If the email is not valid, reutrns a message to inform
            String token= userservice.signUpUser(new Users(                     //about the accepted email structure.
                        request.getFirstName(),
                        request.getLastName(),
                        request.getUserName(),
                        request.getEmail(),
                        request.getPassword(),
                        request.getRole()));
            if (!token.startsWith("Username") && !token.startsWith("Email") && !token.startsWith("Already")){
            String link = "http://localhost:8080/register/confirm?token=" + token;
            emailsender.send(request.getEmail(), userservice.buildEmail(request.getLastName(),link));
            return "Register Sent";}
            else return token;}
        else return  "Email not Valid! Choose a Valid Email Structure like: " +
                "username@domain.com | user.name@domain.com | user-name@domain.com |"
                + "username@domain.co.in | user_name@domain.com";
    }

    
    @Transactional
    public String confirmToken(String token) {                                  //Method for confirming Registration tokens. If the token is found, chscks the confirmation status.
        ConfirmToken confirmtoken = confirmtokenservice                         //If the token is not already confirmed checks the expiration status. At last, if the confirmation
                .getToken(token)                                                //token is exists and it is not expired, sets the token as confirmed and enables the User. 
                .orElseThrow(() ->
                        new IllegalStateException("token not found"));

        if (confirmtoken.getConfirmed() != null) { return "Email already confirmed! Plese Login.";}
        LocalDateTime expiredAt = confirmtoken.getExpires();
        if (expiredAt.isBefore(LocalDateTime.now())) {return "Registration Link expired! Please Register Again";}

        confirmtokenservice.setConfirmedAt(token);
        Users user = confirmtoken.getUser();
        userservice.enableUser(user.getUsername());
        return "Confirmed Email";
    }
    

    
}
