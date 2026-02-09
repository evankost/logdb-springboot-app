
package com.example.LogDB.registration;

import java.util.function.Predicate;
import java.util.regex.Pattern;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@AllArgsConstructor
public class EmailValidator implements Predicate<String>{                       //Class for checking the email structure to avoidany typos or other mistakes which
                                                                                //might result in misdirects and bounces. 
    @Override
    public boolean test(String email){
        String regexPattern = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@" 
        + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
        return Pattern.compile(regexPattern).matcher(email).matches();
    }
    
}
