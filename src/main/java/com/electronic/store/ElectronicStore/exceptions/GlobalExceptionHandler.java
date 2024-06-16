package com.electronic.store.ElectronicStore.exceptions;
import com.electronic.store.ElectronicStore.payload.ApiResponseMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler
{
    private Logger logger= LoggerFactory.getLogger(GlobalExceptionHandler.class);
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponseMessage> resourceNotFoundExceptionHandler(ResourceNotFoundException ex)
    {
       logger.info("Inside the exception handler");
        ApiResponseMessage msg= ApiResponseMessage.builder()
                .message(ex.getMessage())
                .status(HttpStatus.NOT_FOUND)
                .success(true)
                .build();
       return new ResponseEntity<>(msg,HttpStatus.NOT_FOUND);
    }

    //This is for method argument not valid exception handler
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String,Object>> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex)
    {
        List<ObjectError> errors=ex.getBindingResult().getAllErrors();
        Map<String,Object> response=new HashMap<>();
        errors.stream().forEach(objectError -> {
            String message=objectError.getDefaultMessage();
            String key=((FieldError) objectError).getField();
            response.put(key,message);
        });
        return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
    }

}
