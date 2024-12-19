package com.projetjava2025.entities;

public class Utilisateur {
    private int id;
    private String email;
    private String login;
    private String password;
    private String role; // Role : "Boutiquier", "Admin", "Client"
    private boolean isActive; // Nouveau champ pour l'état actif/inactif
    private Client client; // Association avec un Client

    // Constructeur avec paramètres
    public Utilisateur(int id, String email, String login, String password, String role, boolean isActive) {
        this.id = id;
        this.email = email;
        this.login = login;
        this.password = password;
        this.role = role;
        this.isActive = isActive; // Initialisation de l'état actif
    }
    // Nouveau constructeur avec 4 paramètres : email, login, password, role
public Utilisateur(String email, String login, String password, String role) {
    this.email = email;
    this.login = login;
    this.password = password;
    this.role = role;
    this.isActive = true; // Par défaut, l'utilisateur est actif
}


    // Constructeur par défaut (sans paramètres)
    public Utilisateur() {
        // Initialiser les valeurs par défaut, si nécessaire
    }

    // Getters et Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean isActive) {
        this.isActive = isActive;
    }

    // Getter et Setter pour Client
    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }


    // Méthode toString
    @Override
    public String toString() {
        return "Utilisateur{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", login='" + login + '\'' +
                ", password='" + password + '\'' +
                ", role='" + role + '\'' +
                ", isActive=" + isActive +
                ", client=" + (client != null ? client.toString() : "Aucun client associé") +
                '}';
    }
}
