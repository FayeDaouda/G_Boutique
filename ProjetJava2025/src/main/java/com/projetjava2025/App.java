package com.projetjava2025;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;  // Ajoutez cette ligne pour importer Scanner

import com.projetjava2025.entities.Utilisateur;  // Ajoutez cette ligne
import com.projetjava2025.repositories.UtilisateurRepository;
import com.projetjava2025.repositories.impl.UtilisateurRepositoryImpl;
import com.projetjava2025.repositories.ArticleRepository;
import com.projetjava2025.repositories.ClientRepository;
import com.projetjava2025.repositories.impl.ClientRepositoryImpl;
import com.projetjava2025.repositories.impl.ArticleRepositoryImpl;
import com.projetjava2025.entities.Article;
import com.projetjava2025.entities.Client;
import com.projetjava2025.repositories.DemandeDetteRepository;
import com.projetjava2025.repositories.DetteRepository;
import com.projetjava2025.repositories.PaiementRepository;
import com.projetjava2025.repositories.impl.PaiementRepositoryImpl;
import com.projetjava2025.repositories.impl.DemandeDetteRepositoryImpl;
import com.projetjava2025.repositories.impl.DetteRepositoryImpl;
import com.projetjava2025.entities.DemandeDette;
import com.projetjava2025.entities.Dette;
import com.projetjava2025.entities.Paiement;



public class App {

    private static Utilisateur utilisateurConnecte; // Variable globale pour stocker l'utilisateur connecté
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        UtilisateurRepository utilisateurRepository = new UtilisateurRepositoryImpl();
        ArticleRepository articleRepository = new ArticleRepositoryImpl();
        DemandeDetteRepository demandeDetteRepository = new DemandeDetteRepositoryImpl();
        DetteRepository detteRepository = new DetteRepositoryImpl(); 
        ClientRepository clientRepository = new ClientRepositoryImpl(); // Ajoutez ce repository
        PaiementRepository paiementRepository = new PaiementRepositoryImpl();
       


        // Demander les informations de connexion uniquement lors de la première connexion
        if (utilisateurConnecte == null) {
            System.out.println("Veuillez entrer votre login :");
            String login = scanner.nextLine();
            System.out.println("Veuillez entrer votre mot de passe :");
            String password = scanner.nextLine();

            // Vérifier la connexion de l'utilisateur
            utilisateurConnecte = utilisateurRepository.findByLoginAndPassword(login, password);

            // Si l'utilisateur n'est pas trouvé ou mot de passe incorrect
            if (utilisateurConnecte == null) {
                System.out.println("Erreur : Login ou mot de passe incorrect.");
                return; // Arrêter l'exécution si l'authentification échoue
            } else {
                System.out.println("Connexion réussie ! Bienvenue " + utilisateurConnecte.getLogin());
            }
        }

        // Diriger l'utilisateur vers sa vue en fonction de son rôle
        switch (utilisateurConnecte.getRole()) {
            case "Admin":
                afficherVueAdmin(scanner, utilisateurRepository, articleRepository, demandeDetteRepository, detteRepository, clientRepository);
                break;
            case "Client":
                afficherVueClient(scanner, articleRepository, demandeDetteRepository, clientRepository);
                break;
            case "Boutiquier":
                afficherVueBoutiquier(scanner,clientRepository, detteRepository, articleRepository, utilisateurRepository, demandeDetteRepository,paiementRepository);
                break;
            default:
                System.out.println("Erreur : Rôle inconnu.");
        }

