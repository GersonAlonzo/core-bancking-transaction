package com.banpais.api.command.config.client;

import com.banpais.api.infraestructure.entity.TramaParametro;
import com.banpais.api.infraestructure.repository.TramaParametroRepository;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class TramaTransformacionService {

    private final TramaParametroRepository tramaParametroRepository;

    public TramaTransformacionService(TramaParametroRepository tramaParametroRepository) {
        this.tramaParametroRepository = tramaParametroRepository;
    }

    public String generarTrama(Map<String, Object> campos, String nombreTrama) {
        // Obtener los parámetros de la trama
        List<TramaParametro> parametros = tramaParametroRepository.findByNombreTrama(nombreTrama);
        
        // Crear un mapa de parámetros para facilitar la búsqueda
        Map<String, TramaParametro> parametroMap = parametros.stream()
            .collect(Collectors.toMap(TramaParametro::getNombreCampo, p -> p));

        // Preparar la trama según la configuración de la base de datos
        StringBuilder trama = new StringBuilder(" ".repeat(parametros.stream()
            .mapToInt(p -> p.getPosicionInicio() + p.getLongitud() - 1)
            .max()
            .orElse(0)));

        // Procesar cada campo
        for (Map.Entry<String, Object> entry : campos.entrySet()) {
            TramaParametro parametro = parametroMap.get(entry.getKey());
            if (parametro == null) {
                continue; // Saltar campos no definidos
            }

            String valorFormateado = formatearCampo(entry.getValue(), parametro);
            
            trama.replace(parametro.getPosicionInicio() - 1, 
                          parametro.getPosicionInicio() - 1 + parametro.getLongitud(), 
                          valorFormateado);
        }

        return trama.toString();
    }

    private String formatearCampo(Object valor, TramaParametro parametro) {
        if (valor == null) {
            return " ".repeat(parametro.getLongitud());
        }

        String valorStr;
        if (valor instanceof java.time.LocalDateTime) {
            valorStr = ((java.time.LocalDateTime) valor).format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        } else if (valor instanceof java.math.BigDecimal) {
            valorStr = String.format("%0" + parametro.getLongitud() + ".2f", (java.math.BigDecimal) valor);
        } else {
            valorStr = valor.toString();
        }

        // Truncar o rellenar según la longitud
        if (valorStr.length() > parametro.getLongitud()) {
            return valorStr.substring(0, parametro.getLongitud());
        } else {
            // Rellenar con espacios a la derecha para campos de texto
            if (valor instanceof String || valor instanceof java.time.LocalDateTime) {
                return String.format("%-" + parametro.getLongitud() + "s", valorStr);
            } 
            // Rellenar con ceros a la izquierda para números
            else {
                return String.format("%0" + parametro.getLongitud() + "d", 
                    Long.parseLong(valorStr.replace(".", "")));
            }
        }
    }
}