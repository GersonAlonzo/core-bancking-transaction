package com.banpais.api.command.config.client;


import com.banpais.api.command.config.client.AbstractSoapClient;
import com.banpais.api.command.config.interfaces.ISoapCuentaOperations;
import com.banpais.api.command.model.RegistrarCuentaCommandModel;
import com.banpais.api.infraestructure.repository.TramaParametroRepository;
import com.banpais.soap.client.BancoPort;
import com.banpais.soap.client.EliminarCuentaRequest;
import com.banpais.soap.client.EliminarCuentaResponse;
import com.banpais.soap.client.RegistrarCuentaRequest;
import com.banpais.soap.client.RegistrarCuentaResponse;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class SoapCuentaClient extends AbstractSoapClient implements ISoapCuentaOperations {
    
    @Autowired
    public SoapCuentaClient(BancoPort bancoPort, TramaParametroRepository tramaParametroRepository) {
        super(bancoPort, tramaParametroRepository, "CUENTA_IN");
    }

    @Override
    public RegistrarCuentaResponse registrarCuenta(RegistrarCuentaCommandModel request) {
        LocalDateTime ahora = LocalDateTime.now();
        
        Map<String, String> valores = new HashMap<>();
        valores.put("NUMERO_CUENTA", request.getNumeroCuenta());
        valores.put("FK_ID_CLIENTE", request.getClienteId());
        valores.put("FECHA_APERTURA", 
            ahora.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")));
        valores.put("HORA_APERTURA", 
            ahora.format(DateTimeFormatter.ofPattern("HHmmss")));
        valores.put("ESTADO_CUENTA", request.getEstadoCuenta());
        valores.put("SALDO", request.getSaldo().toString());

        String trama = construirTrama(valores);
        
        return ejecutarOperacionSoap(
            () -> {
                RegistrarCuentaRequest soapRequest = new RegistrarCuentaRequest();
                soapRequest.setTrama(trama);
                return bancoPort.registrarCuenta(soapRequest);
            },
            "registro de cuenta"
        );
    }

    @Override
    public EliminarCuentaResponse eliminarCuenta(String numeroCuenta) {
        Map<String, String> valores = new HashMap<>();
        valores.put("NUMERO_CUENTA", numeroCuenta);

        String trama = construirTrama(valores);
        
        return ejecutarOperacionSoap(
            () -> {
                EliminarCuentaRequest soapRequest = new EliminarCuentaRequest();
                soapRequest.setTrama(trama);
                return bancoPort.eliminarCuenta(soapRequest);
            },
            "eliminación de cuenta"
        );
    }
}