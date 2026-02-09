package com.example.LogDB.email;


public interface EmailSender {                                                  //Interface that an Email Service must implement. 
    
    void send(String to,String email);
    
}
