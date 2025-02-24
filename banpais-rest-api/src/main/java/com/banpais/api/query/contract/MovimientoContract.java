package com.banpais.api.query.contract;

import com.banpais.api.infraestructure.entity.Movimiento;
import com.banpais.api.infraestructure.entity.TipoMovimiento;
import com.banpais.api.infraestructure.repository.MovimientoRepository;
import com.banpais.api.infraestructure.repository.TipoMovimientoRepository;
import com.banpais.api.query.model.MovimientoQueyModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class MovimientoContract {

    @Autowired
    private MovimientoRepository movimientoRepository;

    @Autowired
    private TipoMovimientoRepository tipoMovimientoRepository;

    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    public List<MovimientoQueyModel> getMovimientosByCuenta(String numeroCuenta) {
        return movimientoRepository.findByCuentaOrigenOrCuentaDestino(numeroCuenta)
                .stream()
                .map(movimiento -> {
                    MovimientoQueyModel movimientoQueyModel = new MovimientoQueyModel();
                    movimientoQueyModel.setCuentaOrigen(movimiento.getCuentaOrigen().getNumeroCuenta());
                    movimientoQueyModel.setCuentaDestino(movimiento.getCuentaDestino().getNumeroCuenta());
                    movimientoQueyModel.setFechaMovimiento(movimiento.getFechaMovimiento().format(dateFormatter));
                    movimientoQueyModel.setMonto(movimiento.getMonto());
                    movimientoQueyModel.setNumeroReferencia(movimiento.getNumeroReferencia());

                    TipoMovimiento tipoMovimiento = movimiento.getTipoMovimiento();
                    if (tipoMovimiento != null) {
                        movimientoQueyModel.setTipoMovimiento(tipoMovimiento.getDescripcion()); // O el código, según prefieras
                    } else {
                        movimientoQueyModel.setTipoMovimiento("N/A"); // Manejo si no hay tipo
                    }
                    return movimientoQueyModel;
                })
                .collect(Collectors.toList());
    }
}