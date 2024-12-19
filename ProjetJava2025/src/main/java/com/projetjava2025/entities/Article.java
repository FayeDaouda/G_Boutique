package com.projetjava2025.entities;

public class Article {
    private int id;
    private String nom;           // correspond à "name" dans la BD
    private double prix;          // correspond à "price" dans la BD
    private int quantiteStock;    // correspond à "qte_stock" dans la BD

    // Constructeur avec tous les paramètres
    public Article(int id, String nom, double prix, int quantiteStock) {
        this.id = id;
        this.nom = nom;
        this.prix = prix;
        this.quantiteStock = quantiteStock;
    }

    // Constructeur sans ID (si l'ID est généré automatiquement)
    public Article(String nom, double prix, int quantiteStock) {
        this.nom = nom;
        this.prix = prix;
        this.quantiteStock = quantiteStock;
    }

    // Constructeur sans argument
    public Article() {
        // Ce constructeur est nécessaire pour l'initialisation sans arguments
    }

    // Getters et Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public double getPrix() {
        return prix;
    }

    public void setPrix(double prix) {
        this.prix = prix;
    }

    public int getQuantiteStock() {
        return quantiteStock;
    }

    public void setQuantiteStock(int quantiteStock) {
        this.quantiteStock = quantiteStock;
    }

    @Override
    public String toString() {
        return "Article{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", prix=" + prix +
                ", quantiteStock=" + quantiteStock +
                '}';
    }
}
