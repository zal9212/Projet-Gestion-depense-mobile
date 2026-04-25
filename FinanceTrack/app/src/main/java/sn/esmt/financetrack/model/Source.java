package sn.esmt.financetrack.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "sources")
public class Source {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String nom;

    private String icone;

    public Source(int id, String nom, String icone) {
        this.id = id;
        this.nom = nom;
        this.icone = icone;
    }

    public int getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getIcone() {
        return icone;
    }

    public void setIcone(String icone) {
        this.icone = icone;
    }
}
