package com.banpais.api.service;

import com.banpais.api.exceptions.TramaInvalidaException;
import com.banpais.api.infraestructure.entity.TramaParametro;
import com.banpais.api.infraestructure.repository.TramaParametroRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class TramaService {
    private static final Logger log = LoggerFactory.getLogger(TramaService.class);
    private final TramaParametroRepository tramaParametroRepository;

    // Constantes para formatos de fecha y hora
    private static final String DATE_FORMAT = "yyyyMMdd";
    private static final String TIME_FORMAT = "HHmmss";
    private static final String DATETIME_FORMAT = "yyyyMMddHHmmss";

    public TramaService(TramaParametroRepository tramaParametroRepository) {
        this.tramaParametroRepository = tramaParametroRepository;
    }

    public List<TramaParametro> getParametrosTrama(String nombreTrama) {
        List<TramaParametro> parametros = tramaParametroRepository.findByNombreTrama(nombreTrama);
        if (parametros.isEmpty()) {
            throw new TramaInvalidaException("No se encontraron parámetros para la trama: " + nombreTrama);
        }
        return parametros;
    }

    private int calcularLongitudEsperada(List<TramaParametro> parametros) {
        return parametros.stream()
            .mapToInt(p -> p.getPosicionInicio() + p.getLongitud() - 1)
            .max()
            .orElse(0);
    }

    public Map<String, String> desparametrizarTrama(String tramaOriginal, List<TramaParametro> parametros) {
        if (tramaOriginal == null || tramaOriginal.isEmpty()) {
            throw new TramaInvalidaException("La trama no puede ser nula o vacía");
        }

        // Validar longitud total
        int longitudEsperada = calcularLongitudEsperada(parametros);
        if (tramaOriginal.length() < longitudEsperada) {
            throw new TramaInvalidaException(
                String.format("Longitud de trama insuficiente. Esperada: %d, Recibida: %d", 
                            longitudEsperada, tramaOriginal.length())
            );
        }

        Map<String, String> campos = new HashMap<>();
        log.debug("Procesando trama: [{}]", tramaOriginal);

        for (TramaParametro parametro : parametros) {
            try {
                int inicio = parametro.getPosicionInicio() - 1;
                int fin = inicio + parametro.getLongitud();
                String valor = tramaOriginal.substring(inicio, fin);
                campos.put(parametro.getNombreCampo(), valor.trim());
                log.debug("Campo {} extraído: [{}]", parametro.getNombreCampo(), valor);
            } catch (Exception e) {
                throw new TramaInvalidaException(
                    String.format("Error procesando campo %s: %s", 
                                parametro.getNombreCampo(), e.getMessage()), e
                );
            }
        }
        return campos;
    }

    public String parametrizarTrama(Map<String, String> campos, List<TramaParametro> parametros) {
        int longitudTotal = calcularLongitudEsperada(parametros);
        StringBuilder trama = new StringBuilder(" ".repeat(longitudTotal));

        for (TramaParametro parametro : parametros) {
            try {
                String valor = campos.getOrDefault(parametro.getNombreCampo(), "");
                String valorFormateado = String.format("%-" + parametro.getLongitud() + "s", valor)
                    .substring(0, parametro.getLongitud());
                int inicio = parametro.getPosicionInicio() - 1;
                trama.replace(inicio, inicio + valorFormateado.length(), valorFormateado);
                
                log.debug("Campo {} formateado: [{}]", parametro.getNombreCampo(), valorFormateado);
            } catch (Exception e) {
                throw new TramaInvalidaException(
                    String.format("Error formateando campo %s: %s", 
                                parametro.getNombreCampo(), e.getMessage()), e
                );
            }
        }
        
        String tramaFinal = trama.toString();
        log.debug("Trama generada: [{}]", tramaFinal);
        return tramaFinal;
    }

    // Métodos de conversión de tipos
    public String formatBigDecimal(BigDecimal valor, int longitud) {
        if (valor == null) {
            return " ".repeat(longitud);
        }
        try {
            String valorSinDecimales = valor.multiply(new BigDecimal("100"))
                .setScale(0, RoundingMode.UNNECESSARY)
                .toPlainString();
            return String.format("%" + longitud + "s", valorSinDecimales).replace(' ', '0');
        } catch (Exception e) {
            throw new TramaInvalidaException("Error formateando BigDecimal: " + valor, e);
        }
    }

    public BigDecimal parseBigDecimal(String valor) {
        if (valor == null || valor.trim().isEmpty() || valor.trim().matches("^0+$")) {
            return BigDecimal.ZERO;
        }
        try {
            return new BigDecimal(valor.trim()).divide(new BigDecimal("100"), 2, RoundingMode.UNNECESSARY);
        } catch (NumberFormatException e) {
            throw new TramaInvalidaException("Formato de BigDecimal inválido: " + valor, e);
        }
    }

    public String formatLocalDate(LocalDate valor, int longitud) {
        if (valor == null) {
            return " ".repeat(longitud);
        }
        return valor.format(DateTimeFormatter.ofPattern(DATE_FORMAT));
    }

    public LocalDate parseLocalDate(String valor) {
        if (valor == null || valor.trim().isEmpty()) {
            return null;
        }
        try {
            return LocalDate.parse(valor.trim(), DateTimeFormatter.ofPattern(DATE_FORMAT));
        } catch (DateTimeParseException e) {
            throw new TramaInvalidaException("Formato de fecha inválido: " + valor, e);
        }
    }

    public String formatLocalTime(LocalTime valor, int longitud) {
        if (valor == null) {
            return " ".repeat(longitud);
        }
        return valor.format(DateTimeFormatter.ofPattern(TIME_FORMAT));
    }

    public LocalTime parseLocalTime(String valor) {
        if (valor == null || valor.trim().isEmpty()) {
            return null;
        }
        try {
            return LocalTime.parse(valor.trim(), DateTimeFormatter.ofPattern(TIME_FORMAT));
        } catch (DateTimeParseException e) {
            throw new TramaInvalidaException("Formato de hora inválido: " + valor, e);
        }
    }

    public String formatLocalDateTime(LocalDateTime valor, int longitud) {
        if (valor == null) {
            return " ".repeat(longitud);
        }
        return valor.format(DateTimeFormatter.ofPattern(DATETIME_FORMAT));
    }

    public LocalDateTime parseLocalDateTime(String valor) {
        if (valor == null || valor.trim().isEmpty()) {
            return null;
        }
        try {
            return LocalDateTime.parse(valor.trim(), DateTimeFormatter.ofPattern(DATETIME_FORMAT));
        } catch (DateTimeParseException e) {
            throw new TramaInvalidaException("Formato de fecha/hora inválido: " + valor, e);
        }
    }
}