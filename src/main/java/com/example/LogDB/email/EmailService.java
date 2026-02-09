
package com.example.LogDB.email;

import com.example.LogDB.exception.RequestCustomException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class EmailService implements EmailSender{                               //Class fro sending emails on behalf of the Application to the Users that want to register.
                                                                                //Implements the EmailSender Interface and overrides the send method.
    private final JavaMailSender mailsender;
    private final static Logger LOGGER = LoggerFactory
            .getLogger(EmailService.class);
            
    @Override
    @Async
    public void send(String to,String email){                                   //Method that sets the parameters of the MIME standard of email formats and sends the email
                                                                                //to the user. In case the email is not sent, a custom http exception is thrown. Could also
        try{                                                                    //return a "Could not send email" message to the Registration Service.
            MimeMessage message = mailsender.createMimeMessage();
            MimeMessageHelper helper =new MimeMessageHelper(message,"utf-8");
            helper.setText(email,true);
            helper.setTo(to);
            helper.setSubject("Confirm your email");
            helper.setFrom("LogDB@gmail.com");
            mailsender.send(message);
        }
        catch (MessagingException exception){
            LOGGER.error("Could not send email",exception);
            throw new RequestCustomException("Could not send email");
        }
    }
    
}
