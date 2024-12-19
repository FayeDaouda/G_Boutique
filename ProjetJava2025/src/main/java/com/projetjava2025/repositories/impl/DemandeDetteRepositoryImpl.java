package com.projetjava2025.repositories.impl;

import com.projetjava2025.entities.DemandeDette;
import com.projetjava2025.repositories.DemandeDetteRepository;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import com.projetjava2025.entities.Client;
import com.projetjava2025.entities.Article;


public class DemandeDetteRepositoryImpl implements DemandeDetteRepository {
    private static final String URL = "jdbc:postgresql://localhost:5432/javabbd";
    private static final String USER = "mon_utilisateur";
    private static final String PASSWORD = "votre_mot_de_passe";

    @Override
public void create(DemandeDette demandeDette) {
    String query = "INSERT INTO demandes_dette (client_id, date, montant, status) VALUES (?, ?, ?, ?)";
    try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
         PreparedStatement stmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
        stmt.setInt(1, demandeDette.getClientId()); 
        stmt.setDate(2, new java.sql.Date(demandeDette.getDate().getTime()));
        stmt.setDouble(3, demandeDette.getMontant());
        stmt.setString(4, demandeDette.getStatus());
        stmt.executeUpdate();

        // Récupérer l'ID généré pour la demande de dette
        ResultSet rs = stmt.getGeneratedKeys();
        if (rs.next()) {
            int demandeId = rs.getInt(1);
            // Ajouter les articles à la table d'association
            for (Article article : demandeDette.getArticles()) {
                addArticleToDemande(demandeId, article.getId());
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
}

private void addArticleToDemande(int demandeId, int articleId) {
    String query = "INSERT INTO demande_dette_articles (demande_dette_id, article_id) VALUES (?, ?)";
    try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
         PreparedStatement stmt = connection.prepareStatement(query)) {
        stmt.setInt(1, demandeId);
        stmt.setInt(2, articleId);
        stmt.executeUpdate();
    } catch (SQLException e) {
        e.printStackTrace();
    }
}


    @Override
public List<DemandeDette> findAll() {
    List<DemandeDette> demandesDettes = new ArrayList<>();
    String query = "SELECT * FROM demandes_dette";
    try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
         Statement stmt = connection.createStatement();
         ResultSet rs = stmt.executeQuery(query)) {
        while (rs.next()) {
            DemandeDette demandeDette = new DemandeDette();
            demandeDette.setId(rs.getInt("id"));
            demandeDette.setDate(rs.getDate("date"));
            demandeDette.setStatus(rs.getString("status"));
            demandeDette.setClientId(rs.getInt("client_id"));
            
            // Récupérer les articles associés via la table d'association
            demandeDette.setArticles(getArticlesByDemandeId(demandeDette.getId()));
            
            demandesDettes.add(demandeDette);
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return demandesDettes;
}

@Override
public List<DemandeDette> findAllNonArchivees() {
    List<DemandeDette> demandesDettes = new ArrayList<>();
    String query = "SELECT * FROM demandes_dette WHERE status != 'Archivée'";
    try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
         Statement stmt = connection.createStatement();
         ResultSet rs = stmt.executeQuery(query)) {
        while (rs.next()) {
            DemandeDette demandeDette = new DemandeDette();
            demandeDette.setId(rs.getInt("id"));
            demandeDette.setDate(rs.getDate("date"));
            demandeDette.setStatus(rs.getString("status"));
            demandeDette.setClientId(rs.getInt("client_id"));

            // Récupérer les articles associés via la table d'association
            demandeDette.setArticles(getArticlesByDemandeId(demandeDette.getId()));

            demandesDettes.add(demandeDette);
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return demandesDettes;
}


@Override
public DemandeDette findById(int id) {
    String query = "SELECT * FROM demandes_dette WHERE id = ?";
    try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
         PreparedStatement stmt = connection.prepareStatement(query)) {
        stmt.setInt(1, id);
        try (ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                DemandeDette demandeDette = new DemandeDette();
                demandeDette.setId(rs.getInt("id"));
                demandeDette.setDate(rs.getDate("date"));
                demandeDette.setStatus(rs.getString("status"));
                demandeDette.setClientId(rs.getInt("client_id"));

                
                // Récupérer les articles associés via la table d'association
                demandeDette.setArticles(getArticlesByDemandeId(demandeDette.getId()));
                
                return demandeDette;
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return null;
}




// Method to update the status of a DemandeDette
public void update(DemandeDette demande) {
    String sql = "UPDATE demandes_dette SET status = ? WHERE id = ?";  // Utilisation de 'status' au lieu de 'etat'
    try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
         PreparedStatement stmt = connection.prepareStatement(sql)) {
        stmt.setString(1, demande.getStatus());  // Utilisation de 'getStatus'
        stmt.setInt(2, demande.getId());
        int rowsAffected = stmt.executeUpdate();
        if (rowsAffected > 0) {
            System.out.println("Demande de dette mise à jour.");
        } else {
            System.out.println("Erreur lors de la mise à jour de la demande de dette.");
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
}

public List<Article> getArticlesByDemandeId(int demandeId) {
    List<Article> articles = new ArrayList<>();
    String sql = "SELECT a.id, a.name, a.price, a.qte_stock " +
                 "FROM articles a " +
                 "JOIN demande_dette_articles dda ON a.id = dda.article_id " +
                 "WHERE dda.demande_id = ?";
    try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
         PreparedStatement stmt = connection.prepareStatement(sql)) {
        
        stmt.setInt(1, demandeId);  // Paramètre : l'ID de la demande
        
        try (ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                // Construire un objet Article pour chaque ligne
                Article article = new Article();
                article.setId(rs.getInt("id"));
                article.setNom(rs.getString("name"));
                article.setPrix(rs.getDouble("price"));
                article.setQuantiteStock(rs.getInt("qte_stock"));
                articles.add(article);
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return articles;
}



@Override
public void removeArticlesFromDemande(int demandeDetteId, List<Integer> articleIds) {
    String deleteQuery = "DELETE FROM demande_dette_articles WHERE demande_dette_id = ? AND article_id = ?";
    
    try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
         PreparedStatement stmt = connection.prepareStatement(deleteQuery)) {
        for (Integer articleId : articleIds) {
            stmt.setInt(1, demandeDetteId);
            stmt.setInt(2, articleId);
            stmt.executeUpdate();
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
}
    // Méthodes auxiliaires pour récupérer un client et un article par ID
    private Client getClientById(int clientId) {
        // Implémente cette méthode pour récupérer un client à partir de son ID
        // Cela pourrait ressembler à une autre requête SQL pour trouver le client
        return new Client(); // A implémenter selon ta logique
    }

    private Article getArticleById(int articleId) {
        // Implémente cette méthode pour récupérer un article à partir de son ID
        // Cela pourrait ressembler à une autre requête SQL pour trouver l'article
        return new Article(); // A implémenter selon ta logique
    }

    @Override
    public void delete(int id) {
        String deleteArticlesQuery = "DELETE FROM demande_dette_articles WHERE demande_dette_id = ?";
        String deleteDemandeQuery = "DELETE FROM demandes_dette WHERE id = ?";
        
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            // Supprimer les articles associés à la demande de dette
            try (PreparedStatement stmt = connection.prepareStatement(deleteArticlesQuery)) {
                stmt.setInt(1, id);
                stmt.executeUpdate();
            }
    
            // Supprimer la demande de dette
            try (PreparedStatement stmt = connection.prepareStatement(deleteDemandeQuery)) {
                stmt.setInt(1, id);
                stmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
  
    @Override
public void addArticlesToDemande(int demandeDetteId, List<Integer> articleIds) {
    String insertQuery = "INSERT INTO demande_dette_articles (demande_dette_id, article_id) VALUES (?, ?)";
    
    try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
         PreparedStatement stmt = connection.prepareStatement(insertQuery)) {
        for (Integer articleId : articleIds) {
            stmt.setInt(1, demandeDetteId);
            stmt.setInt(2, articleId);
            stmt.executeUpdate();
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
}

public List<DemandeDette> findDemandesEnAttente() {
    List<DemandeDette> demandes = new ArrayList<>();
    String query = "SELECT * FROM demandes_dette WHERE status = 'En attente'"; // Filtrage par statut "En attente"
    
    try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
         Statement stmt = connection.createStatement();
         ResultSet rs = stmt.executeQuery(query)) {
        while (rs.next()) {
            DemandeDette demande = new DemandeDette();
            demande.setId(rs.getInt("id"));
            demande.setClientId(rs.getInt("client_id"));
            demande.setMontant(rs.getDouble("montant"));
            demande.setStatus(rs.getString("status"));
            demande.setDate(rs.getDate("date"));
            
            demandes.add(demande);
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    
    return demandes;
}

}

