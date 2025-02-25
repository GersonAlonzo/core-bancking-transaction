// TipoMovimientoContract.java
package com.banpais.api.query.contract;

import com.banpais.api.infraestructure.entity.TipoMovimiento;
import com.banpais.api.infraestructure.mapper.Mapper;
import com.banpais.api.infraestructure.repository.TipoMovimientoRepository;
import com.banpais.api.query.model.TipoMovimientoQueryModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class TipoMovimientoContract {

    @Autowired
    private TipoMovimientoRepository tipoMovimientoRepository;

    public Optional<TipoMovimientoQueryModel> getByCodigo(String codigo) {
        Optional<TipoMovimiento> tipoMovimientoOptional = tipoMovimientoRepository.findByCodigo(codigo);
        return tipoMovimientoOptional.map(tipoMovimiento -> Mapper.getInstance().map(tipoMovimiento, TipoMovimientoQueryModel.class));
    }

    public List<TipoMovimientoQueryModel> getAll() {
        List<TipoMovimiento> tipoMovimientos = tipoMovimientoRepository.findAll();
        return tipoMovimientos.stream()
                .map(tipoMovimiento -> Mapper.getInstance().map(tipoMovimiento, TipoMovimientoQueryModel.class))
                .collect(Collectors.toList());
    }

    // Método para manejar la excepción (opcional)
//    public TipoMovimientoQueryModel getByCodigoOrThrow(String codigo) {
//        return getByCodigo(codigo)
//            .orElseThrow(() -> new TipoMovimientoNotFoundException("Tipo de movimiento con código " + codigo + " no encontrado"));
//    }
}