package com.banpais.api.query.contract;

import com.banpais.api.infraestructure.entity.Cuenta;
import com.banpais.api.infraestructure.mapper.Mapper;
import com.banpais.api.infraestructure.repository.CuentaRepository;
import com.banpais.api.query.model.CuentaQueyModel;
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
                .map(cuenta -> Mapper.getInstance().map(cuenta, CuentaQueyModel.class));
    }
    
    public List<CuentaQueyModel> getCuentasByClienteId(String clienteId) {
        return cuentaRepository.findCuentasByClienteId(clienteId).stream()
                .map(cuenta -> Mapper.getInstance().map(cuenta, CuentaQueyModel.class))
                .collect(Collectors.toList());
    }
    
    public List<CuentaQueyModel> getAllCuentas() {
        return cuentaRepository.findAll().stream()
                .map(cuenta -> Mapper.getInstance().map(cuenta, CuentaQueyModel.class))
                .collect(Collectors.toList());
    }
}