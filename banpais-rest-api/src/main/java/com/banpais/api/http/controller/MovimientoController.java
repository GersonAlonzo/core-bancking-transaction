package com.banpais.api.http.controller;

import com.banpais.api.query.model.MovimientoQueyModel;
import com.banpais.api.query.service.IQueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/movimientos")
public class MovimientoController {

    private final IQueryService queryService;

    @Autowired
    public MovimientoController(IQueryService queryService) {
        this.queryService = queryService;
    }

    @GetMapping("/{numeroCuenta}")
    public ResponseEntity<List<MovimientoQueyModel>> getMovimientosByCuenta(@PathVariable String numeroCuenta) {
        List<MovimientoQueyModel> movimientos = queryService.getMovimientosByCuenta(numeroCuenta);
        return movimientos.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(movimientos);
    }
}