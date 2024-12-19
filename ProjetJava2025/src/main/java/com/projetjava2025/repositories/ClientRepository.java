package com.projetjava2025.repositories;

import com.projetjava2025.entities.Client;
import java.util.List;

public interface ClientRepository {
    void create(Client client);
    List<Client> findAll();
    Client findByTelephone(String telephone);
    List<Client> findByHasAccount(boolean hasAccount);
    Client findById(int id);  // Ajoute cette méthode
    void update(Client client);
    void delete(int id);
    void save(Client client); // Ajoute cette méthode
    Client findByUserId(int userId);
    List<Client> findClientsWithAccounts();
    List<Client> findClientsWithoutAccounts();
}
