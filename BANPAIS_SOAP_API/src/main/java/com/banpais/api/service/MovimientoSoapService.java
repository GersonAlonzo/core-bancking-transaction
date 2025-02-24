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
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
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

        // Obtener y validar cuentas
        Cuenta cuentaOrigen = cuentaRepository.findById(camposEntrada.get(CAMPO_CUENTA_ORIGEN))
                .orElseThrow(() -> new CuentaNotFoundException("Cuenta origen no encontrada: " + camposEntrada.get(CAMPO_CUENTA_ORIGEN)));
        Cuenta cuentaDestino = null;
        //La cuenta destino es opcional

        if(camposEntrada.get(CAMPO_CUENTA_DESTINO) != null && !camposEntrada.get(CAMPO_CUENTA_DESTINO).trim().isEmpty()){
            cuentaDestino = cuentaRepository.findById(camposEntrada.get(CAMPO_CUENTA_DESTINO))
                    .orElseThrow(() -> new CuentaNotFoundException("Cuenta destino no encontrada: " + camposEntrada.get(CAMPO_CUENTA_DESTINO)));
        }

        // Obtener y validar tipo de movimiento.
        TipoMovimiento tipoMovimiento = tipoMovimientoRepository.findByCodigo(camposEntrada.get(CAMPO_TIPO_MOVIMIENTO))
                .orElseThrow(() -> new TipoMovimientoInvalidoException("Tipo de movimiento inválido: " + camposEntrada.get(CAMPO_TIPO_MOVIMIENTO)));

        // Parsear el monto
        BigDecimal monto = new BigDecimal(camposEntrada.get(CAMPO_MONTO)).setScale(2, RoundingMode.DOWN);

        // Validar saldo suficiente
        if ((tipoMovimiento.getCodigo().equals("RETIRO") || tipoMovimiento.getCodigo().equals("TRANSFER")) &&
            cuentaOrigen.getSaldo().compareTo(monto) < 0) {
            throw new SaldoInsuficienteException("Saldo insuficiente en la cuenta origen: " + cuentaOrigen.getNumeroCuenta());
        }

        // Crear el movimiento
        Movimiento movimiento = new Movimiento();
        movimiento.setCuentaOrigen(cuentaOrigen);
        movimiento.setNumeroReferencia(UUID.randomUUID().toString());
        movimiento.setCuentaDestino(cuentaDestino);
        movimiento.setFechaMovimiento(tramaService.parseLocalDateTime(camposEntrada.get(CAMPO_FECHA_MOVIMIENTO)));
        movimiento.setHoraMovimiento(tramaService.parseLocalDateTime(camposEntrada.get(CAMPO_FECHA_MOVIMIENTO)) != null ? tramaService.parseLocalDateTime(camposEntrada.get(CAMPO_FECHA_MOVIMIENTO)).toLocalTime() : null);
        movimiento.setTipoMovimiento(tipoMovimiento);
        movimiento.setMonto(monto);

        // Actualizar saldos
        if (tipoMovimiento.getCodigo().equals("RETIRO")) {
            cuentaOrigen.setSaldo(cuentaOrigen.getSaldo().subtract(monto));
        } else if (tipoMovimiento.getCodigo().equals("DEPOSITO")) {
            if(cuentaDestino == null){
               cuentaOrigen.setSaldo(cuentaOrigen.getSaldo().add(monto));
            }
            else{
                cuentaDestino.setSaldo(cuentaDestino.getSaldo().add(monto));
            }
        } else if (tipoMovimiento.getCodigo().equals("TRANSFER")) {
            cuentaOrigen.setSaldo(cuentaOrigen.getSaldo().subtract(monto));
            cuentaDestino.setSaldo(cuentaDestino.getSaldo().add(monto));
        }

        try{
            System.out.println("cuenta origen"+ cuentaOrigen);
            cuentaRepository.save(cuentaOrigen);
            if(cuentaDestino!= null){
                 System.out.println("cuenta cuentaDestino"+ cuentaDestino);
                cuentaRepository.save(cuentaDestino);
            }
        }catch(Exception Ex){
            System.out.println(Ex);
        }catch(Error Ex){
            System.out.println(Ex);
        }

        System.out.println("movimiento "+movimiento);
        movimiento = movimientoRepository.save(movimiento);

        // --- Construcción de la respuesta ---
        List<TramaParametro> parametrosSalida = tramaService.getParametrosTrama(MOVIMIENTO_SALIDA);
        Map<String, String> camposSalida = new HashMap<>();


        // Llenar el mapa de salida utilizando directamente las constantes y los valores
        camposSalida.put(CAMPO_CODIGO_RESULTADO, "000"); // Considera hacerlo dinámico
        camposSalida.put(CAMPO_DESCRIPCION, "Operación exitosa"); // Considera hacerlo dinámico.
        camposSalida.put(CAMPO_NUMERO_REFERENCIA, movimiento.getNumeroReferencia());


        String tramaRespuesta = tramaService.parametrizarTrama(camposSalida, parametrosSalida);
        System.out.println("trama respuesta  "+ tramaRespuesta);

        RegistrarMovimientoResponse response = new RegistrarMovimientoResponse();
        response.setTrama(tramaRespuesta);
        return response;
    }
}