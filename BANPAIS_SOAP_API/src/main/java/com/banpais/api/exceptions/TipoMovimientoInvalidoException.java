package com.banpais.api.exceptions;

import org.springframework.ws.soap.server.endpoint.annotation.FaultCode;
import org.springframework.ws.soap.server.endpoint.annotation.SoapFault;

@SoapFault(faultCode = FaultCode.CUSTOM, customFaultCode = "{http://www.example.com/banco}TIPO_MOVIMIENTO_INVALIDO")
public class TipoMovimientoInvalidoException extends RuntimeException {
     public TipoMovimientoInvalidoException(String message) {
        super(message);
    }
}