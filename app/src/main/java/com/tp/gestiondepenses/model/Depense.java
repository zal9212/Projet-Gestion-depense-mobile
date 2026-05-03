package com.tp.gestiondepenses.model;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "depenses",
        foreignKeys = {
                @ForeignKey(entity = Categorie.class, 
                            parentColumns = "id", 
                            childColumns = "categorie_id",
                            onDelete = ForeignKey.CASCADE),
                @ForeignKey(entity = Rubrique.class, 
                            parentColumns = "id", 
                            childColumns = "rubrique_id",
                            onDelete = ForeignKey.CASCADE)
        },
        indices = {@Index("categorie_id"), @Index("rubrique_id")})
public class Depense {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public int categorie_id;
    public Integer rubrique_id;
    public double montant;
    public long date;
    public String description;
    public String moyen_paiement;
    public long created_at;

    public Depense(int categorie_id, Integer rubrique_id, double montant, long date, String description, String moyen_paiement) {
        this.categorie_id = categorie_id;
        this.rubrique_id = rubrique_id;
        this.montant = montant;
        this.date = date;
        this.description = description;
        this.moyen_paiement = moyen_paiement;
        this.created_at = System.currentTimeMillis();
    }
}
