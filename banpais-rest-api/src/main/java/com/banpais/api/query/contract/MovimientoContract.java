package com.banpais.api.query.contract;

import com.banpais.api.infraestructure.entity.Movimiento;
import com.banpais.api.infraestructure.mapper.Mapper;
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
    
    
    
    public List<MovimientoQueyModel> getMovimientosByCuenta(String numeroCuenta) {
        return movimientoRepository.findByCuentaOrigenOrCuentaDestino(numeroCuenta)
                .stream()
                .map(movimiento -> Mapper.getInstance().map(movimiento, MovimientoQueyModel.class))
                .collect(Collectors.toList());
    }
    
    public List<MovimientoQueyModel> getAllMovimientos() {
        return movimientoRepository.findAllMovimientosAsDto();
    }
}