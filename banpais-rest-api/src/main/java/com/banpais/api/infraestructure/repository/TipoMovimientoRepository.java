// TipoMovimientoRepository.java

package com.banpais.api.infraestructure.repository;

import com.banpais.api.infraestructure.entity.TipoMovimiento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TipoMovimientoRepository extends JpaRepository<TipoMovimiento, Integer> {
    Optional<TipoMovimiento> findByCodigo(String codigo);
}