        scanner.close();
    }


    // Vue Admin
    private static void afficherVueAdmin(Scanner scanner, UtilisateurRepository utilisateurRepository, ArticleRepository articleRepository, DemandeDetteRepository demandeDetteRepository, DetteRepository detteRepository, ClientRepository clientRepository) {
        boolean continuer = true;
    
        while (continuer) {
            System.out.println("\n--- Vue Admin ---");
            System.out.println("1. Gérer les utilisateurs");
            System.out.println("2. Gérer les produits");
            System.out.println("3. Gérer les demandes");
            System.out.println("4. Se déconnecter");
            System.out.print("Choisissez une option : ");
            int choix = scanner.nextInt();
            scanner.nextLine(); // Consomme la nouvelle ligne après le choix
    
            switch (choix) {
                case 1:
                    gererUtilisateurs(scanner, utilisateurRepository);
                    break;
                case 2:
                    gererProduits(scanner, articleRepository);
                    break;
                case 3:
                    gererDemandes(scanner, demandeDetteRepository, utilisateurRepository, articleRepository, detteRepository, clientRepository);
                    break;
                case 4:
                    System.out.println("Déconnexion...");
                    continuer = false; // Sortir de la boucle pour terminer la session Admin
                    break;
                default:
                    System.out.println("Choix invalide. Veuillez réessayer.");
            }
        }
    }
    
        private static void gererUtilisateurs(Scanner scanner, UtilisateurRepository utilisateurRepository) {
            System.out.println("\n--- Gestion des utilisateurs ---");
            System.out.println("1. Créer un utilisateur");
            System.out.println("2. Activer/Désactiver un utilisateur");
            System.out.println("3. Afficher les utilisateurs");
            System.out.println("4. Retour");
            System.out.print("Choisissez une option : ");
        
            int choix = scanner.nextInt();
            scanner.nextLine(); // Consomme la nouvelle ligne après le choix
        
            switch (choix) {
                case 1:
                    System.out.println("Création d'un utilisateur...");
                    creerUtilisateur(scanner, utilisateurRepository);
                    break;
        
                case 2:
                    activerDesactiverUtilisateur(scanner, utilisateurRepository);
                    break;
        
                case 3:
                    afficherTousLesUtilisateurs(utilisateurRepository);
                    break;
        
                case 4:
                    // Retour au menu Admin
                    System.out.println("Retour au menu Admin...");
                    return; // Sortir de la méthode pour retourner au menu Admin
                default:
                    System.out.println("Choix invalide.");
                    break;
            }
        
            // Après avoir exécuté une action, revenir au menu Gestion des utilisateurs
            gererUtilisateurs(scanner, utilisateurRepository);
        }
        
        private static void gererProduits(Scanner scanner, ArticleRepository articleRepository) {
            while (true) {  // Boucle pour garder l'utilisateur dans le menu jusqu'à ce qu'il choisisse de quitter
                System.out.println("\n--- Gestion des produits ---");
                System.out.println("1. Créer un produit");
                System.out.println("2. Mettre à jour la quantité d'un produit");
                System.out.println("3. Afficher les produits disponibles");
                System.out.println("4. Retour au menu précédent");
                System.out.println("5. Quitter");
                System.out.print("Choisissez une option : ");
        
                int choix = scanner.nextInt();
                scanner.nextLine(); // Consomme la nouvelle ligne après le choix
        
                switch (choix) {
                    case 1:
                        System.out.println("Création d'un produit...");
                        creerProduit(scanner, articleRepository);
                        break;
                    case 2:
                        System.out.println("Mise à jour de la quantité d'un produit...");
                        mettreAJourQuantiteProduit(scanner, articleRepository);
                        break;
                    case 3:
                        System.out.println("Affichage des produits disponibles...");
                        afficherProduits(articleRepository);
                        break;
                    case 4:
                        System.out.println("Retour au menu Admin...");
                        
                        return;  // Retour à la méthode appelante (menu admin)
                    case 5:
                        System.out.println("Quitter...");
                        System.exit(0);  // Quitte l'application
                        break;
                    default:
                        System.out.println("Choix invalide.");
                }
            }
        }
        

        private static void gererDemandes(Scanner scanner, DemandeDetteRepository demandeDetteRepository,UtilisateurRepository utilisateurRepository,ArticleRepository articleRepository, DetteRepository detteRepository,ClientRepository clientRepository) {
            while (true) {
                System.out.println("\n--- Gestion des demandes de dettes ---");
                System.out.println("1. Afficher les demandes de dettes");
                System.out.println("2. Valider une demande de dette");
                System.out.println("3. Archiver une demande de dette");
                System.out.println("4. Retour");
                System.out.print("Choisissez une option : ");
                
                int choix = scanner.nextInt();
            scanner.nextLine(); // Consomme la nouvelle ligne après le choix
                
                switch (choix) {
                    case 1:
                        System.out.println("Affichage des demandes de dettes...");
                        afficherDemandes(demandeDetteRepository);  // Affiche les demandes
                        break;
                    case 2:
                        System.out.println("Validation d'une demande de dette...");
                        validerDemande(scanner, demandeDetteRepository , detteRepository,clientRepository);  // Valider une demande
                        break;
                    case 3:
                        System.out.println("Archivage d'une demande de dette...");
                        archiverDemande(scanner, demandeDetteRepository);  // Archiver une demande
                        break;
                    case 4:
                    System.out.println("Retour au menu Admin...");
                    afficherVueAdmin(scanner, utilisateurRepository, articleRepository, demandeDetteRepository, detteRepository, clientRepository);
                    break; // Sortir du switch
                    default:
                        System.out.println("Choix invalide.");
                }
            }
        }
        

    // Vue Client
