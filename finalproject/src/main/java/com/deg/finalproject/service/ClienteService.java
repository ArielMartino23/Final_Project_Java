package com.deg.finalproject.service;

import com.deg.finalproject.entidad.Cliente;
import com.deg.finalproject.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    public List<Cliente> findAll() {
        return this.clienteRepository.findAll();
    }

    public Cliente save(Cliente cliente) {
        return this.clienteRepository.save(cliente);
    }

    public Cliente findById(Integer id) {

        var opCliente =  this.clienteRepository.findById(id);
        if (opCliente.isPresent()) {
            return opCliente.get();
        } else {
            return new Cliente();
        }
    }
}
