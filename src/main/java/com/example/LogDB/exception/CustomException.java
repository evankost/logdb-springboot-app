package com.example.LogDB.exception;

import java.time.ZonedDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;


@Getter
@Setter
@AllArgsConstructor
public class CustomException {                                                  //Class for defining the json Structure of Spring Boot Exceptions 
    
    private final String message;
    private final HttpStatus httpStatus;
    private final ZonedDateTime timestamp;

}
