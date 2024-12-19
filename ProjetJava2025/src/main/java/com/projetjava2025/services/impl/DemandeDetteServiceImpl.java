package com.projetjava2025.services.impl;

import com.projetjava2025.entities.DemandeDette;
import com.projetjava2025.repositories.DemandeDetteRepository;
import com.projetjava2025.services.DemandeDetteService;
import java.util.List;

public class DemandeDetteServiceImpl implements DemandeDetteService {

    private final DemandeDetteRepository demandeDetteRepository;

    public DemandeDetteServiceImpl(DemandeDetteRepository demandeDetteRepository) {
        this.demandeDetteRepository = demandeDetteRepository;
    }

    @Override
    public List<DemandeDette> getAllDemandes() {
        return demandeDetteRepository.findAll();
    }

    @Override
    public DemandeDette getDemandeById(int id) {
        return demandeDetteRepository.findById(id);
    }

    @Override
    public void saveDemande(DemandeDette demande) {
        demandeDetteRepository.save(demande);
    }

    @Override
    public void deleteDemande(int id) {
        demandeDetteRepository.delete(id);
    }

    @Override
    public List<DemandeDette> getDemandesByClient(int clientId) {
        return demandeDetteRepository.findByClientId(clientId);
    }
}
