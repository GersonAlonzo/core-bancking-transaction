package com.banpais.api.command.config.client;


import com.banpais.api.config.SoapFaultException;
import com.banpais.api.infraestructure.entity.TramaParametro;
import com.banpais.api.infraestructure.repository.TramaParametroRepository;
import com.banpais.soap.client.BancoPort;
import static java.lang.StrictMath.log;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.apache.cxf.binding.soap.SoapFault;

@Slf4j
public abstract class AbstractSoapClient {
    protected final BancoPort bancoPort;
    protected final TramaParametroRepository tramaParametroRepository;
    protected final String nombreTrama;

    protected AbstractSoapClient(BancoPort bancoPort, 
                               TramaParametroRepository tramaParametroRepository,
                               String nombreTrama) {
        this.bancoPort = bancoPort;
        this.tramaParametroRepository = tramaParametroRepository;
        this.nombreTrama = nombreTrama;
    }

    protected String construirTrama(Map<String, String> valores) {
        List<TramaParametro> parametros = tramaParametroRepository.findByNombreTrama(nombreTrama);
        Map<String, TramaParametro> parametroMap = parametros.stream()
                .collect(Collectors.toMap(TramaParametro::getNombreCampo, p -> p));

        StringBuilder trama = new StringBuilder(" ".repeat(calcularLongitudTotal(parametros)));

        valores.forEach((nombreCampo, valor) -> {
            TramaParametro parametro = parametroMap.get(nombreCampo);
            if (parametro != null) {
                insertarCampo(trama, valor, parametro);
            }
        });

        log.debug("Trama generada: [{}]", trama);
        return trama.toString();
    }

    protected void insertarCampo(StringBuilder trama, String valor, TramaParametro parametro) {
        String valorFormateado = formatearValorSegunTipo(valor, parametro);
        trama.replace(
            parametro.getPosicionInicio() - 1,
            parametro.getPosicionInicio() - 1 + parametro.getLongitud(),
            valorFormateado
        );
    }

    protected String formatearValorSegunTipo(String valor, TramaParametro parametro) {
        if (valor == null) valor = "";
        
        // Según el tipo de campo aplicamos diferente formateo
        switch (parametro.getNombreCampo()) {
            case "MONTO":
                return String.format("%0" + parametro.getLongitud() + ".2f", 
                    new BigDecimal(valor));
            case "FECHA_MOVIMIENTO":
            case "FECHA_APERTURA":
                return String.format("%-" + parametro.getLongitud() + "s", valor);
            default:
                return String.format("%-" + parametro.getLongitud() + "s", valor)
                    .substring(0, parametro.getLongitud());
        }
    }

    protected int calcularLongitudTotal(List<TramaParametro> parametros) {
        return parametros.stream()
            .mapToInt(p -> p.getPosicionInicio() + p.getLongitud() - 1)
            .max()
            .orElse(0);
    }

    protected <T> T ejecutarOperacionSoap(Supplier<T> operacion, String descripcionOperacion) {
        try {
            return operacion.get();
        } catch (SoapFault fault) {
            String faultCode = fault.getFaultCode() != null ? 
                             fault.getFaultCode().getLocalPart() : "Unknown";
            String faultString = fault.getReason() != null ? 
                               fault.getReason() : "Unknown SOAP Fault";
            throw new SoapFaultException(
                String.format("Error SOAP en %s: %s - %s", 
                    descripcionOperacion, faultCode, faultString),
                fault
            );
        } catch (Exception e) {
            throw new RuntimeException(
                String.format("Error al invocar %s: %s", 
                    descripcionOperacion, e.getMessage()),
                e
            );
        }
    }
}