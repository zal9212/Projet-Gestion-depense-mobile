package sn.esmt.financetrack.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(
        tableName = "revenus",
        foreignKeys = {
                @ForeignKey(entity = User.class,
                        parentColumns = "id",
                        childColumns = "user_id",
                        onDelete = ForeignKey.CASCADE),

                @ForeignKey(entity = Source.class,
                        parentColumns = "id",
                        childColumns = "source_id",
                        onDelete = ForeignKey.CASCADE)
        }
)
public class Revenus {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private Double montant;
    private String date;
    private String description;
    private String created_at;

    @ColumnInfo(name = "user_id")
    private int userId;

    @ColumnInfo(name = "source_id")
    private int sourceId;

    public Revenus(int id, double montant, String description, String date, String created_at, int userId, int sourceId) {
        this.id = id;
        this.montant = montant;
        this.description = description;
        this.date = date;
        this.created_at = created_at;
        this.userId = userId;
        this.sourceId = sourceId;
    }

    // Getters et Setters


    public int getId() {
        return id;
    }

    public double getMontant() {
        return montant;
    }

    public void setMontant(double montant) {
        this.montant = montant;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public int getSourceId() {
        return sourceId;
    }

    public void setSourceId(int sourceId) {
        this.sourceId = sourceId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}