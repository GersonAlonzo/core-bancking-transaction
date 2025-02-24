package com.banpais.api.command.config.client;

import com.banpais.api.command.config.interfaces.ISoapClienteOperations;
import com.banpais.api.command.model.RegistrarClienteCommandModel;
import com.banpais.api.infraestructure.repository.TramaParametroRepository;
import com.banpais.soap.client.ActualizarClienteRequest;
import com.banpais.soap.client.ActualizarClienteResponse;
import com.banpais.soap.client.BancoPort;
import com.banpais.soap.client.EliminarClienteRequest;
import com.banpais.soap.client.EliminarClienteResponse;
import com.banpais.soap.client.RegistrarClienteRequest;
import com.banpais.soap.client.RegistrarClienteResponse;

import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class SoapClienteClient extends AbstractSoapClient implements ISoapClienteOperations {

    @Autowired
    public SoapClienteClient(BancoPort bancoPort, TramaParametroRepository tramaParametroRepository,
            @Value("${soap.username}") String username,
            @Value("${soap.password}") String password) {
        super(bancoPort, tramaParametroRepository, "CLIENTE_IN",username, password);
    }

    @Override
    public RegistrarClienteResponse registrarCliente(RegistrarClienteCommandModel request) {
        Map<String, String> valores = new HashMap<>();
        valores.put("ID", UUID.randomUUID().toString());
        valores.put("NOMBRE", request.getNombre());
        valores.put("IDENTIFICACION", request.getIdentificacion());
        valores.put("TIPO_IDENTIFICACION", request.getTipoIdentificacion());
        valores.put("FECHA_NACIMIENTO",
                request.getFechaNacimiento().format(DateTimeFormatter.ofPattern("yyyyMMdd")));

        String trama = construirTrama(valores);

        return ejecutarOperacionSoap(
                () -> {
                    RegistrarClienteRequest soapRequest = new RegistrarClienteRequest();
                    soapRequest.setTrama(trama);
                    return bancoPort.registrarCliente(soapRequest);
                },
                "registro de cliente"
        );
    }

    @Override
    public ActualizarClienteResponse actualizarCliente(RegistrarClienteCommandModel request) {
        Map<String, String> valores = new HashMap<>();
        valores.put("ID", request.getId());
        valores.put("NOMBRE", request.getNombre());
        valores.put("IDENTIFICACION", request.getIdentificacion());
        valores.put("TIPO_IDENTIFICACION", request.getTipoIdentificacion());
        valores.put("FECHA_NACIMIENTO",
                request.getFechaNacimiento().format(DateTimeFormatter.ofPattern("yyyyMMdd")));

        String trama = construirTrama(valores);

        return ejecutarOperacionSoap(
                () -> {
                    ActualizarClienteRequest soapRequest = new ActualizarClienteRequest();
                    soapRequest.setTrama(trama);
                    return bancoPort.actualizarCliente(soapRequest);
                },
                "actualización de cliente"
        );
    }

    @Override
    public EliminarClienteResponse eliminarCliente(String clienteId) {
        Map<String, String> valores = new HashMap<>();
        valores.put("ID", clienteId);

        String trama = construirTrama(valores);

        return ejecutarOperacionSoap(
                () -> {
                    EliminarClienteRequest soapRequest = new EliminarClienteRequest();
                    soapRequest.setTrama(trama);
                    return bancoPort.eliminarCliente(soapRequest);
                },
                "eliminación de cliente"
        );
    }
}
