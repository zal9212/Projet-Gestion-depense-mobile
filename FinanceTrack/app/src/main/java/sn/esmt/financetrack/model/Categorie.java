package sn.esmt.financetrack.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

// @Entity dit à Android : "Crée une table SQL qui s'appelle 'categories' avec ça"
@Entity(tableName = "categories")
public class Categorie {

    // @PrimaryKey dit : "C'est l'identifiant unique (auto-incrémenté 1, 2, 3...)"
    @PrimaryKey(autoGenerate = true)
    private int id;

    private String nom;
    private String icone;
    private String couleur;
    
    // Pour savoir si c'est une catégorie par défaut (Loyer, Transport) qu'on ne peut pas supprimer
    private boolean estDefaut;

    // --- LE CONSTRUCTEUR ---
    public Categorie(String nom, String icone, String couleur, boolean estDefaut) {
        this.nom = nom;
        this.icone = icone;
        this.couleur = couleur;
        this.estDefaut = estDefaut;
    }

    // --- LES GETTERS ET SETTERS --- (Pour lire et modifier les variables privées)
    
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getIcone() { return icone; }
    public void setIcone(String icone) { this.icone = icone; }

    public String getCouleur() { return couleur; }
    public void setCouleur(String couleur) { this.couleur = couleur; }

    public boolean isEstDefaut() { return estDefaut; }
    public void setEstDefaut(boolean estDefaut) { this.estDefaut = estDefaut; }
}
