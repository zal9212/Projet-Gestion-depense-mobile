package com.tp.gestiondepenses.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "revenus")
public class Revenu {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public String source;
    public double montant;
    public long date;
    public String description;
    public long created_at;

    public Revenu(String source, double montant, long date, String description) {
        this.source = source;
        this.montant = montant;
        this.date = date;
        this.description = description;
        this.created_at = System.currentTimeMillis();
    }
}
