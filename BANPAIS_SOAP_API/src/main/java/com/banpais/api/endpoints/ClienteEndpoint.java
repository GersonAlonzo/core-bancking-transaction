package com.banpais.api.endpoints;


import com.banpais.api.service.ClienteSoapService;
import com.example.banco.*;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

@Endpoint
public class ClienteEndpoint {
    private static final String NAMESPACE_URI = "http://www.example.com/banco";

    private final ClienteSoapService clienteSoapService;

    public ClienteEndpoint(ClienteSoapService clienteSoapService) {
        this.clienteSoapService = clienteSoapService;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "registrarClienteRequest")
    @ResponsePayload
    public RegistrarClienteResponse registrarCliente(@RequestPayload RegistrarClienteRequest request) {
        System.out.println("trama completa:"+request.getTrama().toString());
        RegistrarClienteResponse response = clienteSoapService.registrarCliente(request.getTrama());
        return response; 
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "actualizarClienteRequest")
    @ResponsePayload
    public ActualizarClienteResponse actualizarCliente(@RequestPayload ActualizarClienteRequest request) {
        ActualizarClienteResponse response = clienteSoapService.actualizarCliente(request.getTrama());
        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "eliminarClienteRequest")
    @ResponsePayload
    public EliminarClienteResponse eliminarCliente(@RequestPayload EliminarClienteRequest request) {
        EliminarClienteResponse response = clienteSoapService.eliminarCliente(request.getTrama());
        return response;
    }
}