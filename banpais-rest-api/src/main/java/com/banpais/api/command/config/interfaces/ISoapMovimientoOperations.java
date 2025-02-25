package com.banpais.api.command.config.interfaces;

import com.banpais.api.command.model.RegistrarMovimientoCommandModel;
import com.banpais.soap.client.RegistrarMovimientoResponse;

public interface ISoapMovimientoOperations {
    RegistrarMovimientoResponse registrarMovimiento(RegistrarMovimientoCommandModel request);
}