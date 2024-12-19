package com.projetjava2025.services;

import com.projetjava2025.entities.Client;
import java.util.List;

public interface ClientService {
    List<Client> getAllClients();     // Récupérer tous les clients
    Client getClientById(int id);     // Récupérer un client par son ID
    void saveClient(Client client);   // Sauvegarder un client
    void deleteClient(int id);       // Supprimer un client par son ID
    
}
