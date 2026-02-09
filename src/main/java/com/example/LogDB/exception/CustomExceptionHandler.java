
package com.example.LogDB.exception;

import java.time.ZonedDateTime;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice                                               
public class CustomExceptionHandler {                                           //Class for handling custom or exisitng exceptions, in order to customise the appearnce of the exception.
    
    @ExceptionHandler(value = {RequestCustomException.class})                   //Class that receives a Custom Exception container, gets the values for providing the custom exception 
    public ResponseEntity<Object> handleRequestException(                       //format and passes the payload with the formated details to the response entity.
            RequestCustomException exception){
    
        HttpStatus badRequest = HttpStatus.BAD_REQUEST;                        
        CustomException customException = new CustomException(exception.getMessage(),badRequest,ZonedDateTime.now());
        return new ResponseEntity<>(customException,badRequest);
    }
    
}
