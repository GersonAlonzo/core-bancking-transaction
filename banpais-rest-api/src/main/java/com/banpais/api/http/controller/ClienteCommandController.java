//package com.banpais.api.http.controller;
//
//import com.banpais.api.command.config.client.SoapClienteClient;
//import com.banpais.api.command.model.RegistrarClienteCommandModel;
//import com.banpais.api.http.utils.ClienteValidator;
//import com.banpais.api.http.utils.SoapResponseProcessor;
//import com.banpais.soap.client.ActualizarClienteResponse;
//import com.banpais.soap.client.EliminarClienteResponse;
//import com.banpais.soap.client.RegistrarClienteResponse;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//@RestController
//@RequestMapping("/api/clientes")
//public class ClienteCommandController {
//
//    private final SoapClienteClient soapClienteClient;
//
//    @Autowired
//    public ClienteCommandController(SoapClienteClient soapClienteClient) {
//        this.soapClienteClient = soapClienteClient;
//    }
//
//    @PostMapping("/registrar")
//    public ResponseEntity<?> registrarCliente(@RequestBody RegistrarClienteCommandModel request) {
//        // Validación movida al ClienteValidator
//        ClienteValidator.validarCliente(request);
//        
//        // Llamar al SoapClient
//        RegistrarClienteResponse soapResponse = soapClienteClient.registrarCliente(request);
//        
//        // Procesar la respuesta SOAP de manera genérica
//        return SoapResponseProcessor.procesarRespuestaSoap(soapResponse, "Cliente registrado exitosamente");
//    }
//
//    @PutMapping("/actualizar")
//    public ResponseEntity<?> actualizarCliente(@RequestBody RegistrarClienteCommandModel request) {
//        // Validación movida al ClienteValidator
//        ClienteValidator.validarCliente(request);
//        
//        // Validación específica de ID para actualización
//        if (request.getId() == null || request.getId().trim().isEmpty()) {
//            throw new IllegalArgumentException("El ID del cliente es obligatorio para actualizar.");
//        }
//        
//        // Llamar al SoapClient
//        ActualizarClienteResponse soapResponse = soapClienteClient.actualizarCliente(request);
//        
//        // Procesar la respuesta SOAP de manera genérica
//        return SoapResponseProcessor.procesarRespuestaSoap(soapResponse, "Cliente actualizado exitosamente");
//    }
//
//    @DeleteMapping("/{clienteId}")
//    public ResponseEntity<?> eliminarCliente(@PathVariable String clienteId) {
//        // Validación de ID de cliente
//        if (clienteId == null || clienteId.trim().isEmpty()) {
//            throw new IllegalArgumentException("El ID del cliente es obligatorio.");
//        }
//        
//        // Llamar al SoapClient
//        EliminarClienteResponse soapResponse = soapClienteClient.eliminarCliente(clienteId);
//        
//        // Procesar la respuesta SOAP de manera genérica
//        return SoapResponseProcessor.procesarRespuestaSoap(soapResponse, "Cliente eliminado exitosamente");
//    }
//}