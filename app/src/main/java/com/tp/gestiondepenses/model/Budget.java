package com.tp.gestiondepenses.model;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "budgets",
        foreignKeys = @ForeignKey(entity = Categorie.class, parentColumns = "id", childColumns = "categorie_id"),
        indices = {@Index("categorie_id")})
public class Budget {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public Integer categorie_id; // null for global
    public double montant_plafond;
    public String periode; // e.g., "MENSUEL"
    public int mois;
    public int annee;

    public Budget(Integer categorie_id, double montant_plafond, String periode, int mois, int annee) {
        this.categorie_id = categorie_id;
        this.montant_plafond = montant_plafond;
        this.periode = periode;
        this.mois = mois;
        this.annee = annee;
    }
}
