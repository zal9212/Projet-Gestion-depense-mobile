package sn.esmt.financetrack.data.entity;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

/**
 * Entité Room représentant un budget mensuel pour une catégorie.
 * Étudiant 3 - Module Budgets
 *
 * Un budget lie une catégorie à un plafond de dépenses pour un mois/année donnés.
 */
@Entity(
    tableName = "budgets",
    foreignKeys = @ForeignKey(
        entity = Categorie.class,
        parentColumns = "id",
        childColumns = "categorieId",
        onDelete = ForeignKey.CASCADE
    ),
    indices = {@Index("categorieId")}
)
public class Budget {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private int categorieId;   // FK vers Categorie
    private double plafond;    // Montant maximum alloué (ex: 50000 FCFA)
    private int mois;          // 1-12
    private int annee;         // ex: 2025

    // ─── Constructeurs ────────────────────────────────────────────────────────

    public Budget() {}

    public Budget(int categorieId, double plafond, int mois, int annee) {
        this.categorieId = categorieId;
        this.plafond = plafond;
        this.mois = mois;
        this.annee = annee;
    }

    // ─── Getters / Setters ────────────────────────────────────────────────────

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getCategorieId() { return categorieId; }
    public void setCategorieId(int categorieId) { this.categorieId = categorieId; }

    public double getPlafond() { return plafond; }
    public void setPlafond(double plafond) { this.plafond = plafond; }

    public int getMois() { return mois; }
    public void setMois(int mois) { this.mois = mois; }

    public int getAnnee() { return annee; }
    public void setAnnee(int annee) { this.annee = annee; }
}
