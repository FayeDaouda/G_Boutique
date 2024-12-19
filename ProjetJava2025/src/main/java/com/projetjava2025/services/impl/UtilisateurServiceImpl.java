package com.projetjava2025.services.impl;

import com.projetjava2025.entities.Utilisateur;
import com.projetjava2025.repositories.UtilisateurRepository;
import com.projetjava2025.services.UtilisateurService;
import java.util.List;

public class UtilisateurServiceImpl implements UtilisateurService {

    private final UtilisateurRepository utilisateurRepository;

    public UtilisateurServiceImpl(UtilisateurRepository utilisateurRepository) {
        this.utilisateurRepository = utilisateurRepository;
    }

    @Override
    public List<Utilisateur> getAllUtilisateurs() {
        return utilisateurRepository.findAll();
    }

    @Override
    public Utilisateur getUtilisateurById(int id) {
        return utilisateurRepository.findById(id);
    }

    @Override
    public void saveUtilisateur(Utilisateur utilisateur) {
        // Vérification si un utilisateur avec le même email existe déjà
        if (utilisateurRepository.findByEmail(utilisateur.getEmail()) != null) {
            throw new IllegalArgumentException("Un utilisateur avec cet email existe déjà.");
        }

        // Vérification si un utilisateur avec le même login existe déjà
        if (utilisateurRepository.findByLogin(utilisateur.getLogin()) != null) {
            throw new IllegalArgumentException("Un utilisateur avec ce login existe déjà.");
        }

        // Si aucune duplication n'est trouvée, on enregistre l'utilisateur
        utilisateurRepository.create(utilisateur);
    }

    @Override
    public void deleteUtilisateur(int id) {
        utilisateurRepository.delete(id);
    }

    @Override
    public Utilisateur getUtilisateurByLogin(String login) {
        return utilisateurRepository.findByLogin(login);
    }
}
