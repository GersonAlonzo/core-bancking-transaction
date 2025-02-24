package com.banpais.api.exceptions;

import org.springframework.ws.soap.server.endpoint.annotation.FaultCode;
import org.springframework.ws.soap.server.endpoint.annotation.SoapFault;

//Excepciones personalizadas
@SoapFault(faultCode = FaultCode.CUSTOM, customFaultCode = "{http://www.example.com/banco}CLIENTE_NO_ENCONTRADO")
public class ClienteNotFoundException extends RuntimeException{
    public ClienteNotFoundException(String message) {
        super(message);
    }
}