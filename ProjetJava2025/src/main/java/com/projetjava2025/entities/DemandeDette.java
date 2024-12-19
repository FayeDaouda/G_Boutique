package com.projetjava2025.entities;

import java.util.Date;
import java.util.List;

public class DemandeDette {
    private int id;
    private int clientId;
    private Date date;
    private double montant;
    private String status;  // Renommé de "etat" à "status"
    private List<Article> articles; // Liste d'articles associés à cette demande de dette

    // Constructeur par défaut
    public DemandeDette() {}

    // Constructeur avec tous les paramètres
    public DemandeDette(int id, int clientId, Date date, double montant, String status, List<Article> articles) {
        this.id = id;
        this.clientId = clientId;
        this.date = date;
        this.montant = montant;
        this.status = status;  // Modifié de "etat" à "status"
        this.articles = articles;
    }

    // Getters et Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getClientId() {
        return clientId;
    }

    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public double getMontant() {
        return montant;
    }

    public void setMontant(double montant) {
        this.montant = montant;
    }

    public String getStatus() {
        return status;  // Modifié de "getEtat" à "getStatus"
    }

    public void setStatus(String status) {
        this.status = status;  // Modifié de "setEtat" à "setStatus"
    }

    public List<Article> getArticles() {
        return articles;
    }

    public void setArticles(List<Article> articles) {
        this.articles = articles;
    }

    @Override
public String toString() {
    return "DemandeDette [ID=" + id + ", ClientID=" + clientId +
           ", Montant=" + montant + ", Status=" + status +
           ", Nombre d'articles=" + (articles != null ? articles.size() : 0) + "]";
}

}
