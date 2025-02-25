package com.banpais.api.endpoints;

import com.banpais.api.service.CuentaSoapService;
import com.example.banco.*;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

@Endpoint
public class CuentaEndpoint {

    private static final String NAMESPACE_URI = "http://www.example.com/banco";

    private  final CuentaSoapService cuentaSoapService;

    public CuentaEndpoint(CuentaSoapService cuentaSoapService){
        this.cuentaSoapService = cuentaSoapService;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "registrarCuentaRequest")
    @ResponsePayload
    public RegistrarCuentaResponse registrarCuenta(@RequestPayload RegistrarCuentaRequest request){
        return cuentaSoapService.registrarCuenta(request.getTrama());
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "actualizarCuentaRequest")
    @ResponsePayload
    public ActualizarCuentaResponse actualizarCuenta(@RequestPayload ActualizarCuentaRequest request){
       return cuentaSoapService.actualizarCuenta(request.getTrama());
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "eliminarCuentaRequest")
    @ResponsePayload
    public EliminarCuentaResponse eliminarCuenta(@RequestPayload EliminarCuentaRequest request){
        return cuentaSoapService.eliminarCuenta(request.getTrama());
    }
}