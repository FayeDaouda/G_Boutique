package com.projetjava2025.entities;

import java.util.Date;
import java.util.List;

public class Dette {
    private int id;
    private int clientId;
    private Client client;
    private Date date;
    private double montant;
    private double montantVerser;
    private double montantRestant;

    // Constructeur par défaut
    public Dette() {
        // Laisser vide ou initialiser les valeurs par défaut si nécessaire
    }

    // Constructeur avec des paramètres
    public Dette(String date, double montant, double montantVerser, double montantRestant, Client client, List<Article> articles) {
        this.date = java.sql.Date.valueOf(date);  // Assurez-vous que la date est au bon format
        this.montant = montant;
        this.montantVerser = montantVerser;
        this.montantRestant = montantRestant;
        this.clientId = client.getId();
        // Gérer les articles ici si nécessaire
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

    public double getMontantVerser() {
        return montantVerser;
    }

    public void setMontantVerser(double montantVerser) {
        this.montantVerser = montantVerser;
    }

    public double getMontantRestant() {
        return montantRestant;
    }

    public void setMontantRestant(double montantRestant) {
        this.montantRestant = montantRestant;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }
}
