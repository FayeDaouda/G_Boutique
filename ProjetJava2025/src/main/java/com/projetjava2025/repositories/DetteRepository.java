package com.projetjava2025.repositories;

import com.projetjava2025.entities.Dette;
import java.util.List;

public interface DetteRepository {
    void create(Dette dette);                   // Créer une dette
    List<Dette> findAll();                      // Trouver toutes les dettes
    Dette findById(int id);                     // Trouver une dette par son ID
    List<Dette> findByClientId(int clientId);   // Trouver des dettes par client ID
    void update(Dette dette);                   // Mettre à jour une dette
    void delete(int id);                        // Supprimer une dette
    List<Dette> findNonSoldedByClientPhone(String phone);
    
    
    

}
