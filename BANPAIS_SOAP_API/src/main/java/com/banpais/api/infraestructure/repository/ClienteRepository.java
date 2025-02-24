// ClienteRepository.java
package com.banpais.api.infraestructure.repository;


import com.banpais.api.infraestructure.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClienteRepository extends JpaRepository<Cliente, String> {
	Cliente findByIdentificacion(String identificacion);
}