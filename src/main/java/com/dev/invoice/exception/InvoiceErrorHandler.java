package com.dev.invoice.exception;


import com.dev.invoice.Entity.ErrorType;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Date;

@RestControllerAdvice
public class InvoiceErrorHandler {

    /**
     * In case of InvoiceNotFoundException is thrown
     * form any controller method, this logic getd
     * executed which behaves like re-usable and
     * clear code (Code Modularity)
     * @param nfe
     * @return ResponseEntity
     */
    @ExceptionHandler(InvoiceNotFoundException.class)
    public ResponseEntity<ErrorType> handleNotFound(InvoiceNotFoundException nfe){
        return new ResponseEntity<ErrorType>(
                new ErrorType(
                        new Date(System.currentTimeMillis()).toString(),
                        "400- NOT FOUND",
                        nfe.getMessage()),
                HttpStatus.NOT_FOUND);
    }




}
