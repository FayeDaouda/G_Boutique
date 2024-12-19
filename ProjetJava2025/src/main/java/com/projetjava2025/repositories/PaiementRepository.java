package com.projetjava2025.repositories;

import com.projetjava2025.entities.Paiement;
import java.util.List;

public interface PaiementRepository {
    void create(Paiement paiement);
    List<Paiement> findAll();
    Paiement findById(int id);
    List<Paiement> findByDetteId(int detteId);
    void update(Paiement paiement);
    void delete(int id);
}
