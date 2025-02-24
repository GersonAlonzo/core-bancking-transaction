package com.banpais.api.exceptions;

import org.springframework.ws.soap.server.endpoint.annotation.FaultCode;
import org.springframework.ws.soap.server.endpoint.annotation.SoapFault;

@SoapFault(faultCode = FaultCode.CUSTOM, customFaultCode = "{http://www.example.com/banco}CUENTA_NO_ENCONTRADA")
public class CuentaNotFoundException extends RuntimeException{
    public CuentaNotFoundException(String message){
        super(message);
    }
}