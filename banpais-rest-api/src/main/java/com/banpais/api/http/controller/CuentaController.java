package com.banpais.api.http.controller;

import com.banpais.api.query.model.CuentaQueyModel;
import com.banpais.api.query.service.IQueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cuentas")
public class CuentaController {

    private final IQueryService queryService;

    @Autowired
    public CuentaController(IQueryService queryService) {
        this.queryService = queryService;
    }

    @GetMapping("/{numeroCuenta}")
    public ResponseEntity<CuentaQueyModel> getCuentaByNumeroCuenta(@PathVariable String numeroCuenta) {
        return queryService.getCuentaByNumeroCuenta(numeroCuenta)
                .map(cuenta -> ResponseEntity.ok(cuenta))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<List<CuentaQueyModel>> getCuentasByClienteId(@PathVariable String clienteId) {
        List<CuentaQueyModel> cuentas = queryService.getCuentasByClienteId(clienteId);
        return cuentas.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(cuentas);
    }
    
    //todo agregar todas las cuentas metodo getAllCuentas
}