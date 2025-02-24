package com.banpais.api.command.config.interfaces;


import com.banpais.api.command.model.RegistrarCuentaCommandModel;
import com.banpais.soap.client.EliminarCuentaResponse;
import com.banpais.soap.client.RegistrarCuentaResponse;

public interface ISoapCuentaOperations {
    RegistrarCuentaResponse registrarCuenta(RegistrarCuentaCommandModel request);
    EliminarCuentaResponse eliminarCuenta(String numeroCuenta);
}