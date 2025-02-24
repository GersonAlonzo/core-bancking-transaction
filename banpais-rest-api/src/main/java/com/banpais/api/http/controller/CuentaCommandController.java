//package com.banpais.api.http.controller;
//
//import com.banpais.api.command.config.client.SoapCuentaClient;
//import com.banpais.api.command.model.RegistrarCuentaCommandModel;
//import com.banpais.api.http.utils.CuentaValidator;
//import com.banpais.api.http.utils.SoapResponseProcessor;
//import com.banpais.soap.client.EliminarCuentaResponse;
//import com.banpais.soap.client.RegistrarCuentaResponse;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//@RestController
//@RequestMapping("/api/cuentas")
//public class CuentaCommandController {
//
//    private final SoapCuentaClient soapCuentaClient;
//
//    @Autowired
//    public CuentaCommandController(SoapCuentaClient soapCuentaClient) {
//        this.soapCuentaClient = soapCuentaClient;
//    }
//
//    @PostMapping("/registrar")
//    public ResponseEntity<?> registrarCuenta(@RequestBody RegistrarCuentaCommandModel request) {
//        // 1. Validaciones
//        CuentaValidator.validarRegistroCuenta(request);
//
//        // 2. Llamar al SoapClient
//        RegistrarCuentaResponse soapResponse = soapCuentaClient.registrarCuenta(request);
//
//        // 3. Procesar la respuesta SOAP de manera genérica
//        return SoapResponseProcessor.procesarRespuestaSoap(soapResponse, "Cuenta registrada exitosamente");
//    }
//    
//    @DeleteMapping("/{numeroCuenta}")
//    public ResponseEntity<?> eliminarCuenta(@PathVariable String numeroCuenta) {
//        // 1. Validación
//        CuentaValidator.validarEliminacionCuenta(numeroCuenta);
//
//        // 2. Llamar al SoapClient
//        EliminarCuentaResponse soapResponse = soapCuentaClient.eliminarCuenta(numeroCuenta);
//
//        // 3. Procesar la respuesta SOAP de manera genérica
//        return SoapResponseProcessor.procesarRespuestaSoap(soapResponse, "Cuenta eliminada exitosamente");
//    }
//}