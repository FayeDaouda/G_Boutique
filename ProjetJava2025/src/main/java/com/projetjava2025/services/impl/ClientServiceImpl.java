package com.projetjava2025.services.impl;

import com.projetjava2025.entities.Client;
import com.projetjava2025.repositories.ClientRepository;
import com.projetjava2025.services.ClientService;
import java.util.List;

public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;

    public ClientServiceImpl(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Override
    public List<Client> getAllClients() {
        return clientRepository.findAll();
    }

    @Override
    public Client getClientById(int id) {
        return clientRepository.findById(id);
    }

    @Override
    public void saveClient(Client client) {
        clientRepository.save(client);
    }

    @Override
    public void deleteClient(int id) {
        clientRepository.delete(id);
    }
}
