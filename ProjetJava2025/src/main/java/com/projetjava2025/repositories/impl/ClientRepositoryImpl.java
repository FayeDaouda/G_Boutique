package com.projetjava2025.repositories.impl;

import com.projetjava2025.entities.Client;
import com.projetjava2025.repositories.ClientRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClientRepositoryImpl implements ClientRepository {
    private static final String URL = "jdbc:postgresql://localhost:5432/javabbd";
    private static final String USER = "mon_utilisateur";
    private static final String PASSWORD = "votre_mot_de_passe";
    private static final Logger LOGGER = Logger.getLogger(ClientRepositoryImpl.class.getName());

    @Override
    public void create(Client client) {
        String query = "INSERT INTO clients (surname, phone, address, has_account) VALUES (?, ?, ?, ?)";
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, client.getSurname());
            stmt.setString(2, client.getPhone());
            stmt.setString(3, client.getAddress());
            stmt.setBoolean(4, client.isHasAccount());
            stmt.executeUpdate();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error creating client: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Client> findAll() {
        List<Client> clients = new ArrayList<>();
        String query = "SELECT * FROM clients";
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                clients.add(new Client(
                        rs.getInt("id"),
                        rs.getString("surname"),
                        rs.getString("phone"),
                        rs.getString("address"),
                        rs.getBoolean("has_account")
                ));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error fetching clients: " + e.getMessage(), e);
        }
        return clients;
    }
    @Override
public Client findById(int id) {
    Client client = null;
    String query = "SELECT * FROM clients WHERE id = ?";
    // Créer une connexion à la base de données ici
    try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
         PreparedStatement statement = connection.prepareStatement(query)) {
        statement.setInt(1, id);
        try (ResultSet resultSet = statement.executeQuery()) {
            if (resultSet.next()) {
                client = new Client();
                client.setId(resultSet.getInt("id"));
                client.setPhone(resultSet.getString("phone"));
                client.setSurname(resultSet.getString("surname"));
                client.setHasAccount(resultSet.getBoolean("has_account"));
            }
        }
    } catch (SQLException e) {
        LOGGER.log(Level.SEVERE, "Error finding client by ID: " + e.getMessage(), e);
    }
    return client;
}

    @Override
    public void save(Client client) {
        // Si le client existe déjà (basé sur son id), on effectue une mise à jour
        if (client.getId() > 0) {
            update(client);
        } else {
            // Sinon, on crée un nouveau client
            create(client);
        }
    }

    @Override
    public Client findByTelephone(String telephone) {
        String query = "SELECT * FROM clients WHERE phone = ?";
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, telephone);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Client(
                            rs.getInt("id"),
                            rs.getString("surname"),
                            rs.getString("phone"),
                            rs.getString("address"),
                            rs.getBoolean("has_account")
                    );
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error finding client by phone: " + e.getMessage(), e);
        }
        return null;
    }

    @Override
public Client findByUserId(int userId) {
    String query = "SELECT * FROM clients WHERE user_id = ?"; // Utilisez `user_id` selon la structure de votre table
    try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
         PreparedStatement stmt = connection.prepareStatement(query)) {

        stmt.setInt(1, userId);

        try (ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return new Client(
                        rs.getInt("id"),
                        rs.getString("surname"),
                        rs.getString("phone"),
                        rs.getString("address"),
                        rs.getBoolean("has_account")
                );
            }
        }
    } catch (SQLException e) {
        LOGGER.log(Level.SEVERE, "Error finding client by userId: " + e.getMessage(), e);
    }
    return null; // Retourner null si aucun client n'est trouvé
}


    @Override
    public List<Client> findByHasAccount(boolean hasAccount) {
        List<Client> clients = new ArrayList<>();
        String query = "SELECT * FROM clients WHERE has_account = ?";
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setBoolean(1, hasAccount);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    clients.add(new Client(
                            rs.getInt("id"),
                            rs.getString("surname"),
                            rs.getString("phone"),
                            rs.getString("address"),
                            rs.getBoolean("has_account")
                    ));
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error finding clients by account status: " + e.getMessage(), e);
        }
        return clients;
    }

    @Override
    public void update(Client client) {
        String query = "UPDATE clients SET surname = ?, phone = ?, address = ?, has_account = ? WHERE id = ?";
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, client.getSurname());
            stmt.setString(2, client.getPhone());
            stmt.setString(3, client.getAddress());
            stmt.setBoolean(4, client.isHasAccount());
            stmt.setInt(5, client.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error updating client: " + e.getMessage(), e);
        }
    }

    @Override
    public void delete(int id) {
        String query = "DELETE FROM clients WHERE id = ?";
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error deleting client: " + e.getMessage(), e);
        }
    }

    @Override
public List<Client> findClientsWithAccounts() {
    List<Client> clients = new ArrayList<>();
    String query = "SELECT * FROM clients WHERE has_account = TRUE";
    try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
         PreparedStatement stmt = connection.prepareStatement(query);
         ResultSet rs = stmt.executeQuery()) {
        while (rs.next()) {
            clients.add(new Client(
                    rs.getInt("id"),
                    rs.getString("surname"),
                    rs.getString("phone"),
                    rs.getString("address"),
                    rs.getBoolean("has_account")
            ));
        }
    } catch (SQLException e) {
        LOGGER.log(Level.SEVERE, "Error fetching clients with accounts: " + e.getMessage(), e);
    }
    return clients;
}

@Override
public List<Client> findClientsWithoutAccounts() {
    List<Client> clients = new ArrayList<>();
    String query = "SELECT * FROM clients WHERE has_account = FALSE";
    try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
         PreparedStatement stmt = connection.prepareStatement(query);
         ResultSet rs = stmt.executeQuery()) {
        while (rs.next()) {
            clients.add(new Client(
                    rs.getInt("id"),
                    rs.getString("surname"),
                    rs.getString("phone"),
                    rs.getString("address"),
                    rs.getBoolean("has_account")
            ));
        }
    } catch (SQLException e) {
        LOGGER.log(Level.SEVERE, "Error fetching clients without accounts: " + e.getMessage(), e);
    }
    return clients;
}

    

}