private static void afficherVueClient(Scanner scanner, ArticleRepository articleRepository, DemandeDetteRepository demandeDetteRepository, ClientRepository clientRepository) {
    boolean continuer = true;
    while (continuer) {
        System.out.println("\n--- Vue Client ---");
        System.out.println("1. Consulter les produits");
        System.out.println("2. Faire une Demande");
        System.out.println("3. Consulter mon profil");
        System.out.println("4. Se déconnecter");
        System.out.print("Choisissez une option : ");

        int choix = scanner.nextInt();
        scanner.nextLine(); // Consommer la nouvelle ligne après le choix

        switch (choix) {
            case 1:
                System.out.println("\n--- Produits disponibles ---");
                afficherProduits(articleRepository); // Méthode pour afficher les produits
                break;
            case 2:
                System.out.println("\n--- Faire une Demande ---");
                faireCommande(scanner, articleRepository, demandeDetteRepository, clientRepository,utilisateurConnecte);
                break;
            case 3:
                System.out.println("\n--- Mon Profil ---");
                afficherProfilUtilisateur(utilisateurConnecte);
                
                faireCommande(scanner, articleRepository, demandeDetteRepository, clientRepository,utilisateurConnecte);
                break;
            case 4:
                System.out.println("Déconnexion...");
                continuer = false; // Sortir de la boucle
                break;
            default:
                System.out.println("Choix invalide, veuillez réessayer.");
        }
    }
}

    // Vue Boutiquier
