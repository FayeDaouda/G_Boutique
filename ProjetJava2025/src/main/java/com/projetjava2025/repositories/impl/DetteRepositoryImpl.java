package com.projetjava2025.repositories.impl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.projetjava2025.entities.Client;
import com.projetjava2025.entities.Dette;
import com.projetjava2025.repositories.DetteRepository;

public class DetteRepositoryImpl implements DetteRepository {
    private static final String URL = "jdbc:postgresql://localhost:5432/javabbd";
    private static final String USER = "mon_utilisateur";
    private static final String PASSWORD = "votre_mot_de_passe";

    @Override
public void create(Dette dette) {
    String query = "INSERT INTO dettes (client_id, date, montant, montant_verser, montant_restante) VALUES (?, ?, ?, ?, ?)";
    try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
         PreparedStatement stmt = connection.prepareStatement(query)) {
        
        // Utilisation de clientId directement
        stmt.setInt(1, dette.getClientId());  // Remplacez getClient() par getClientId()
        stmt.setDate(2, new java.sql.Date(dette.getDate().getTime())); // Conversion Date à SQL Date
        stmt.setDouble(3, dette.getMontant());
        stmt.setDouble(4, dette.getMontantVerser());
        stmt.setDouble(5, dette.getMontantRestant());
        
        stmt.executeUpdate();
    } catch (SQLException e) {
        e.printStackTrace();
    }
}


    @Override
    public List<Dette> findAll() {
        List<Dette> dettes = new ArrayList<>();
        String query = "SELECT * FROM dettes";
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                Client client = new Client(); // Assure-toi que l'objet Client est bien peuplé (si tu as un ClientRepository, utilise-le)
                client.setId(rs.getInt("client_id"));

                Dette dette = new Dette();
                dette.setId(rs.getInt("id"));
                dette.setClientId(rs.getInt("client_id")); // Utilisation de clientId
                dette.setDate(rs.getDate("date"));
                dette.setMontant(rs.getDouble("montant"));
                dette.setMontantVerser(rs.getDouble("montant_verser"));
                dette.setMontantRestant(rs.getDouble("montant_restante"));

                dettes.add(dette);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dettes;
    }

    @Override
    public Dette findById(int id) {
        String query = "SELECT * FROM dettes WHERE id = ?";
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Client client = new Client(); // Assure-toi que l'objet Client est bien peuplé
                    client.setId(rs.getInt("client_id"));

                    Dette dette = new Dette();
                    dette.setId(rs.getInt("id"));
                    dette.setClientId(rs.getInt("client_id")); // Utilisation de clientId
                    dette.setDate(rs.getDate("date"));
                    dette.setMontant(rs.getDouble("montant"));
                    dette.setMontantVerser(rs.getDouble("montant_verser"));
                    dette.setMontantRestant(rs.getDouble("montant_restante"));
                    return dette;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Dette> findByClientId(int clientId) {
        List<Dette> dettes = new ArrayList<>();
        String query = "SELECT * FROM dettes WHERE client_id = ?";
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, clientId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Client client = new Client(); // Assure-toi que l'objet Client est bien peuplé
                    client.setId(rs.getInt("client_id"));

                    Dette dette = new Dette();
                    dette.setId(rs.getInt("id"));
                    dette.setClient(client);
                    dette.setDate(rs.getDate("date"));
                    dette.setMontant(rs.getDouble("montant"));
                    dette.setMontantVerser(rs.getDouble("montant_verser"));
                    dette.setMontantRestant(rs.getDouble("montant_restante"));

                    dettes.add(dette);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dettes;
    }

 @Override
public void update(Dette dette) {
    if (dette == null) {
        throw new IllegalArgumentException("L'objet Dette ne peut pas être null.");
    }
    if (dette.getClientId() <= 0) { // Vérification de l'ID du client
        throw new IllegalArgumentException("L'ID du client associé à la dette est invalide ou non défini.");
    }
    if (dette.getDate() == null) {
        throw new IllegalArgumentException("La date de la dette est null.");
    }

    // Calcul automatique du montant restant
    double montantRestant = dette.getMontant() - dette.getMontantVerser();
    dette.setMontantRestant(montantRestant);

    String query = "UPDATE dettes SET client_id = ?, date = ?, montant = ?, montant_verser = ?, montant_restante = ? WHERE id = ?";
    try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
         PreparedStatement stmt = connection.prepareStatement(query)) {
        stmt.setInt(1, dette.getClientId()); // Utilisation de clientId directement
        stmt.setDate(2, new java.sql.Date(dette.getDate().getTime())); // Conversion Date à SQL Date
        stmt.setDouble(3, dette.getMontant());
        stmt.setDouble(4, dette.getMontantVerser());
        stmt.setDouble(5, montantRestant); // Mise à jour du montant restant
        stmt.setInt(6, dette.getId());

        int rowsAffected = stmt.executeUpdate();
        if (rowsAffected == 0) {
            throw new RuntimeException("Aucune ligne n'a été mise à jour. La dette avec ID " + dette.getId() + " n'existe pas.");
        }
    } catch (SQLException e) {
        e.printStackTrace();
        throw new RuntimeException("Erreur lors de la mise à jour de la dette : " + e.getMessage());
    }
}

    

    @Override
    public void delete(int id) {
        String query = "DELETE FROM dettes WHERE id = ?";
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @Override
public List<Dette> findNonSoldedByClientPhone(String phone) {
    List<Dette> dettes = new ArrayList<>();
    String query = "SELECT * FROM dettes JOIN clients ON dettes.client_id = clients.id WHERE clients.phone = ? AND dettes.montant_restante > 0";
    
    try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
         PreparedStatement stmt = connection.prepareStatement(query)) {
        stmt.setString(1, phone); // Paramètre pour le téléphone

        try (ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Client client = new Client();
                client.setId(rs.getInt("client_id"));
                client.setPhone(rs.getString("phone"));

                Dette dette = new Dette();
                dette.setId(rs.getInt("id"));
                dette.setClient(client);
                dette.setDate(rs.getDate("date"));
                dette.setMontant(rs.getDouble("montant"));
                dette.setMontantVerser(rs.getDouble("montant_verser"));
                dette.setMontantRestant(rs.getDouble("montant_restante"));
                
                // Filtrer les dettes non soldées
                if (dette.getMontantRestant() > 0) {
                    dettes.add(dette);
                }
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return dettes;
}


}
