package com.projetjava2025.entities;

public class Client {
    private int id;
    private String surname;
    private String phone;
    private String address;
    private boolean hasAccount;
    private double cumulMontant; // Nouveau champ pour stocker le montant cumulé

    // Constructeur avec les paramètres : id, surname, phone, address, hasAccount
    public Client(int id, String surname, String phone, String address, boolean hasAccount) {
        this.id = id;
        this.surname = surname;
        this.phone = phone;
        this.address = address;
        this.hasAccount = hasAccount;
    }
    // Nouveau constructeur avec 3 paramètres : surname, phone, address
public Client(String surname, String phone, String address) {
    this.surname = surname;
    this.phone = phone;
    this.address = address;
    this.hasAccount = false; // Par défaut, le compte utilisateur est non créé
}

    // Nouveau constructeur avec les paramètres : surname, phone, address, hasAccount
    public Client(String surname, String phone, String address, boolean hasAccount) {
        this.surname = surname;
        this.phone = phone;
        this.address = address;
        this.hasAccount = hasAccount;
    }

    // Constructeur par défaut
    public Client() {
    }

    // Getters et setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public boolean isHasAccount() {
        return hasAccount;
    }

    public void setHasAccount(boolean hasAccount) {
        this.hasAccount = hasAccount;
    }
    // Getter pour le montant cumulé
    public double getCumulMontant() {
        return cumulMontant;
    }

    // Setter pour le montant cumulé
    public void setCumulMontant(double cumulMontant) {
        this.cumulMontant = cumulMontant;
    }

    // Surcharge de la méthode toString pour une représentation plus lisible
    @Override
    public String toString() {
        return "Client{id=" + id + ", surname='" + surname + "', phone='" + phone + "', address='" + address + "', hasAccount=" + hasAccount + "}";
    }
}
