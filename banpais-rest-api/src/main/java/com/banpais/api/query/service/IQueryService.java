
package com.banpais.api.query.service;

import com.banpais.api.query.model.ClienteQueyModel;
import com.banpais.api.query.model.CuentaQueyModel;
import com.banpais.api.query.model.MovimientoQueyModel;
import com.banpais.api.query.model.TipoMovimientoQueryModel;
import java.util.List;
import java.util.Optional;

public interface IQueryService {
    
    
    
    public Optional<ClienteQueyModel> getClienteByIdentificacion(String identificacion);
    public List<ClienteQueyModel> getAllClientes();
    public Optional<ClienteQueyModel> getClienteById(String id);
    
    
    
    public List<MovimientoQueyModel> getMovimientosByCuenta(String numeroCuenta);
    
    public Optional<CuentaQueyModel> getCuentaByNumeroCuenta(String numeroCuenta);
    public List<CuentaQueyModel> getCuentasByClienteId(String clienteId);
    public List<CuentaQueyModel> getAllCuentas();
    
    
     public Optional<TipoMovimientoQueryModel> getTipoMovimientoByCodigo(String codigo);
     public List<TipoMovimientoQueryModel> getAllTipoMovimientos();
     public List<MovimientoQueyModel> getAllMovimientos();
    
}
