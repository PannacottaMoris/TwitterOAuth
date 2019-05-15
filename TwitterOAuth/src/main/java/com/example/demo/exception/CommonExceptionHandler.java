package com.example.demo.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CommonExceptionHandler{
	@ExceptionHandler(ExceptionCommon.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	protected Map<String, Object> commonExceptionHandler(ExceptionCommon e){
		Map<String, Object> map = new HashMap<>();
		map.put("message", e.getComment());
		return map;
	}
	
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected Map<String, Object> exceptionHandler(Exception e){
        Map<String, Object> map = new HashMap<>();
        map.put("message", "ERROR");
        return map;
    }
}