private static void afficherVueBoutiquier(Scanner scanner, ClientRepository clientRepository, DetteRepository detteRepository, ArticleRepository articleRepository, UtilisateurRepository utilisateurRepository,DemandeDetteRepository demandeDetteRepository,PaiementRepository paiementRepository) {
    boolean continuer = true;

    while (continuer) {
        System.out.println("\n--- Vue Boutiquier ---");
        System.out.println("1. Créer un client");
        System.out.println("2. Lister les clients");
        System.out.println("3. Rechercher un client par téléphone");
        System.out.println("4. Créer une dette");
        System.out.println("5. Enregistrer un paiement");
        System.out.println("6. Lister les dettes non soldées");
        System.out.println("7. Lister les demandes de dette en cours");
        System.out.println("8. Se déconnecter");
        System.out.print("Choisissez une option : ");

        int choix = scanner.nextInt();
        scanner.nextLine(); // Consommer la nouvelle ligne après le choix

        switch (choix) {
            case 1:
                creerClient(scanner, clientRepository, utilisateurRepository);
                break;
            case 2:
                listerClients(scanner, clientRepository);
                break;
            case 3:
                rechercherClientParTelephone(scanner, clientRepository);
                break;
            case 4:
                creerDette(scanner, detteRepository, clientRepository, articleRepository);
                break;
            case 5:
                enregistrerPaiement(scanner, detteRepository, paiementRepository);
                break;
            case 6:
                listerDettesNonSoldees(scanner, detteRepository);
                break;
            case 7:
                listerDemandesDetteEnAttente(scanner, demandeDetteRepository); 
                break;
            case 8:
                System.out.println("Déconnexion...");
                continuer = false;
                break;
            default:
                System.out.println("Choix invalide. Veuillez réessayer.");
        }
    }
}

    private static void creerUtilisateur(Scanner scanner, UtilisateurRepository utilisateurRepository) {
        System.out.println("\n--- Création d'un utilisateur ---");
    
        // Créer une boucle pour permettre à l'utilisateur de ressaisir en cas d'erreur
        boolean utilisateurCreer = false;
        while (!utilisateurCreer) {
            // Récupérer les informations de l'utilisateur
            System.out.print("Entrez l'email : ");
            String email = scanner.nextLine();
    
            System.out.print("Entrez le login : ");
            String login = scanner.nextLine();
    
            System.out.print("Entrez le mot de passe : ");
            String password = scanner.nextLine();
    
            System.out.println("Choisissez un rôle : ");
            System.out.println("1. Admin");
            System.out.println("2. Client");
            System.out.println("3. Boutiquier");
            System.out.print("Entrez votre choix : ");
            int choixRole = scanner.nextInt();
            scanner.nextLine(); // Consommer le saut de ligne
    
            String role;
            switch (choixRole) {
                case 1:
                    role = "Admin";
                    break;
                case 2:
                    role = "Client";
                    break;
                case 3:
                    role = "Boutiquier";
                    break;
                default:
                    System.out.println("Rôle invalide. Veuillez réessayer.");
                    continue;  // Redemander l'entrée du rôle
            }
    
            // Vérifier si l'email ou le login existe déjà
            Utilisateur existingUserByEmail = utilisateurRepository.findByEmail(email);
            Utilisateur existingUserByLogin = utilisateurRepository.findByLogin(login);
    
            if (existingUserByEmail != null) {
                System.out.println("Erreur : Un utilisateur avec cet email existe déjà !");
                System.out.println("Voulez-vous saisir un autre email ? (oui/non)");
                String reponse = scanner.nextLine();
                if (reponse.equalsIgnoreCase("non")) {
                    System.out.println("Opération annulée.");
                    return;  // Sortie de la méthode, retour au menu principal
                }
                continue; // Redemander un autre email
            } else if (existingUserByLogin != null) {
                System.out.println("Erreur : Un utilisateur avec ce login existe déjà !");
                continue; // Redemander un autre login
            } else {
                // Créer un nouvel utilisateur si les données sont valides
                Utilisateur nouvelUtilisateur = new Utilisateur(0, email, login, password, role, true);  // Utilisation de 6 paramètres, avec isActive défini sur true
    
                // Appeler la méthode create pour insérer dans la base de données
                utilisateurRepository.create(nouvelUtilisateur);
    
                System.out.println("Utilisateur créé avec succès !");
                utilisateurCreer = true;  // Sortir de la boucle car l'utilisateur a été créé avec succès
            }
        }
    }
    

    private static void activerDesactiverUtilisateur(Scanner scanner, UtilisateurRepository utilisateurRepository) {
        System.out.println("\n--- Activation/Désactivation d'un utilisateur ---");
    
        System.out.print("Entrez l'ID de l'utilisateur à activer/désactiver : ");
        int id = scanner.nextInt();
        scanner.nextLine(); // Consommer le saut de ligne
    
        Utilisateur utilisateur = utilisateurRepository.findById(id);
        if (utilisateur == null) {
            System.out.println("Erreur : Aucun utilisateur trouvé avec cet ID.");
        } else {
            boolean nouvelEtat = !utilisateur.isActive();
            utilisateurRepository.updateStatus(id, nouvelEtat);
    
            String statut = nouvelEtat ? "activé" : "désactivé";
            System.out.println("L'utilisateur avec l'ID " + id + " a été " + statut + ".");
        }
    }

    private static void afficherTousLesUtilisateurs(UtilisateurRepository utilisateurRepository) {
        System.out.println("\n--- Liste des utilisateurs ---");
        List<Utilisateur> utilisateurs = utilisateurRepository.findAll();
    
        if (utilisateurs.isEmpty()) {
            System.out.println("Aucun utilisateur trouvé.");
        } else {
            for (Utilisateur utilisateur : utilisateurs) {
                System.out.println("ID: " + utilisateur.getId());
                System.out.println("Email: " + utilisateur.getEmail());
                System.out.println("Login: " + utilisateur.getLogin());
                System.out.println("Rôle: " + utilisateur.getRole());
                System.out.println("Statut: " + (utilisateur.isActive() ? "Actif" : "Inactif"));
                System.out.println("-------------------------");
            }
        }
    }

    private static void afficherProfilUtilisateur(Utilisateur utilisateurConnecte) {
        if (utilisateurConnecte == null) {
            System.out.println("Aucun utilisateur connecté.");
            return;
        }
    
        System.out.println("\n--- Mon Profil ---");
        System.out.println(String.format("ID: %-5d | Email: %-20s | Login: %-10s | Rôle: %-15s | Statut: %-8s", 
                                        utilisateurConnecte.getId(), utilisateurConnecte.getEmail(), 
                                        utilisateurConnecte.getLogin(), utilisateurConnecte.getRole(), 
                                        utilisateurConnecte.isActive() ? "Actif" : "Inactif"));
        System.out.println("-------------------------");
    }
    
    
    private static void continuerOuAnnuler(Scanner scanner, UtilisateurRepository utilisateurRepository) {
        System.out.println("Voulez-vous réessayer ? (oui/non)");
        String reponse = scanner.nextLine();
        if (reponse.equalsIgnoreCase("non")) {
            System.out.println("Opération annulée.");
            gererUtilisateurs(scanner, utilisateurRepository); // Retourner au menu de gestion des utilisateurs
        }
    }
    
    private static void gestionRetourOuQuitter(Scanner scanner) {
        boolean choixValide = false;
        while (!choixValide) {
            System.out.println("\nQue voulez-vous faire ?");
            System.out.println("1. Retourner au menu principal");
            System.out.println("2. Quitter l'application");
            System.out.print("Entrez votre choix : ");
            
            int choixRetourQuitter = scanner.nextInt();  // Renommer la variable ici
            scanner.nextLine(); // Consommer le saut de ligne
    
            switch (choixRetourQuitter) {
                case 1:
                    // Retourner au menu principal
                    System.out.println("Retour au menu principal...");
                    return;  // Retour à la méthode appelante (ce qui sortira de la boucle et du menu actuel)
                case 2:
                    // Quitter l'application
                    System.out.println("Merci d'avoir utilisé l'application. À bientôt !");
                    System.exit(0);  // Quitter l'application
                    break;
                default:
                    // Si le choix est invalide, redemander
                    System.out.println("Choix invalide, veuillez réessayer.");
                    break;
            }
        }
    }
    
    private static void creerProduit(Scanner scanner, ArticleRepository articleRepository) {
        System.out.print("Entrez le nom du produit : ");
        String nom = scanner.nextLine();
        
        System.out.print("Entrez le prix du produit : ");
        double prix = scanner.nextDouble();
        scanner.nextLine(); // Consomme la nouvelle ligne
    
        System.out.print("Entrez la quantité en stock : ");
        int quantite = scanner.nextInt();
        scanner.nextLine(); // Consomme la nouvelle ligne
    
        Article nouvelArticle = new Article(nom, prix, quantite);
        articleRepository.create(nouvelArticle); // Sauvegarde dans la base de données
        System.out.println("Produit créé avec succès !");
    }

    private static void mettreAJourQuantiteProduit(Scanner scanner, ArticleRepository articleRepository) {
        System.out.print("Entrez l'ID du produit : ");
        int id = scanner.nextInt();
        scanner.nextLine(); // Consomme la nouvelle ligne
    
        Article article = articleRepository.findById(id);
        if (article == null) {
            System.out.println("Aucun produit trouvé avec cet ID.");
            return;
        }
    
        System.out.println("Produit sélectionné : " + article.getNom());
        System.out.print("Entrez la nouvelle quantité en stock : ");
        int nouvelleQuantite = scanner.nextInt();
        scanner.nextLine(); // Consomme la nouvelle ligne
    
        article.setQuantiteStock(nouvelleQuantite);
        articleRepository.update(article); // Mise à jour dans la base de données
        System.out.println("Quantité mise à jour avec succès !");
    }

    private static void afficherProduits(ArticleRepository articleRepository) {
        List<Article> articles = articleRepository.findAll();
    
        if (articles.isEmpty()) {
            System.out.println("Aucun produit disponible.");
        } else {
            System.out.println("\n--- Liste des produits disponibles ---");
            for (Article article : articles) {
                System.out.println("ID: " + article.getId() + 
                                   ", Nom: " + article.getNom() + 
                                   ", Prix: " + article.getPrix() + 
                                   ", Quantité en stock: " + article.getQuantiteStock());
            }
        }
    }


    // Méthodes pour afficher, valider et archiver les demandes
