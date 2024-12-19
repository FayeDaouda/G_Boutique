package com.projetjava2025.services;

import com.projetjava2025.entities.DemandeDette;
import java.util.List;

public interface DemandeDetteService {
    List<DemandeDette> getAllDemandes();    // Récupérer toutes les demandes
    DemandeDette getDemandeById(int id);    // Récupérer une demande de dette par son ID
    void saveDemande(DemandeDette demande); // Sauvegarder une demande de dette
    void deleteDemande(int id);             // Supprimer une demande de dette par son ID
    List<DemandeDette> getDemandesByClient(int clientId); // Récupérer les demandes d'un client
}
