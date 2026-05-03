package com.tp.gestiondepenses.model;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "rubriques",
        foreignKeys = @ForeignKey(entity = Categorie.class,
                parentColumns = "id",
                childColumns = "categorie_id",
                onDelete = ForeignKey.CASCADE),
        indices = {@Index("categorie_id")})
public class Rubrique {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public int categorie_id;
    public String nom;

    public Rubrique(int categorie_id, String nom) {
        this.categorie_id = categorie_id;
        this.nom = nom;
    }
}
