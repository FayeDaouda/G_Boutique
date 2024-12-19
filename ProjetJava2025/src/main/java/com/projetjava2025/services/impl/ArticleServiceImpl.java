package com.projetjava2025.services.impl;

import com.projetjava2025.entities.Article;
import com.projetjava2025.repositories.ArticleRepository;
import com.projetjava2025.services.ArticleService;
import java.util.List;

public class ArticleServiceImpl implements ArticleService {

    private final ArticleRepository articleRepository;

    public ArticleServiceImpl(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    @Override
    public List<Article> getAllArticles() {
        return articleRepository.findAll();
    }

    @Override
    public Article getArticleById(int id) {
        return articleRepository.findById(id);
    }

    @Override
    public void saveArticle(Article article) {
        articleRepository.save(article);
    }

    @Override
    public void deleteArticle(int id) {
        articleRepository.delete(id);
    }
}
