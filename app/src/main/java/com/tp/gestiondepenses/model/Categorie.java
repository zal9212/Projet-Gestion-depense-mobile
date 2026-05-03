package com.tp.gestiondepenses.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "categories")
public class Categorie {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public String nom;
    public String icone;
    public String couleur;
    public boolean est_defaut;

    public Categorie(String nom, String icone, String couleur, boolean est_defaut) {
        this.nom = nom;
        this.icone = icone;
        this.couleur = couleur;
        this.est_defaut = est_defaut;
    }
}
