package com.banpais.api.command.config.client;

import com.banpais.api.command.model.RegistrarMovimientoCommandModel;
import com.banpais.api.infraestructure.repository.TramaParametroRepository;
import com.banpais.soap.client.BancoPort;
import com.banpais.soap.client.RegistrarMovimientoRequest;
import com.banpais.soap.client.RegistrarMovimientoResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class SoapMovimientoClient extends AbstractSoapClient {

    @Autowired
    public SoapMovimientoClient(
        BancoPort bancoPort, 
        TramaParametroRepository tramaParametroRepository,
        @Value("${soap.username}") String username,
        @Value("${soap.password}") String password
    ) {
        super(bancoPort, tramaParametroRepository, "MOVIMIENTO_IN", username, password);
    }

    public RegistrarMovimientoResponse registrarMovimiento(RegistrarMovimientoCommandModel request) {
        Map<String, String> valores = new HashMap<>();
        
        // Mapear los campos según la configuración de la trama
        valores.put("CUENTA_ORIGEN", request.getCuentaOrigen());
        valores.put("CUENTA_DESTINO", request.getCuentaDestino());
        valores.put("FK_CODIGO_MOVIMIENTO", request.getTipoMovimiento());
        valores.put("MONTO", request.getMonto().toString());
        valores.put("FECHA_MOVIMIENTO", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")));

        String trama = construirTrama(valores);

        return ejecutarOperacionSoap(
            () -> {
                RegistrarMovimientoRequest soapRequest = new RegistrarMovimientoRequest();
                soapRequest.setTrama(trama);
                return bancoPort.registrarMovimiento(soapRequest);
            },
            "registro de movimiento"
        );
    }
}