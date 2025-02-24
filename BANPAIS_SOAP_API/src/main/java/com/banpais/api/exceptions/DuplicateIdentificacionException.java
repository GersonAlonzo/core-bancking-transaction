package com.banpais.api.exceptions;

import org.springframework.ws.soap.server.endpoint.annotation.FaultCode;
import org.springframework.ws.soap.server.endpoint.annotation.SoapFault;

@SoapFault(faultCode = FaultCode.CUSTOM, customFaultCode = "{http://www.example.com/banco}IDENTIFICACION_DUPLICADA")
public class DuplicateIdentificacionException extends RuntimeException {
    public DuplicateIdentificacionException(String message) {
        super(message);
    }
}