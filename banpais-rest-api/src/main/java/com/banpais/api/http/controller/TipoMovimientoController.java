// TipoMovimientoController.java
package com.banpais.api.http.controller;

import com.banpais.api.query.model.TipoMovimientoQueryModel;
import com.banpais.api.query.service.IQueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tipos-movimiento")
public class TipoMovimientoController {

    private final IQueryService queryService;

    @Autowired
    public TipoMovimientoController(IQueryService queryService) {
        this.queryService = queryService;
    }

    @GetMapping("/{codigo}")
    public ResponseEntity<TipoMovimientoQueryModel> getByCodigo(@PathVariable String codigo) {
        return queryService.getTipoMovimientoByCodigo(codigo)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<TipoMovimientoQueryModel>> getAll() {
        List<TipoMovimientoQueryModel> tiposMovimiento = queryService.getAllTipoMovimientos();
        return tiposMovimiento.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(tiposMovimiento);
    }
}