private static void afficherDemandes(DemandeDetteRepository demandeDetteRepository) {
    // Exemple de code pour afficher toutes les demandes de dettes
    List<DemandeDette> demandes = demandeDetteRepository.findAllNonArchivees();
    if (demandes.isEmpty()) {
        System.out.println("Aucune demande de dette à afficher.");
    } else {
        for (DemandeDette demande : demandes) {
            System.out.println(demande);  // Affiche chaque demande (il faudrait que la classe DemandeDette override toString())
        }
    }
}

private static void validerDemande(Scanner scanner, DemandeDetteRepository demandeDetteRepository, DetteRepository detteRepository, ClientRepository clientRepository) {
    System.out.print("Entrez l'ID de la demande de dette à valider : ");
    int id = scanner.nextInt();
    scanner.nextLine();  // Consommer la ligne après l'ID

    DemandeDette demande = demandeDetteRepository.findById(id);
    if (demande != null) {
        if (demande.getClientId() == 0) {
            System.out.println("Erreur : L'ID du client associé à cette demande de dette est invalide.");
            return;
        }

        if ("Validée".equals(demande.getStatus())) {
            System.out.println("Erreur : La demande de dette a déjà été validée.");
            return;
        }

        try {
            // Récupérer les informations du client via son ID
            int clientId = demande.getClientId();
            Client client = clientRepository.findById(clientId);
            if (client == null) {
                System.out.println("Erreur : Aucun client trouvé avec l'ID " + clientId + ".");
                return;
            }

            // Affichage de l'ID et du nom du client pour vérification
            System.out.println("Client ID : " + clientId);
            System.out.println("Client : " + client.getSurname());  // Nom du client

            // Valider la demande de dette
            demande.setStatus("Validée");  // Changer le statut en "Validée"
            demandeDetteRepository.update(demande);  // Mettre à jour la demande dans la base

            // Créer une nouvelle dette pour le client
            Dette dette = new Dette();
            dette.setClientId(clientId);  // Lier la dette à l'ID du client
            dette.setDate(new Date());  // Utiliser la date actuelle pour la validation
            dette.setMontant(demande.getMontant());  // Le montant de la dette correspond au montant de la demande
            double montantVerser = 0;  // Au début, rien n'a été versé
            dette.setMontantVerser(montantVerser);

            // Calculer le montant restant
            double montantRestant = dette.getMontant() - montantVerser;
            dette.setMontantRestant(montantRestant);  // Calcul du montant restant

            // Enregistrer la dette dans la base de données
            detteRepository.create(dette);

            System.out.println("Demande de dette validée. Nouvelle dette créée pour le client " +
                               client.getSurname() + " (ID: " + clientId + ") " +
                               "d'un montant de " + dette.getMontant() + " avec un solde restant de " + dette.getMontantRestant());
        } catch (Exception e) {
            System.out.println("Erreur inattendue : " + e.getMessage());
            e.printStackTrace();
        }
    } else {
        System.out.println("Demande de dette introuvable.");
    }
}




