package com.projetjava2025.repositories.impl;

import com.projetjava2025.entities.Paiement;
import com.projetjava2025.entities.Dette;
import com.projetjava2025.repositories.PaiementRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PaiementRepositoryImpl implements PaiementRepository {
    private static final String URL = "jdbc:postgresql://localhost:5432/javabbd";
    private static final String USER = "mon_utilisateur";
    private static final String PASSWORD = "votre_mot_de_passe";

    @Override
    public void create(Paiement paiement) {
        String query = "INSERT INTO paiements (dette_id, date, montant) VALUES (?, ?, ?)";
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, paiement.getDette().getId());
            stmt.setDate(2, new java.sql.Date(paiement.getDate().getTime()));
            stmt.setDouble(3, paiement.getMontant());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Paiement> findAll() {
        List<Paiement> paiements = new ArrayList<>();
        String query = "SELECT * FROM paiements";
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                Paiement paiement = new Paiement();
                paiement.setId(rs.getInt("id"));
                paiement.setDate(rs.getDate("date"));
                paiement.setMontant(rs.getDouble("montant"));
                // On récupère la Dette associée au Paiement
                Dette dette = new Dette();  // Assume que tu as une méthode pour récupérer une Dette via l'ID
                dette.setId(rs.getInt("dette_id"));
                paiement.setDette(dette);
                paiements.add(paiement);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return paiements;
    }

    @Override
    public Paiement findById(int id) {
        String query = "SELECT * FROM paiements WHERE id = ?";
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Paiement paiement = new Paiement();
                    paiement.setId(rs.getInt("id"));
                    paiement.setDate(rs.getDate("date"));
                    paiement.setMontant(rs.getDouble("montant"));
                    // On récupère la Dette associée
                    Dette dette = new Dette();
                    dette.setId(rs.getInt("dette_id"));
                    paiement.setDette(dette);
                    return paiement;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Paiement> findByDetteId(int detteId) {
        List<Paiement> paiements = new ArrayList<>();
        String query = "SELECT * FROM paiements WHERE dette_id = ?";
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, detteId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Paiement paiement = new Paiement();
                    paiement.setId(rs.getInt("id"));
                    paiement.setDate(rs.getDate("date"));
                    paiement.setMontant(rs.getDouble("montant"));
                    // On récupère la Dette associée
                    Dette dette = new Dette();
                    dette.setId(rs.getInt("dette_id"));
                    paiement.setDette(dette);
                    paiements.add(paiement);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return paiements;
    }

    @Override
    public void update(Paiement paiement) {
        String query = "UPDATE paiements SET dette_id = ?, date = ?, montant = ? WHERE id = ?";
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, paiement.getDette().getId());
            stmt.setDate(2, new java.sql.Date(paiement.getDate().getTime()));
            stmt.setDouble(3, paiement.getMontant());
            stmt.setInt(4, paiement.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(int id) {
        String query = "DELETE FROM paiements WHERE id = ?";
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

