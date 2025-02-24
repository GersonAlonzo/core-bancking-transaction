package com.banpais.api.query.contract;

import com.banpais.api.infraestructure.entity.Cliente;
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
            ClienteQueyModel clienteQueyModel = new ClienteQueyModel();
            clienteQueyModel.setId(cliente.getId());
            clienteQueyModel.setNombre(cliente.getNombre());
            clienteQueyModel.setIdentificacion(cliente.getIdentificacion());
            clienteQueyModel.setTipoIdentificacion(cliente.getTipoIdentificacion());
            clienteQueyModel.setFechaNacimiento(cliente.getFechaNacimiento());
            return Optional.of(clienteQueyModel);
        } else {
            return Optional.empty();
        }
    }

    public List<ClienteQueyModel> getAllClientes() {
        List<Cliente> clientes = clienteRepository.findAll();
        return clientes.stream()
                .map(cliente -> {
                    ClienteQueyModel clienteQueyModel = new ClienteQueyModel();
                    clienteQueyModel.setId(cliente.getId());
                    clienteQueyModel.setNombre(cliente.getNombre());
                    clienteQueyModel.setIdentificacion(cliente.getIdentificacion());
                    clienteQueyModel.setTipoIdentificacion(cliente.getTipoIdentificacion());
                    clienteQueyModel.setFechaNacimiento(cliente.getFechaNacimiento());
                    return clienteQueyModel;
                })
                .collect(Collectors.toList());
    }
}