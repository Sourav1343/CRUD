package com.dev.invoice.exception;

//Custom Exception
public class InvoiceNotFoundException extends RuntimeException{

    private static final long serialVersionUID = 1L;

    public InvoiceNotFoundException() {
        super();
    }

    public InvoiceNotFoundException(String message) {
        super(message);
    }

}
