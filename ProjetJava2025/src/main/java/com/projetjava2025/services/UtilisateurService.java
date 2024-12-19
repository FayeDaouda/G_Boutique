package com.projetjava2025.services;

import com.projetjava2025.entities.Utilisateur;
import java.util.List;

public interface UtilisateurService {
    List<Utilisateur> getAllUtilisateurs();     // Récupérer tous les utilisateurs
    Utilisateur getUtilisateurById(int id);     // Récupérer un utilisateur par son ID
    void saveUtilisateur(Utilisateur utilisateur);  // Sauvegarder un utilisateur
    void deleteUtilisateur(int id);             // Supprimer un utilisateur par son ID
    Utilisateur getUtilisateurByLogin(String login); // Récupérer un utilisateur par son login
    
}
