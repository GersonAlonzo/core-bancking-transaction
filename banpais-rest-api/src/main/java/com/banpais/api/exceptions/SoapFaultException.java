package com.banpais.api.exceptions;

public class SoapFaultException extends RuntimeException {

    public SoapFaultException(String message, Throwable cause) {
        super(message, cause);
    }

    // Constructor adicional (opcional, pero útil)
    public SoapFaultException(String message) {
        super(message);
    }
}