// CuentaRepository.java
package com.banpais.api.infraestructure.repository;

import com.banpais.api.infraestructure.entity.Cuenta;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface CuentaRepository extends JpaRepository<Cuenta, String> {
    Optional<Cuenta> findByNumeroCuenta(String numeroCuenta);

    // Consulta JPQL corregida para el cambio a String en cliente
    @Query("SELECT c FROM Cuenta c WHERE c.cliente = :clienteId")
    List<Cuenta> findCuentasByClienteId(@Param("clienteId") String clienteId);
}