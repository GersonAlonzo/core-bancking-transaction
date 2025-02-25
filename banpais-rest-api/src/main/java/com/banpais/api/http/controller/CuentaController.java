package com.banpais.api.http.controller;

import com.banpais.api.command.config.client.SoapCuentaClient;
import com.banpais.api.command.model.RegistrarCuentaCommandModel;
import com.banpais.api.http.utils.CuentaValidator;
import com.banpais.api.http.utils.SoapResponseProcessor;
import com.banpais.api.query.model.CuentaQueyModel;
import com.banpais.api.query.service.IQueryService;
import com.banpais.soap.client.EliminarCuentaResponse;
import com.banpais.soap.client.RegistrarCuentaResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cuentas")
public class CuentaController {

    private final IQueryService queryService;
    private final SoapCuentaClient soapCuentaClient;

    @Autowired
    public CuentaController(IQueryService queryService, SoapCuentaClient soapCuentaClient) {
        this.queryService = queryService;
        this.soapCuentaClient = soapCuentaClient;
    }

    @GetMapping("/{numeroCuenta}")
    public ResponseEntity<CuentaQueyModel> getCuentaByNumeroCuenta(@PathVariable String numeroCuenta) {
        return queryService.getCuentaByNumeroCuenta(numeroCuenta)
                .map(cuenta -> ResponseEntity.ok(cuenta))
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping
    public ResponseEntity<List<CuentaQueyModel>> getAllCuentas() {
        
        List<CuentaQueyModel> cuentas = queryService.getAllCuentas();
        return  ResponseEntity.ok(cuentas);
    }

    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<List<CuentaQueyModel>> getCuentasByClienteId(@PathVariable String clienteId) {
        List<CuentaQueyModel> cuentas = queryService.getCuentasByClienteId(clienteId);
        return cuentas.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(cuentas);
    }

    //todo agregar todas las cuentas metodo getAllCuentas
    //command methods
    @PostMapping("/registrar")
    public ResponseEntity<?> registrarCuenta(@RequestBody RegistrarCuentaCommandModel request) {
        CuentaValidator.validarRegistroCuenta(request);
        RegistrarCuentaResponse soapResponse = soapCuentaClient.registrarCuenta(request);
        return SoapResponseProcessor.procesarRespuestaSoap(soapResponse, "Cuenta registrada exitosamente");
    }

    @DeleteMapping("/{numeroCuenta}")
    public ResponseEntity<?> eliminarCuenta(@PathVariable String numeroCuenta) {
        CuentaValidator.validarEliminacionCuenta(numeroCuenta);
        EliminarCuentaResponse soapResponse = soapCuentaClient.eliminarCuenta(numeroCuenta);
        return SoapResponseProcessor.procesarRespuestaSoap(soapResponse, "Cuenta eliminada exitosamente");
    }
}
