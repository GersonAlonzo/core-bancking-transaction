package com.banpais.api.endpoints;

import com.banpais.api.service.MovimientoSoapService;
import com.example.banco.RegistrarMovimientoRequest;
import com.example.banco.RegistrarMovimientoResponse;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

@Endpoint
public class MovimientoEndpoint {

    private static final String NAMESPACE_URI = "http://www.example.com/banco";

    private final MovimientoSoapService movimientoSoapService;

    public MovimientoEndpoint(MovimientoSoapService movimientoSoapService) {
        this.movimientoSoapService = movimientoSoapService;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "registrarMovimientoRequest")
    @ResponsePayload
    public RegistrarMovimientoResponse registrarMovimiento(@RequestPayload RegistrarMovimientoRequest request) {
      
        return movimientoSoapService.registrarMovimiento(request.getTrama());
    }
}