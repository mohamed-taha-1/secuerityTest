package com.utils;

import java.util.Arrays;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


@RestControllerAdvice
public class CustomizeExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(UsernameNotFoundException.class)
	public ResponseEntity<?> handleRunTimeException( UsernameNotFoundException ex){
		ErrorResponse error=new ErrorResponse(ex.getLocalizedMessage() ,Arrays.asList(ex.getMessage()) );
		return new ResponseEntity<ErrorResponse>(HttpStatus.NOT_FOUND);
	}

}
