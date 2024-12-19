package com.projetjava2025.services;

import com.projetjava2025.entities.Article;
import java.util.List;

public interface ArticleService {
    List<Article> getAllArticles();
    Article getArticleById(int id);
    void saveArticle(Article article);
    void deleteArticle(int id);
}
