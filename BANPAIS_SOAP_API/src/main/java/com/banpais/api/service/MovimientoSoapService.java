package com.banpais.api.service;

import com.banpais.api.exceptions.CuentaNotFoundException;
import com.banpais.api.exceptions.SaldoInsuficienteException;
import com.banpais.api.exceptions.TipoMovimientoInvalidoException;
import com.banpais.api.infraestructure.entity.Cuenta;
import com.banpais.api.infraestructure.entity.Movimiento;
import com.banpais.api.infraestructure.entity.TipoMovimiento;
import com.banpais.api.infraestructure.entity.TramaParametro;
import com.banpais.api.infraestructure.repository.CuentaRepository;
import com.banpais.api.infraestructure.repository.MovimientoRepository;
import com.banpais.api.infraestructure.repository.TipoMovimientoRepository;
import com.example.banco.RegistrarMovimientoResponse;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Value;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MovimientoSoapService {

    private final MovimientoRepository movimientoRepository;
    private final CuentaRepository cuentaRepository;
    private final TipoMovimientoRepository tipoMovimientoRepository;
    private final TramaService tramaService;

    private static final String MOVIMIENTO_ENTRADA = "MOVIMIENTO_IN";
    private static final String MOVIMIENTO_SALIDA = "MOVIMIENTO_OUT";

    // Constantes para los nombres de los campos de entrada
    private static final String CAMPO_CUENTA_ORIGEN = "CUENTA_ORIGEN";
    private static final String CAMPO_CUENTA_DESTINO = "CUENTA_DESTINO";
    private static final String CAMPO_TIPO_MOVIMIENTO = "FK_CODIGO_MOVIMIENTO";
    private static final String CAMPO_MONTO = "MONTO";
    private static final String CAMPO_FECHA_MOVIMIENTO = "FECHA_MOVIMIENTO";

    // Constantes para los nombres de los campos de salida
    private static final String CAMPO_CODIGO_RESULTADO = "CODIGO_RESULTADO";
    private static final String CAMPO_DESCRIPCION = "DESCRIPCION";
    private static final String CAMPO_NUMERO_REFERENCIA = "NUMERO_REFERENCIA";
    
    // Constantes para los códigos de tipo de movimiento
    private static final String CODIGO_DEPOSITO = "DEPOSITO";
    private static final String CODIGO_RETIRO = "RETIRO";
    private static final String CODIGO_TRANSFER = "TRANSFER";
    
    @Value("${banco.cuenta.oficial.numero}") 
    private String bancoCuentaOficial;

    public MovimientoSoapService(MovimientoRepository movimientoRepository,
                                  CuentaRepository cuentaRepository,
                                  TipoMovimientoRepository tipoMovimientoRepository,
                                  TramaService tramaService) {
        this.movimientoRepository = movimientoRepository;
        this.cuentaRepository = cuentaRepository;
        this.tipoMovimientoRepository = tipoMovimientoRepository;
        this.tramaService = tramaService;
    }

    @Transactional
    public RegistrarMovimientoResponse registrarMovimiento(String trama) {
        List<TramaParametro> parametrosEntrada = tramaService.getParametrosTrama(MOVIMIENTO_ENTRADA);
        Map<String, String> camposEntrada = tramaService.desparametrizarTrama(trama, parametrosEntrada);

        camposEntrada.forEach((key, value) -> {
            System.out.println("Campo: " + key + ", Valor: [" + value + "], Longitud: " + value.length());
        });

        // Obtener y validar tipo de movimiento primero
        TipoMovimiento tipoMovimiento = tipoMovimientoRepository.findByCodigo(camposEntrada.get(CAMPO_TIPO_MOVIMIENTO))
                .orElseThrow(() -> new TipoMovimientoInvalidoException("Tipo de movimiento inválido: " + camposEntrada.get(CAMPO_TIPO_MOVIMIENTO)));

        // Parsear el monto
        BigDecimal monto = new BigDecimal(camposEntrada.get(CAMPO_MONTO)).setScale(2, RoundingMode.DOWN);

        // Obtener y validar cuentas según el tipo de movimiento
        Cuenta cuentaOrigen = null;
        Cuenta cuentaDestino = null;
        
        // Manejar depósitos de forma especial
        if (CODIGO_DEPOSITO.equals(tipoMovimiento.getCodigo())) {
            // Para depósitos, la cuenta origen es la cuenta oficial del banco
            cuentaOrigen = cuentaRepository.findById(bancoCuentaOficial)
                    .orElseThrow(() -> new RuntimeException("Cuenta oficial del banco no encontrada: " + bancoCuentaOficial));
                    
            // La cuenta destino es obligatoria para depósitos
            if (camposEntrada.get(CAMPO_CUENTA_DESTINO) == null || camposEntrada.get(CAMPO_CUENTA_DESTINO).trim().isEmpty()) {
                throw new IllegalArgumentException("Para un depósito, se requiere una cuenta destino");
            }
            
            cuentaDestino = cuentaRepository.findById(camposEntrada.get(CAMPO_CUENTA_DESTINO))
                    .orElseThrow(() -> new CuentaNotFoundException("Cuenta destino no encontrada: " + camposEntrada.get(CAMPO_CUENTA_DESTINO)));
        } else {
            // Para otros tipos de movimiento, obtenemos la cuenta origen normalmente
            cuentaOrigen = cuentaRepository.findById(camposEntrada.get(CAMPO_CUENTA_ORIGEN))
                    .orElseThrow(() -> new CuentaNotFoundException("Cuenta origen no encontrada: " + camposEntrada.get(CAMPO_CUENTA_ORIGEN)));
            
            // Para retiros, podríamos usar la cuenta del banco como destino
            if (CODIGO_RETIRO.equals(tipoMovimiento.getCodigo())) {
                cuentaDestino = cuentaRepository.findById(bancoCuentaOficial)
                        .orElseThrow(() -> new RuntimeException("Cuenta oficial del banco no encontrada: " + bancoCuentaOficial));
            } 
            // Para transferencias, necesitamos una cuenta destino válida
            else if (CODIGO_TRANSFER.equals(tipoMovimiento.getCodigo())) {
                if (camposEntrada.get(CAMPO_CUENTA_DESTINO) == null || camposEntrada.get(CAMPO_CUENTA_DESTINO).trim().isEmpty()) {
                    throw new IllegalArgumentException("Para una transferencia, se requiere una cuenta destino");
                }
                
                cuentaDestino = cuentaRepository.findById(camposEntrada.get(CAMPO_CUENTA_DESTINO))
                        .orElseThrow(() -> new CuentaNotFoundException("Cuenta destino no encontrada: " + camposEntrada.get(CAMPO_CUENTA_DESTINO)));
            }
        }

        // Validar saldo suficiente para retiros y transferencias
        if ((CODIGO_RETIRO.equals(tipoMovimiento.getCodigo()) || CODIGO_TRANSFER.equals(tipoMovimiento.getCodigo())) &&
            cuentaOrigen.getSaldo().compareTo(monto) < 0) {
            throw new SaldoInsuficienteException("Saldo insuficiente en la cuenta origen: " + cuentaOrigen.getNumeroCuenta());
        }

        // Crear el movimiento
        Movimiento movimiento = new Movimiento();
        movimiento.setCuentaOrigen(cuentaOrigen);
        movimiento.setNumeroReferencia(UUID.randomUUID().toString());
        movimiento.setCuentaDestino(cuentaDestino);
        movimiento.setFechaMovimiento(tramaService.parseLocalDateTime(camposEntrada.get(CAMPO_FECHA_MOVIMIENTO)));
        movimiento.setHoraMovimiento(tramaService.parseLocalDateTime(camposEntrada.get(CAMPO_FECHA_MOVIMIENTO)) != null ? 
            tramaService.parseLocalDateTime(camposEntrada.get(CAMPO_FECHA_MOVIMIENTO)).toLocalTime() : null);
        movimiento.setTipoMovimiento(tipoMovimiento);
        movimiento.setMonto(monto);

        // Actualizar saldos
        if (CODIGO_RETIRO.equals(tipoMovimiento.getCodigo())) {
            cuentaOrigen.setSaldo(cuentaOrigen.getSaldo().subtract(monto));
        } else if (CODIGO_DEPOSITO.equals(tipoMovimiento.getCodigo())) {
            cuentaDestino.setSaldo(cuentaDestino.getSaldo().add(monto));
        } else if (CODIGO_TRANSFER.equals(tipoMovimiento.getCodigo())) {
            cuentaOrigen.setSaldo(cuentaOrigen.getSaldo().subtract(monto));
            cuentaDestino.setSaldo(cuentaDestino.getSaldo().add(monto));
        }

        try {
            // Guardar las cuentas actualizadas
            cuentaRepository.save(cuentaOrigen);
            if (cuentaDestino != null) {
                cuentaRepository.save(cuentaDestino);
            }
        } catch (Exception ex) {
            System.out.println("Error al guardar las cuentas: " + ex.getMessage());
            throw ex; // Re-lanzar para que la transacción se revierta
        }

        // Guardar el movimiento
        movimiento = movimientoRepository.save(movimiento);

        // --- Construcción de la respuesta ---
        List<TramaParametro> parametrosSalida = tramaService.getParametrosTrama(MOVIMIENTO_SALIDA);
        Map<String, String> camposSalida = new HashMap<>();

        // Llenar el mapa de salida utilizando directamente las constantes y los valores
        camposSalida.put(CAMPO_CODIGO_RESULTADO, "000");
        camposSalida.put(CAMPO_DESCRIPCION, "Operación exitosa");
        camposSalida.put(CAMPO_NUMERO_REFERENCIA, movimiento.getNumeroReferencia());

        String tramaRespuesta = tramaService.parametrizarTrama(camposSalida, parametrosSalida);
        System.out.println("trama respuesta: " + tramaRespuesta);

        RegistrarMovimientoResponse response = new RegistrarMovimientoResponse();
        response.setTrama(tramaRespuesta);
        return response;
    }
}