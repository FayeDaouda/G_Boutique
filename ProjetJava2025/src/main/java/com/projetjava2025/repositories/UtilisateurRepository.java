package com.projetjava2025.repositories;

import com.projetjava2025.entities.Utilisateur;
import java.util.List;

public interface UtilisateurRepository {
    void create(Utilisateur utilisateur);
    List<Utilisateur> findAll();
    Utilisateur findById(int id);
    Utilisateur findByLogin(String login);
    Utilisateur findByEmail(String email);  // Méthode pour rechercher un utilisateur par email
    Utilisateur findByLoginAndPassword(String login, String password); // Ajout de cette méthode
    void update(Utilisateur utilisateur);
    void delete(int id);
    List<Utilisateur> findByRole(String role); // Pour récupérer les utilisateurs par rôle (Admin, Client, Boutiquier)
    List<Utilisateur> findActiveUsers();       // Pour récupérer uniquement les utilisateurs actifs
    void updateStatus(int id, boolean isActive);

    
}
