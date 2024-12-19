package com.projetjava2025.repositories;

import com.projetjava2025.entities.Article;
import com.projetjava2025.entities.DemandeDette;
import java.util.List;

public interface DemandeDetteRepository {
    void create(DemandeDette demandeDette);                
    List<DemandeDette> findAll();                           
    DemandeDette findById(int id); 
    void update(DemandeDette demandeDette);    
    void delete(int id);                                    
    void addArticlesToDemande(int demandeDetteId, List<Integer> articleIds);  
    void removeArticlesFromDemande(int demandeDetteId, List<Integer> articleIds); 
    List<DemandeDette> findDemandesEnAttente();
    List<DemandeDette> findAllNonArchivees();
    List<Article> getArticlesByDemandeId(int demandeId); 
}
