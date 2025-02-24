package com.banpais.api.query.contract;

import com.banpais.api.infraestructure.entity.Cuenta;
import com.banpais.api.infraestructure.entity.Movimiento;
import com.banpais.api.infraestructure.repository.CuentaRepository;
import com.banpais.api.infraestructure.repository.MovimientoRepository;
import com.banpais.api.query.model.CuentaQueyModel;
import com.banpais.api.query.model.MovimientoQueyModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class CuentaContract {

    @Autowired
    private CuentaRepository cuentaRepository;

    public Optional<CuentaQueyModel> getCuentaByNumeroCuenta(String numeroCuenta) {
        return cuentaRepository.findByNumeroCuenta(numeroCuenta)
                .map(cuenta -> {
                    CuentaQueyModel cuentaQueyModel = new CuentaQueyModel();
                    cuentaQueyModel.setNumeroCuenta(cuenta.getNumeroCuenta());
                    cuentaQueyModel.setClienteId(cuenta.getCliente());
                    cuentaQueyModel.setFechaApertura(cuenta.getFechaApertura());
                    cuentaQueyModel.setHoraApertura(cuenta.getHoraApertura());
                    cuentaQueyModel.setEstadoCuenta(cuenta.getEstadoCuenta());
                    cuentaQueyModel.setSaldo(cuenta.getSaldo());
                    return cuentaQueyModel;
                });
    }

    public List<CuentaQueyModel> getCuentasByClienteId(String clienteId) {
        return cuentaRepository.findCuentasByClienteId(clienteId).stream()
                .map(cuenta -> {
                    CuentaQueyModel cuentaQueyModel = new CuentaQueyModel();
                    cuentaQueyModel.setNumeroCuenta(cuenta.getNumeroCuenta());
                    cuentaQueyModel.setClienteId(cuenta.getCliente());
                    cuentaQueyModel.setFechaApertura(cuenta.getFechaApertura());
                    cuentaQueyModel.setHoraApertura(cuenta.getHoraApertura());
                    cuentaQueyModel.setEstadoCuenta(cuenta.getEstadoCuenta());
                    cuentaQueyModel.setSaldo(cuenta.getSaldo());
                    return cuentaQueyModel;
                })
                .collect(Collectors.toList());
    }
}