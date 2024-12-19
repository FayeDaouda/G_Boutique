package com.projetjava2025.repositories.impl;

import com.projetjava2025.entities.Utilisateur;
import com.projetjava2025.repositories.UtilisateurRepository;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UtilisateurRepositoryImpl implements UtilisateurRepository {
    private static final String URL = "jdbc:postgresql://localhost:5432/javabbd";
    private static final String USER = "mon_utilisateur";
    private static final String PASSWORD = "votre_mot_de_passe";
    
    @Override
public Utilisateur findByLoginAndPassword(String login, String password) {
    String query = "SELECT * FROM utilisateurs WHERE login = ? AND password = ? AND is_active = true"; // Changer isActive par is_active
    try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
         PreparedStatement stmt = conn.prepareStatement(query)) {

        stmt.setString(1, login);
        stmt.setString(2, password);

        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            // Si l'utilisateur est trouvé et actif, on renvoie l'objet Utilisateur
            return new Utilisateur(
                rs.getInt("id"),
                rs.getString("email"),
                rs.getString("login"),
                rs.getString("password"),
                rs.getString("role"),
                rs.getBoolean("is_active") // Changer isActive par is_active ici aussi
            );
        }

    } catch (SQLException e) {
        e.printStackTrace();
    }
    return null; // Si aucun utilisateur n'est trouvé ou inactif
}



    
    @Override
public void create(Utilisateur utilisateur) {
    String query = "INSERT INTO utilisateurs (email, login, password, role) VALUES (?, ?, ?, ?)";
    try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
         PreparedStatement stmt = connection.prepareStatement(query)) {
        stmt.setString(1, utilisateur.getEmail());
        stmt.setString(2, utilisateur.getLogin());
        stmt.setString(3, utilisateur.getPassword());
        stmt.setString(4, utilisateur.getRole());
        stmt.executeUpdate();
    } catch (SQLIntegrityConstraintViolationException e) {
        System.out.println("Erreur : Un utilisateur avec cet email ou login existe déjà.");
    } catch (SQLException e) {
        System.out.println("Erreur lors de la création de l'utilisateur : " + e.getMessage());
    }
}


    @Override
    public List<Utilisateur> findAll() {
        List<Utilisateur> utilisateurs = new ArrayList<>();
        String query = "SELECT * FROM utilisateurs";
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                Utilisateur utilisateur = new Utilisateur();
                utilisateur.setId(rs.getInt("id"));
                utilisateur.setEmail(rs.getString("email"));
                utilisateur.setLogin(rs.getString("login"));
                utilisateur.setPassword(rs.getString("password"));
                utilisateur.setRole(rs.getString("role"));
                utilisateurs.add(utilisateur);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return utilisateurs;
    }

    @Override
public Utilisateur findById(int id) {
    String query = "SELECT * FROM utilisateurs WHERE id = ?";
    try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
         PreparedStatement stmt = connection.prepareStatement(query)) {

        stmt.setInt(1, id);

        try (ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                // Créer et retourner l'objet Utilisateur
                return new Utilisateur(
                    rs.getInt("id"),
                    rs.getString("email"),
                    rs.getString("login"),
                    rs.getString("password"),
                    rs.getString("role"),
                    rs.getBoolean("is_active")  // Récupérer le champ isActive
                );
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return null; // Aucun utilisateur trouvé avec cet ID
}

   
    @Override
    public Utilisateur findByLogin(String login) {
        String query = "SELECT * FROM utilisateurs WHERE login = ?";
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, login);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Utilisateur utilisateur = new Utilisateur();
                    utilisateur.setId(rs.getInt("id"));
                    utilisateur.setEmail(rs.getString("email"));
                    utilisateur.setLogin(rs.getString("login"));
                    utilisateur.setPassword(rs.getString("password"));
                    utilisateur.setRole(rs.getString("role"));
                    return utilisateur;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
public Utilisateur findByEmail(String email) {
    String query = "SELECT * FROM utilisateurs WHERE email = ?";
    try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
         PreparedStatement stmt = connection.prepareStatement(query)) {
        
        stmt.setString(1, email);  // On définit l'email comme paramètre de la requête
        try (ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {  // Si un résultat est trouvé
                Utilisateur utilisateur = new Utilisateur();
                utilisateur.setId(rs.getInt("id"));
                utilisateur.setEmail(rs.getString("email"));
                utilisateur.setLogin(rs.getString("login"));
                utilisateur.setPassword(rs.getString("password"));
                utilisateur.setRole(rs.getString("role"));
                return utilisateur;
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();  // On affiche l'exception si elle se produit
    }
    return null;  // Si aucun utilisateur n'est trouvé, on retourne null
}


    @Override
    public void update(Utilisateur utilisateur) {
        String query = "UPDATE utilisateurs SET email = ?, login = ?, password = ?, role = ? WHERE id = ?";
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, utilisateur.getEmail());
            stmt.setString(2, utilisateur.getLogin());
            stmt.setString(3, utilisateur.getPassword());
            stmt.setString(4, utilisateur.getRole());
            stmt.setInt(5, utilisateur.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(int id) {
        String query = "DELETE FROM utilisateurs WHERE id = ?";
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateStatus(int id, boolean is_active) {
        String query = "UPDATE utilisateurs SET is_active = ? WHERE id = ?";
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setBoolean(1, is_active);
            pstmt.setInt(2, id);
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("L'utilisateur a été mis à jour avec succès.");
            } else {
                System.out.println("Aucun utilisateur trouvé avec cet ID.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
public List<Utilisateur> findByRole(String role) {
    List<Utilisateur> utilisateurs = new ArrayList<>();
    String query = "SELECT * FROM utilisateurs WHERE role = ?";
    try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
         PreparedStatement stmt = connection.prepareStatement(query)) {

        stmt.setString(1, role);

        try (ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Utilisateur utilisateur = new Utilisateur();
                utilisateur.setId(rs.getInt("id"));
                utilisateur.setEmail(rs.getString("email"));
                utilisateur.setLogin(rs.getString("login"));
                utilisateur.setPassword(rs.getString("password"));
                utilisateur.setRole(rs.getString("role"));
                utilisateurs.add(utilisateur);
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return utilisateurs;
}
@Override
public List<Utilisateur> findActiveUsers() {
    List<Utilisateur> utilisateurs = new ArrayList<>();
    String query = "SELECT * FROM utilisateurs WHERE is_active = TRUE"; // Vérifiez que la colonne `is_active` existe et est de type BOOLEAN
    try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
         PreparedStatement stmt = connection.prepareStatement(query);
         ResultSet rs = stmt.executeQuery()) {

        while (rs.next()) {
            Utilisateur utilisateur = new Utilisateur();
            utilisateur.setId(rs.getInt("id"));
            utilisateur.setEmail(rs.getString("email"));
            utilisateur.setLogin(rs.getString("login"));
            utilisateur.setPassword(rs.getString("password"));
            utilisateur.setRole(rs.getString("role"));
            utilisateurs.add(utilisateur);
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return utilisateurs;
}

    
    
}
