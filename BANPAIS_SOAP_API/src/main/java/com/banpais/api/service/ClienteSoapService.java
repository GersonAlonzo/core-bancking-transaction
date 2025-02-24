package com.banpais.api.service;

import com.banpais.api.exceptions.ClienteNotFoundException;
import com.banpais.api.exceptions.DuplicateIdentificacionException;
import com.banpais.api.infraestructure.entity.Cliente;
import com.banpais.api.infraestructure.entity.TramaParametro;
import com.banpais.api.infraestructure.repository.ClienteRepository;
import com.example.banco.*; // Importa las clases generadas

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.springframework.stereotype.Component;

@Service
@Component
public class ClienteSoapService {

    private final ClienteRepository clienteRepository;
    private final TramaService tramaService;

    // Constantes para los tipos de transacción
    private static final String CLIENTE_ENTRADA = "CLIENTE_IN";

    // Constantes para los nombres de los campos
    private static final String CAMPO_ID = "ID";
    private static final String CAMPO_NOMBRE = "NOMBRE";
    private static final String CAMPO_IDENTIFICACION = "IDENTIFICACION";
    private static final String CAMPO_TIPO_IDENTIFICACION = "TIPO_IDENTIFICACION";
    private static final String CAMPO_FECHA_NACIMIENTO = "FECHA_NACIMIENTO";


    public ClienteSoapService(ClienteRepository clienteRepository, TramaService tramaService) {
        this.clienteRepository = clienteRepository;
        this.tramaService = tramaService;
    }

    @Transactional
    public RegistrarClienteResponse registrarCliente(String trama) {
        // Desparametrizar la trama
        List<TramaParametro> parametros = tramaService.getParametrosTrama(CLIENTE_ENTRADA);
        System.out.println("Parametros trama" + parametros);
        System.out.println("trama plana" + trama);
        Map<String, String> campos = tramaService.desparametrizarTrama(trama, parametros);
        System.out.println(campos);

        // Verificar si ya existe un cliente con la misma identificación
        if (clienteRepository.findByIdentificacion(campos.get(CAMPO_IDENTIFICACION)) != null) {
            throw new DuplicateIdentificacionException("Ya existe un cliente con la identificación: " + campos.get(CAMPO_IDENTIFICACION));
        }

        // Crear y guardar la entidad Cliente
        Cliente cliente = new Cliente();
        cliente.setId(UUID.randomUUID().toString());

        //Asignacion usando constantes directamente
        cliente.setNombre(campos.get(CAMPO_NOMBRE));
        cliente.setIdentificacion(campos.get(CAMPO_IDENTIFICACION));
        cliente.setTipoIdentificacion(campos.get(CAMPO_TIPO_IDENTIFICACION));
        cliente.setFechaNacimiento(tramaService.parseLocalDate(campos.get(CAMPO_FECHA_NACIMIENTO)));
        System.out.println("cliente a guardar" + cliente);
        clienteRepository.save(cliente);

        // Crear la respuesta usando la clase generada
        RegistrarClienteResponse response = new RegistrarClienteResponse();
        response.setCodigo("000"); //Considera hacerlo dinamico
        response.setMensaje("Cliente registrado exitosamente"); //Considera hacerlo dinamico
        return response;
    }

    @Transactional
    public ActualizarClienteResponse actualizarCliente(String trama) {
        List<TramaParametro> parametros = tramaService.getParametrosTrama(CLIENTE_ENTRADA);
        Map<String, String> campos = tramaService.desparametrizarTrama(trama, parametros);

        Cliente cliente = clienteRepository.findById(campos.get(CAMPO_ID))
                .orElseThrow(() -> new ClienteNotFoundException("No existe el cliente con el ID: " + campos.get(CAMPO_ID)));

        //Asignacion usando las constantes directamente
        cliente.setNombre(campos.get(CAMPO_NOMBRE));
        cliente.setIdentificacion(campos.get(CAMPO_IDENTIFICACION));
        cliente.setTipoIdentificacion(campos.get(CAMPO_TIPO_IDENTIFICACION));
        cliente.setFechaNacimiento(tramaService.parseLocalDate(campos.get(CAMPO_FECHA_NACIMIENTO)));
        System.out.println("cliente update" + cliente);
        clienteRepository.save(cliente);

        ActualizarClienteResponse response = new ActualizarClienteResponse();
        response.setCodigo("000"); //Considera hacerlo dinamico
        response.setMensaje("Cliente actualizado exitosamente"); //Considera hacerlo dinamico
        return response;
    }

    @Transactional
    public EliminarClienteResponse eliminarCliente(String trama) {
        List<TramaParametro> parametros = tramaService.getParametrosTrama(CLIENTE_ENTRADA);
        Map<String, String> campos = tramaService.desparametrizarTrama(trama, parametros);

        Cliente cliente = clienteRepository.findById(campos.get(CAMPO_ID))
                .orElseThrow(() -> new ClienteNotFoundException("No existe el cliente con el ID: " + campos.get(CAMPO_ID)));

        clienteRepository.delete(cliente);

        EliminarClienteResponse response = new EliminarClienteResponse();
        response.setCodigo("000");  //Considera hacerlo dinamico
        response.setMensaje("Cliente eliminado exitosamente"); //Considera hacerlo dinamico
        return response;
    }
}