private static void archiverDemande(Scanner scanner, DemandeDetteRepository demandeDetteRepository) {
    System.out.print("Entrez l'ID de la demande de dette à archiver : ");
    int id = scanner.nextInt();
    scanner.nextLine(); // Consommer la ligne après l'ID

    DemandeDette demande = demandeDetteRepository.findById(id);
    if (demande != null) {
        demande.setStatus("Archivée"); // Exemple d'archivage
        demandeDetteRepository.update(demande); // Utiliser la méthode update
        System.out.println("Demande de dette archivée !");
    } else {
        System.out.println("Demande de dette introuvable.");
    }
}







// Méthode pour afficher le profil du client
private static void afficherProfilClient(Client client) {
    System.out.println("Nom : " + client.getSurname());
    System.out.println("Adresse : " + client.getAddress());
    System.out.println("Téléphone : " + client.getPhone());
}

// Méthode pour créer la commande

private static void faireCommande(Scanner scanner, ArticleRepository articleRepository, DemandeDetteRepository demandeDetteRepository, ClientRepository clientRepository, Utilisateur utilisateurConnecte) {
    // Étape 1 : Vérifier que l'utilisateur est un client
    if ("Client".equals(utilisateurConnecte.getRole())) {
        // Récupérer le client associé à cet utilisateur
        Client client = clientRepository.findByUserId(utilisateurConnecte.getId());
        if (client == null) {
            System.out.println("Client introuvable. Veuillez réessayer.");
            return;
        }

        // Affichage du profil du client
        afficherProfilClient(client);

        // Liste des articles disponibles
        List<Article> articlesDisponibles = articleRepository.findAll();
        if (articlesDisponibles.isEmpty()) {
            System.out.println("Aucun article disponible pour le moment.");
            return;
        }

        // Afficher la liste des articles disponibles
        System.out.println("ID\tNom\tPrix\tQuantité en stock");
        for (Article article : articlesDisponibles) {
            System.out.println(article.getId() + "\t" + article.getNom() + "\t" + article.getPrix() + "\t" + article.getQuantiteStock());
        }

        // Liste pour stocker les articles et quantités choisies
        List<Article> articlesChoisis = new ArrayList<>();
        Map<Integer, Integer> quantitesParArticle = new HashMap<>();
        double montantTotal = 0;

        // Étape 2 : Ajouter des articles
        boolean continuer = true;
        while (continuer) {
            System.out.println("\nEntrez l'ID de l'article que vous souhaitez ajouter (ou -1 pour terminer) : ");
            int idArticle = scanner.nextInt();
            scanner.nextLine(); // Consommer la nouvelle ligne

            if (idArticle == -1) {
                continuer = false; // Sortir de la boucle
                break;
            }

            // Récupérer l'article
            Article article = articleRepository.findById(idArticle);
            if (article == null) {
                System.out.println("Article introuvable. Veuillez réessayer.");
                continue;
            }

            // Choisir la quantité
            System.out.println("Quantité souhaitée : ");
            int quantite = scanner.nextInt();
            scanner.nextLine(); // Consommer la nouvelle ligne

            if (quantite > 0 && quantite <= article.getQuantiteStock()) {
                // Ajouter l'article dans la liste
                articlesChoisis.add(article);
                quantitesParArticle.put(article.getId(), quantite);

                // Mettre à jour le stock temporaire
                article.setQuantiteStock(article.getQuantiteStock() - quantite);
                articleRepository.update(article); // Mise à jour de l'article existant

                // Ajouter au montant total
                montantTotal += article.getPrix() * quantite;

                System.out.println("Article ajouté avec succès !");
            } else {
                System.out.println("Quantité invalide ou insuffisante. Veuillez réessayer.");
            }
        }

        // Étape 3 : Créer la demande de dette
        if (!articlesChoisis.isEmpty()) {
            DemandeDette nouvelleDemande = new DemandeDette();
            nouvelleDemande.setClientId(client.getId()); // Utilisation de l'ID du client récupéré
            nouvelleDemande.setDate(new Date()); // Utilisation de la date actuelle
            nouvelleDemande.setMontant(montantTotal);
            nouvelleDemande.setStatus("En attente");
            nouvelleDemande.setArticles(articlesChoisis);

            // Sauvegarder la demande dans le repository
            demandeDetteRepository.create(nouvelleDemande);

            System.out.println("\nDemande créée avec succès !");
            System.out.println("Montant total : " + montantTotal + " FCFA");
            System.out.println("Statut : En attente");
        } else {
            System.out.println("Aucun article sélectionné. La demande n'a pas été créée.");
        }
    } else {
        System.out.println("Vous devez être un client pour passer une commande.");
    }
}



