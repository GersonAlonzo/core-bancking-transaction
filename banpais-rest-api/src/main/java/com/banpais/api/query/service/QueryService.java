package com.banpais.api.query.service;

import com.banpais.api.query.contract.ClienteContract;
import com.banpais.api.query.contract.CuentaContract;
import com.banpais.api.query.contract.MovimientoContract;
import com.banpais.api.query.contract.TipoMovimientoContract;
import com.banpais.api.query.model.ClienteQueyModel;
import com.banpais.api.query.model.CuentaQueyModel;
import com.banpais.api.query.model.MovimientoQueyModel;
import com.banpais.api.query.model.TipoMovimientoQueryModel;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class QueryService implements IQueryService {

    private final ClienteContract clienteQueryContract;
    private final CuentaContract cuentaQueryContract;
    private final MovimientoContract movimientoQueryContract;
    private final TipoMovimientoContract tipoMovimientoContract;

    @Autowired
    public QueryService(ClienteContract clienteQueryContract,
                        CuentaContract cuentaQueryContract,
                        MovimientoContract movimientoQueryContract,
                        TipoMovimientoContract tipoMovimientoContract) {
        this.clienteQueryContract = clienteQueryContract;
        this.cuentaQueryContract = cuentaQueryContract;
        this.movimientoQueryContract = movimientoQueryContract;
        this.tipoMovimientoContract = tipoMovimientoContract;
    }

    @Override
    public Optional<ClienteQueyModel> getClienteByIdentificacion(String identificacion) {
        return clienteQueryContract.getClienteByIdentificacion(identificacion);
    }

    @Override
    public List<ClienteQueyModel> getAllClientes() {
        return clienteQueryContract.getAllClientes();
    }

    @Override
    public List<MovimientoQueyModel> getMovimientosByCuenta(String numeroCuenta) {
        return movimientoQueryContract.getMovimientosByCuenta(numeroCuenta);
    }

    @Override
    public Optional<CuentaQueyModel> getCuentaByNumeroCuenta(String numeroCuenta) {
        return cuentaQueryContract.getCuentaByNumeroCuenta(numeroCuenta);
    }

    @Override
    public List<CuentaQueyModel> getCuentasByClienteId(String clienteId) {
        return cuentaQueryContract.getCuentasByClienteId(clienteId);
    }
    
    @Override
    public Optional<TipoMovimientoQueryModel> getTipoMovimientoByCodigo(String codigo) {
        return tipoMovimientoContract.getByCodigo(codigo);
    }

    @Override
    public List<TipoMovimientoQueryModel> getAllTipoMovimientos() {
        return tipoMovimientoContract.getAll();
    }


}