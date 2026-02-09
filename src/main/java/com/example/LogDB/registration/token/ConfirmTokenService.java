
package com.example.LogDB.registration.token;

import java.time.LocalDateTime;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@AllArgsConstructor
public class ConfirmTokenService {                                              //Class that handles the action procedures of the Registration tokens.
    
    private final ConfirmTokenRepository confirmtokenrepository;
    
    public void saveToken(ConfirmToken token){                                  //Method for saving a Registration Token.
        confirmtokenrepository.save(token);
    }
    
    public Optional<ConfirmToken> getToken(String token) {                      //Method for retrieving a Registration Token.
        return confirmtokenrepository.findToken(token);
    }
   
    public int setConfirmedAt(String token) {                                   //Method for confirming a Registration Token.
        return confirmtokenrepository.updateConfirmed(
                token, LocalDateTime.now());
    }
}
