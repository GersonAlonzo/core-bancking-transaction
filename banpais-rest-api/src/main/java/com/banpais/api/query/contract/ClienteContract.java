package com.banpais.api.query.contract;

import com.banpais.api.infraestructure.entity.Cliente;
import com.banpais.api.infraestructure.mapper.Mapper;
import com.banpais.api.infraestructure.repository.ClienteRepository;
import com.banpais.api.query.model.ClienteQueyModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class ClienteContract {
    
    @Autowired
    private ClienteRepository clienteRepository;
    
    public Optional<ClienteQueyModel> getClienteByIdentificacion(String identificacion) {
        Cliente cliente = clienteRepository.findByIdentificacion(identificacion);
        if (cliente != null) {
            return Optional.of(Mapper.getInstance().map(cliente, ClienteQueyModel.class));
        } else {
            return Optional.empty();
        }
    }
    
     public Optional<ClienteQueyModel> getClienteById(String id) {
        Optional<Cliente> cliente = clienteRepository.findById(id);
        if (cliente != null) {
            return Optional.of(Mapper.getInstance().map(cliente, ClienteQueyModel.class));
        } else {
            return Optional.empty();
        }
    }
    
    public List<ClienteQueyModel> getAllClientes() {
        List<Cliente> clientes = clienteRepository.findAll();
        return clientes.stream()
                .map(cliente -> Mapper.getInstance().map(cliente, ClienteQueyModel.class))
                .collect(Collectors.toList());
    }
}