package com.banpais.api.command.config.client;

import com.banpais.api.command.model.RegistrarMovimientoCommandModel;
import com.banpais.api.config.SoapFaultException;
import com.banpais.api.infraestructure.entity.TramaParametro;
import com.banpais.api.infraestructure.repository.TramaParametroRepository;
import com.banpais.soap.client.BancoPort;
import com.banpais.soap.client.RegistrarMovimientoResponse;
import java.time.LocalDateTime;
import org.apache.cxf.binding.soap.SoapFault;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class SoapMovimientoClient {

    private final BancoPort bancoPort;
    private final TramaParametroRepository tramaParametroRepository;

    @Autowired
    public SoapMovimientoClient(BancoPort bancoPort, TramaParametroRepository tramaParametroRepository) {
        this.bancoPort = bancoPort;
        this.tramaParametroRepository = tramaParametroRepository;
    }

    public RegistrarMovimientoResponse registrarMovimiento(RegistrarMovimientoCommandModel request) {
        try {
            // Obtener los parámetros de la trama de entrada
            List<TramaParametro> parametros = tramaParametroRepository.findByNombreTrama("MOVIMIENTO_IN");

            // Crear un mapa de parámetros para facilitar la búsqueda
            Map<String, TramaParametro> parametroMap = parametros.stream()
                    .collect(Collectors.toMap(TramaParametro::getNombreCampo, p -> p));

            // Preparar la trama según la configuración de la base de datos
            StringBuilder trama = new StringBuilder(" ".repeat(parametros.stream()
                    .mapToInt(p -> p.getPosicionInicio() + p.getLongitud() - 1)
                    .max()
                    .orElse(0)));

            // Formatear CUENTA_ORIGEN
            TramaParametro cuentaOrigenParam = parametroMap.get("CUENTA_ORIGEN");
            String cuentaOrigen = String.format("%-" + cuentaOrigenParam.getLongitud() + "s",
                    request.getCuentaOrigen()).substring(0, cuentaOrigenParam.getLongitud());
            trama.replace(cuentaOrigenParam.getPosicionInicio() - 1,
                    cuentaOrigenParam.getPosicionInicio() - 1 + cuentaOrigenParam.getLongitud(),
                    cuentaOrigen);

            // Formatear CUENTA_DESTINO
            TramaParametro cuentaDestinoParam = parametroMap.get("CUENTA_DESTINO");
            String cuentaDestino = String.format("%-" + cuentaDestinoParam.getLongitud() + "s",
                    request.getCuentaDestino()).substring(0, cuentaDestinoParam.getLongitud());
            trama.replace(cuentaDestinoParam.getPosicionInicio() - 1,
                    cuentaDestinoParam.getPosicionInicio() - 1 + cuentaDestinoParam.getLongitud(),
                    cuentaDestino);

            // Formatear FECHA_MOVIMIENTO
            TramaParametro fechaMovimientoParam = parametroMap.get("FECHA_MOVIMIENTO");
            String fechaBase = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")); // Aquí está el cambio
            String fechaMovimiento = String.format("%-" + fechaMovimientoParam.getLongitud() + "s", fechaBase)
                    .substring(0, fechaMovimientoParam.getLongitud());
            trama.replace(fechaMovimientoParam.getPosicionInicio() - 1,
                    fechaMovimientoParam.getPosicionInicio() - 1 + fechaMovimientoParam.getLongitud(),
                    fechaMovimiento);

            // Formatear FK_CODIGO_MOVIMIENTO
            TramaParametro tipoMovimientoParam = parametroMap.get("FK_CODIGO_MOVIMIENTO");
            String tipoMovimiento = String.format("%-" + tipoMovimientoParam.getLongitud() + "s",
                    request.getTipoMovimiento()).substring(0, tipoMovimientoParam.getLongitud());
            trama.replace(tipoMovimientoParam.getPosicionInicio() - 1,
                    tipoMovimientoParam.getPosicionInicio() - 1 + tipoMovimientoParam.getLongitud(),
                    tipoMovimiento);

            // Formatear MONTO
            TramaParametro montoParam = parametroMap.get("MONTO");
            String monto = String.format("%0" + montoParam.getLongitud() + ".2f", request.getMonto())
                    .substring(0, montoParam.getLongitud());
            trama.replace(montoParam.getPosicionInicio() - 1,
                    montoParam.getPosicionInicio() - 1 + montoParam.getLongitud(),
                    monto);

            // Preparar el request SOAP request request
            com.banpais.soap.client.RegistrarMovimientoRequest soapRequest = new com.banpais.soap.client.RegistrarMovimientoRequest();
            soapRequest.setTrama(trama.toString());

            // Debug para verificar la trama tramatrama
            System.out.println("Trama generada: [" + trama.toString() + "]");
            System.out.println("Longitud total: " + trama.length());

            return bancoPort.registrarMovimiento(soapRequest);
        } catch (SoapFault fault) {
            String faultCode = fault.getFaultCode() != null ? fault.getFaultCode().getLocalPart() : "Unknown";
            String faultString = fault.getReason() != null ? fault.getReason() : "Unknown SOAP Fault";
            throw new SoapFaultException("Error SOAP: " + faultCode + " - " + faultString, fault);
        } catch (Exception e) {
            System.out.println("error de soap "+e.toString());
            throw new RuntimeException("Error al invocar el servicio SOAP de registro de movimiento: " + e.getMessage(), e);
        }
    }
}
