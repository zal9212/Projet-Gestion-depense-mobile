package sn.esmt.financetrack.model;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "transactions",
        foreignKeys = @ForeignKey(entity = Rubrique.class,
                parentColumns = "id",
                childColumns = "rubriqueId",
                onDelete = ForeignKey.CASCADE),
        indices = {@androidx.room.Index("rubriqueId")})
public class Transaction implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private Double montant;
    private String description;
    private long date;
    private String type; // "DEPENSE" ou "REVENU"
    private int rubriqueId;

    public Transaction(double montant, String description, long date, String type, int rubriqueId) {
        this.montant = montant;
        this.description = description;
        this.date = date;
        this.type = type;
        this.rubriqueId = rubriqueId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getMontant() {
        return montant;
    }

    public void setMontant(double montant) {
        this.montant = montant;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getRubriqueId() {
        return rubriqueId;
    }

    public void setRubriqueId(int rubriqueId) {
        this.rubriqueId = rubriqueId;
    }
}
