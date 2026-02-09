package com.example.LogDB.exception;

public class RequestCustomException extends RuntimeException{                   //Class that extends the RuntimeException Class and uses its constuctors for creating Objects that
                                                                                //ocntain the error message or the message and the actual cause of it.
    public RequestCustomException (String error){                               
        super(error);
    }
    
    public RequestCustomException (String error,Throwable cause){
        super(error,cause);
    }
}
