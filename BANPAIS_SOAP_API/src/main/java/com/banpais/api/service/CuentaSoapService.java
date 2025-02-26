package com.banpais.api.service;

import com.banpais.api.exceptions.ClienteNotFoundException;
import com.banpais.api.exceptions.CuentaNotFoundException;
import com.banpais.api.infraestructure.entity.Cliente;
import com.banpais.api.infraestructure.entity.Cuenta;
import com.banpais.api.infraestructure.entity.TramaParametro;
import com.banpais.api.infraestructure.repository.ClienteRepository;
import com.banpais.api.infraestructure.repository.CuentaRepository;
import com.example.banco.*;
import java.time.LocalDateTime;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;

@Service
public class CuentaSoapService {

    private final CuentaRepository cuentaRepository;
    private final ClienteRepository clienteRepository;
    private final TramaService tramaService;
    private static final String CUENTA_ENTRADA = "CUENTA_IN";
    @Value("${banco.cuenta.oficial.numero}")
    private String bancoCuentaOficial;

    // Constantes para los nombres de los campos
    private static final String CAMPO_NUMERO_CUENTA = "NUMERO_CUENTA";
    private static final String CAMPO_FK_ID_CLIENTE = "FK_ID_CLIENTE";
    private static final String CAMPO_FECHA_APERTURA = "FECHA_APERTURA";
    private static final String CAMPO_ESTADO_CUENTA = "ESTADO_CUENTA";
    private static final String CAMPO_SALDO = "SALDO";

    public enum EstadoCuenta {
        ACT,
        INA,
        BLO
    }

    public CuentaSoapService(CuentaRepository cuentaRepository, ClienteRepository clienteRepository, TramaService tramaService) {
        this.cuentaRepository = cuentaRepository;
        this.clienteRepository = clienteRepository;
        this.tramaService = tramaService;
    }

    @Transactional
    public RegistrarCuentaResponse registrarCuenta(String trama) {
        List<TramaParametro> parametros = tramaService.getParametrosTrama(CUENTA_ENTRADA);
        Map<String, String> campos = tramaService.desparametrizarTrama(trama, parametros);

        campos.forEach((key, value) -> {
            System.out.println("Campo: " + key + ", Valor: [" + value + "], Longitud: " + value.length());
        });

        System.out.println("Parametros trama" + parametros);
        System.out.println("trama plana" + trama);
        System.out.println("campos" + campos.toString());

        Cliente cliente = clienteRepository.findById(campos.get(CAMPO_FK_ID_CLIENTE))
                .orElseThrow(() -> new ClienteNotFoundException("No se encontró el cliente con el ID: " + campos.get(CAMPO_FK_ID_CLIENTE)));

        Cuenta cuenta = new Cuenta();
        cuenta.setNumeroCuenta(UUID.randomUUID().toString().replace("-", "").substring(0, 16));
        cuenta.setCliente(campos.get(CAMPO_FK_ID_CLIENTE));
        cuenta.setFechaApertura(tramaService.parseLocalDateTime(campos.get(CAMPO_FECHA_APERTURA)));

        LocalDateTime fechaHoraApertura = tramaService.parseLocalDateTime(campos.get(CAMPO_FECHA_APERTURA));
        cuenta.setHoraApertura(fechaHoraApertura != null ? fechaHoraApertura.toLocalTime() : null);

        cuenta.setEstadoCuenta(EstadoCuenta.ACT.name());

        cuenta.setSaldo(new BigDecimal(campos.get(CAMPO_SALDO)).setScale(2, RoundingMode.DOWN));

        System.out.println("cuenta save" + cuenta.toString());
        cuentaRepository.save(cuenta);

        RegistrarCuentaResponse response = new RegistrarCuentaResponse();
        response.setCodigo("000");
        response.setMensaje("Cuenta creada exitosamente");
        return response;
    }

    @Transactional
    public ActualizarCuentaResponse actualizarCuenta(String trama) {
        List<TramaParametro> parametros = tramaService.getParametrosTrama(CUENTA_ENTRADA);
        Map<String, String> campos = tramaService.desparametrizarTrama(trama, parametros);

        campos.forEach((key, value) -> {
            System.out.println("Campo: " + key + ", Valor: [" + value + "], Longitud: " + value.length());
        });

        Cuenta cuenta = cuentaRepository.findById(campos.get(CAMPO_NUMERO_CUENTA))
                .orElseThrow(() -> new CuentaNotFoundException("No existe la cuenta con el número de cuenta: " + campos.get(CAMPO_NUMERO_CUENTA)));

        Cliente cliente = clienteRepository.findById(campos.get(CAMPO_FK_ID_CLIENTE))
                .orElseThrow(() -> new ClienteNotFoundException("No existe el cliente con ID: " + campos.get(CAMPO_FK_ID_CLIENTE)));

        cuenta.setCliente(campos.get(CAMPO_FK_ID_CLIENTE));
        cuenta.setFechaApertura(tramaService.parseLocalDateTime(campos.get(CAMPO_FECHA_APERTURA)));

        LocalDateTime fechaHoraApertura = tramaService.parseLocalDateTime(campos.get(CAMPO_FECHA_APERTURA));
        cuenta.setHoraApertura(fechaHoraApertura != null ? fechaHoraApertura.toLocalTime() : null);

        cuenta.setEstadoCuenta(EstadoCuenta.ACT.name());

        cuenta.setSaldo(new BigDecimal(campos.get(CAMPO_SALDO)).setScale(2, RoundingMode.DOWN));

        cuentaRepository.save(cuenta);

        ActualizarCuentaResponse response = new ActualizarCuentaResponse();
        response.setCodigo("000");
        response.setMensaje("Cuenta actualizada exitosamente");
        return response;
    }

    @Transactional
    public EliminarCuentaResponse eliminarCuenta(String trama) {
        List<TramaParametro> parametros = tramaService.getParametrosTrama(CUENTA_ENTRADA);
        Map<String, String> campos = tramaService.desparametrizarTrama(trama, parametros);
        
        String numeroCuenta = campos.get(CAMPO_NUMERO_CUENTA);
        System.out.println("comparativa "+numeroCuenta+bancoCuentaOficial);
        if (numeroCuenta.equals(bancoCuentaOficial)) {
            throw new RuntimeException("No se puede eliminar la cuenta oficial del banco");
        }

        Cuenta cuenta = cuentaRepository.findById(numeroCuenta)
                .orElseThrow(() -> new CuentaNotFoundException("No existe la cuenta con el número de cuenta: " + numeroCuenta));

        cuentaRepository.delete(cuenta);

        EliminarCuentaResponse response = new EliminarCuentaResponse();
        response.setCodigo("000");
        response.setMensaje("Cuenta eliminada exitosamente");
        return response;
    }
}
