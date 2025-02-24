// TramaParametroRepository.java
package com.banpais.api.infraestructure.repository;

import com.banpais.api.infraestructure.entity.TramaParametro;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TramaParametroRepository extends JpaRepository<TramaParametro, Integer> {
    List<TramaParametro> findByNombreTrama(String nombreTrama);
}