package sn.esmt.financetrack.data.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.annotation.NonNull;

/**
 * Entité Room représentant une catégorie de dépenses/revenus.
 * Étudiant 3 - Module Catégories
 */
@Entity(tableName = "categories")
public class Categorie {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @NonNull
    private String nom;

    private String icone;   // Nom de l'icône Material (ex: "restaurant", "directions_car")
    private String couleur; // Couleur hex (ex: "#FF5722")
    private boolean isDefault; // true = catégorie système, non supprimable

    // ─── Constructeurs ────────────────────────────────────────────────────────

    public Categorie() {}

    public Categorie(@NonNull String nom, String icone, String couleur, boolean isDefault) {
        this.nom = nom;
        this.icone = icone;
        this.couleur = couleur;
        this.isDefault = isDefault;
    }

    // ─── Getters / Setters ────────────────────────────────────────────────────

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    @NonNull
    public String getNom() { return nom; }
    public void setNom(@NonNull String nom) { this.nom = nom; }

    public String getIcone() { return icone; }
    public void setIcone(String icone) { this.icone = icone; }

    public String getCouleur() { return couleur; }
    public void setCouleur(String couleur) { this.couleur = couleur; }

    public boolean isDefault() { return isDefault; }
    public void setDefault(boolean aDefault) { isDefault = aDefault; }

    @Override
    public String toString() {
        return nom;
    }
}
