package sn.esmt.financetrack.data.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "transactions")
public class Transaction {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private double montant;
    private String type; // DEPENSE or REVENU
    private int categorieId;
    private String date; // ISO format for strftime

    public Transaction(double montant, String type, int categorieId, String date) {
        this.montant = montant;
        this.type = type;
        this.categorieId = categorieId;
        this.date = date;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public double getMontant() { return montant; }
    public void setMontant(double montant) { this.montant = montant; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public int getCategorieId() { return categorieId; }
    public void setCategorieId(int categorieId) { this.categorieId = categorieId; }
    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }
}
