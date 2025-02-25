package com.banpais.api.infraestructure.repository;

import com.banpais.api.infraestructure.entity.Movimiento;
import com.banpais.api.query.model.MovimientoQueyModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MovimientoRepository extends JpaRepository<Movimiento, String> {
     @Query("SELECT m FROM Movimiento m WHERE m.cuentaOrigen.numeroCuenta = :numeroCuenta OR m.cuentaDestino.numeroCuenta = :numeroCuenta")
     List<Movimiento> findByCuentaOrigenOrCuentaDestino(@Param("numeroCuenta") String numeroCuenta);
     
     @Query("SELECT new com.banpais.api.query.model.MovimientoQueyModel(m.numeroReferencia, " +
           "m.cuentaOrigen.numeroCuenta, m.cuentaDestino.numeroCuenta, " +
           "m.fechaMovimiento, m.monto, m.tipoMovimiento.descripcion) " +
           "FROM Movimiento m")
    List<MovimientoQueyModel> findAllMovimientosAsDto();
}