package com.banpais.api.http.controller;

import com.banpais.api.command.config.client.SoapMovimientoClient;
import com.banpais.api.command.model.RegistrarMovimientoCommandModel;
import com.banpais.api.http.utils.ErrorResponse;
import com.banpais.api.http.utils.MovimientoValidator;
import com.banpais.api.http.utils.SuccessResponse;
import com.banpais.api.query.model.MovimientoQueyModel;
import com.banpais.api.query.service.IQueryService;
import com.banpais.soap.client.RegistrarMovimientoResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import org.springframework.http.HttpStatus;

@RestController
@RequestMapping("/api/movimientos")
public class MovimientoController {

    private final IQueryService queryService;
    private final SoapMovimientoClient soapMovimientoClient;
    
    @Autowired
    public MovimientoController(IQueryService queryService,SoapMovimientoClient soapMovimientoClient) {
        this.queryService = queryService;
         this.soapMovimientoClient = soapMovimientoClient;
    }

    @GetMapping("/{numeroCuenta}")
    public ResponseEntity<List<MovimientoQueyModel>> getMovimientosByCuenta(@PathVariable String numeroCuenta) {
        List<MovimientoQueyModel> movimientos = queryService.getMovimientosByCuenta(numeroCuenta);
        return movimientos.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(movimientos);
    }
    @GetMapping
    public ResponseEntity<List<MovimientoQueyModel>> getAllMovimientos() {
        
        List<MovimientoQueyModel> movimientos = queryService.getAllMovimientos();
        return  ResponseEntity.ok(movimientos);
    }
    
    
//    command methods
    
    @PostMapping("/registrar")
    public ResponseEntity<?> registrarMovimiento(@RequestBody RegistrarMovimientoCommandModel request) {
        MovimientoValidator.validarRegistroMovimiento(request);
        RegistrarMovimientoResponse soapResponse = soapMovimientoClient.registrarMovimiento(request);
        return procesarRespuestaSoap(soapResponse);
    }

    private ResponseEntity<?> procesarRespuestaSoap(RegistrarMovimientoResponse soapResponse) {
        if (soapResponse == null || soapResponse.getTrama() == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("RESPUESTA_NULA", "La respuesta del servicio SOAP fue nula o incompleta."));
        }

        MovimientoValidator.ParsedResponse parsedResponse = 
            MovimientoValidator.parsearTrama(soapResponse.getTrama());

        if ("000".equals(parsedResponse.getCodigo())) {
            return ResponseEntity.ok(new SuccessResponse(
                parsedResponse.getCodigo(), 
                "Movimiento registrado exitosamente"
            ));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(
                        parsedResponse.getCodigo(), 
                        parsedResponse.getMensaje()
                    ));
        }
    }
}