private static void creerClient(Scanner scanner, ClientRepository clientRepository, UtilisateurRepository utilisateurRepository) {
    System.out.println("\n--- Création d'un Client ---");

    // Informations sur le client
    System.out.print("Nom du client : ");
    String surname = scanner.nextLine();

    System.out.print("Téléphone : ");
    String phone = scanner.nextLine();

    System.out.print("Adresse : ");
    String address = scanner.nextLine();

    Client client = new Client(surname, phone, address);
    clientRepository.save(client);

    // Associer un compte utilisateur (optionnel)
    System.out.print("Voulez-vous créer un compte utilisateur pour ce client ? (oui/non) : ");
    String reponse = scanner.nextLine();

    if (reponse.equalsIgnoreCase("oui")) {
        System.out.print("Email : ");
        String email = scanner.nextLine();

        System.out.print("Login : ");
        String login = scanner.nextLine();

        System.out.print("Mot de passe : ");
        String password = scanner.nextLine();

        Utilisateur utilisateur = new Utilisateur(email, login, password, "Client");
        utilisateur.setClient(client); // Associer le client au compte utilisateur
        utilisateurRepository.create(utilisateur);

        System.out.println("Compte utilisateur créé et associé au client.");
    }

    System.out.println("Client créé avec succès.");
}

private static void listerClients(Scanner scanner, ClientRepository clientRepository) {
    System.out.println("\n--- Liste des Clients ---");

    System.out.println("1. Tous les clients");
    System.out.println("2. Clients avec comptes");
    System.out.println("3. Clients sans comptes");
    System.out.print("Choisissez une option : ");

    int choix = scanner.nextInt();
    scanner.nextLine();

    List<Client> clients;

    switch (choix) {
        case 1:
            clients = clientRepository.findAll();
            break;
        case 2:
            clients = clientRepository.findClientsWithAccounts();
            break;
        case 3:
            clients = clientRepository.findClientsWithoutAccounts();
            break;
        default:
            System.out.println("Choix invalide.");
            return;
    }

    for (Client client : clients) {
        System.out.println("Nom : " + client.getSurname());
        System.out.println("Téléphone : " + client.getPhone());
        System.out.println("Adresse : " + client.getAddress());
        System.out.println("Cumul des montants dus : " + client.getCumulMontant());
        System.out.println("----------------------------------");
    }
}

private static void rechercherClientParTelephone(Scanner scanner, ClientRepository clientRepository) {
    System.out.println("\n--- Rechercher un Client ---");

    System.out.print("Entrez le numéro de téléphone : ");
    String telephone = scanner.nextLine();

    Client client = clientRepository.findByTelephone(telephone);

    if (client != null) {
        System.out.println("Nom : " + client.getSurname());
        System.out.println("Téléphone : " + client.getPhone());
        System.out.println("Adresse : " + client.getAddress());
        System.out.println("Cumul des montants dus : " + client.getCumulMontant());
    } else {
        System.out.println("Aucun client trouvé avec ce numéro de téléphone.");
    }
}


private static void creerDette(Scanner scanner, DetteRepository detteRepository, ClientRepository clientRepository, ArticleRepository articleRepository) {
    System.out.println("\n--- Création d'une Dette ---");

    System.out.print("Numéro de téléphone du client : ");
    String phone = scanner.nextLine();
    Client client = clientRepository.findByTelephone(phone);

    if (client == null) {
        System.out.println("Client introuvable.");
        return;
    }

    System.out.print("Date (YYYY-MM-DD) : ");
    String date = scanner.nextLine();

    System.out.print("Montant total : ");
    double montant = scanner.nextDouble();
    scanner.nextLine();

    System.out.print("Montant versé : ");
    double montantVerse = scanner.nextDouble();
    scanner.nextLine();

    double montantRestant = montant - montantVerse;

    // Ajouter des articles à la dette
    List<Article> articles = new ArrayList<>();
    boolean ajouterArticles = true;

    while (ajouterArticles) {
        System.out.print("ID de l'article : ");
        int articleId = scanner.nextInt();
        scanner.nextLine();

        Article article = articleRepository.findById(articleId);

        if (article != null) {
            articles.add(article);
            System.out.println("Article ajouté.");
        } else {
            System.out.println("Article introuvable.");
        }

        System.out.print("Ajouter un autre article ? (oui/non) : ");
        String reponse = scanner.nextLine();
        ajouterArticles = reponse.equalsIgnoreCase("oui");
    }

    Dette dette = new Dette(date, montant, montantVerse, montantRestant, client, articles);
    detteRepository.create(dette);

    System.out.println("Dette créée avec succès.");
}

