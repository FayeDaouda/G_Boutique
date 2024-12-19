package com.projetjava2025.repositories;

import com.projetjava2025.entities.Article;
import java.util.List;

public interface ArticleRepository {
    void create(Article article);                   // Créer un article
    List<Article> findAll();                        // Trouver tous les articles
    Article findById(int id);                       // Trouver un article par son ID
    void update(Article article);                   // Mettre à jour un article
    void delete(int id);                            // Supprimer un article
}
