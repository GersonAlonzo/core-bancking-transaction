package com.banpais.api.command.config.interfaces;


import com.banpais.api.command.model.RegistrarClienteCommandModel;
import com.banpais.soap.client.ActualizarClienteResponse;
import com.banpais.soap.client.EliminarClienteResponse;
import com.banpais.soap.client.RegistrarClienteResponse;

public interface ISoapClienteOperations {
    RegistrarClienteResponse registrarCliente(RegistrarClienteCommandModel request);
    ActualizarClienteResponse actualizarCliente(RegistrarClienteCommandModel request);
    EliminarClienteResponse eliminarCliente(String clienteId);
}