private static void listerDettesNonSoldees(Scanner scanner, DetteRepository detteRepository) {
    System.out.println("\n--- Liste des Dettes Non Soldées ---");

    // Récupérer toutes les dettes non soldées (montant_restante > 0)
    List<Dette> dettes = detteRepository.findAll(); // On récupère toutes les dettes

    // Vérifier si des dettes non soldées existent
    boolean foundNonSolded = false;

    // Afficher toutes les dettes non soldées
    for (Dette dette : dettes) {
        if (dette.getMontantRestant() > 0) { // Vérifie si la dette est non soldée
            foundNonSolded = true;
            System.out.println("Date : " + dette.getDate());
            System.out.println("Montant Restant : " + dette.getMontantRestant());
            System.out.println("----------------------------------");
        }
    }

    // Si aucune dette non soldée n'est trouvée
    if (!foundNonSolded) {
        System.out.println("Aucune dette non soldée dans la base de données.");
    }
}


private static void enregistrerPaiement(Scanner scanner, DetteRepository detteRepository, PaiementRepository paiementRepository) {
    System.out.println("\n--- Enregistrement d'un Paiement ---");

    // Demander à l'utilisateur les informations nécessaires
    System.out.print("Numéro de la dette (ID) : ");
    int detteId = Integer.parseInt(scanner.nextLine()); // Récupérer l'ID de la dette

    System.out.print("Montant du paiement : ");
    double montant = Double.parseDouble(scanner.nextLine()); // Récupérer le montant du paiement

    // Récupérer la dette associée à l'ID
    Dette dette = detteRepository.findById(detteId);
    if (dette == null) {
        System.out.println("Aucune dette trouvée avec l'ID " + detteId);
        return;
    }

    // Vérifier que le montant saisi est <= au montant restant de la dette
    double montantRestant = dette.getMontant() - dette.getMontantVerser(); // Calculer le montant restant
    if (montant > montantRestant) {
        System.out.println("Erreur : Le montant saisi dépasse le montant restant de la dette (Montant restant : " + montantRestant + ").");
        return;
    }

    // Ajouter le montant saisi au montant versé de la dette
    double nouveauMontantVerser = dette.getMontantVerser() + montant;
    dette.setMontantVerser(nouveauMontantVerser);
    

    // Mettre à jour la dette dans la base de données
    detteRepository.update(dette);

    // Créer un objet Paiement avec la date actuelle
    Paiement paiement = new Paiement();
    paiement.setMontant(montant);
    paiement.setDate(new Date()); // Date actuelle comme date de création
    paiement.setDette(dette); // Associer la dette au paiement

    // Enregistrer le paiement dans la base de données
    paiementRepository.create(paiement);

    System.out.println("Paiement enregistré avec succès !");
    System.out.println("Nouveau montant versé pour la dette : " + nouveauMontantVerser);
    System.out.println("Montant restant : " + (dette.getMontant() - nouveauMontantVerser));
}


public void listerDemandesDette(Scanner scanner, DetteRepository detteRepo, ArticleRepository articleRepo) {
    // Liste toutes les dettes en attente
    System.out.println("Liste des demandes de dette :");

    List<Dette> dettes = detteRepo.findAll();
    
    if (dettes.isEmpty()) {
        System.out.println("Aucune demande de dette trouvée.");
    } else {
        for (Dette dette : dettes) {
            System.out.println("ID de la dette : " + dette.getId());
            System.out.println("Montant restant : " + dette.getMontantRestant());
            System.out.println("Client : " + dette.getClient().getSurname());
            
            // Optionnel : Associer une liste d'articles à la dette (si applicable)
            List<Article> articles = articleRepo.findByDette(dette);  // Méthode à implémenter selon votre logique
            if (!articles.isEmpty()) {
                System.out.println("Articles associés :");
                for (Article article : articles) {
                    System.out.println(" - " + article.getNom() + " (Quantité : " + article.getQuantite() + ")");
                }
            }
            System.out.println("----------");
        }
    }
}

private static void listerDemandesDetteEnAttente(Scanner scanner, DemandeDetteRepository demandeDetteRepository) {
    System.out.println("\n--- Liste des Demandes de Dette En Attente ---");

    // Récupérer toutes les demandes de dette avec le statut "En attente"
    List<DemandeDette> demandes = demandeDetteRepository.findDemandesEnAttente();

    // Vérifier si des demandes en attente existent
    boolean found = false;

    // Afficher toutes les demandes en attente
    for (DemandeDette demande : demandes) {
        if ("En attente".equals(demande.getStatus())) { // Vérifie si le statut est "En attente"
            found = true;
            System.out.println("ID Demande : " + demande.getId());
            System.out.println("Client ID : " + demande.getClientId());
            System.out.println("Montant : " + demande.getMontant());
            System.out.println("Date de demande : " + demande.getDate());
            System.out.println("----------------------------------");
        }
    }

    // Si aucune demande en attente n'est trouvée
    if (!found) {
        System.out.println("Aucune demande de dette en attente dans la base de données.");
    }
}


}