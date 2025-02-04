package com.example.securityapp.exception;

public class CustomJwtException extends RuntimeException{
    public CustomJwtException(){
        super();
    }
    public CustomJwtException(String e){
        super(e);
    }
}
