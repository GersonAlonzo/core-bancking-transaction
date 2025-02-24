package com.banpais.api.http.controller;

import com.banpais.api.query.model.ClienteQueyModel;
import com.banpais.api.query.service.IQueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clientes")
public class ClienteController {

    private final IQueryService queryService;

    @Autowired
    public ClienteController(IQueryService queryService) {
        this.queryService = queryService;
    }

    @GetMapping("/{identificacion}")
    public ResponseEntity<ClienteQueyModel> getClienteByIdentificacion(@PathVariable String identificacion) {
        return queryService.getClienteByIdentificacion(identificacion)
                .map(cliente -> ResponseEntity.ok(cliente))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<ClienteQueyModel>> getAllClientes() {
        List<ClienteQueyModel> clientes = queryService.getAllClientes();
        return clientes.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(clientes);
    }
}