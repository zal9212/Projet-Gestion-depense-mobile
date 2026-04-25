package sn.esmt.financetrack.model;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "budgets",
        foreignKeys = {
                @ForeignKey(entity = User.class,
                        parentColumns = "id",
                        childColumns = "user_id",
                        onDelete = ForeignKey.CASCADE),

                @ForeignKey(entity = Categorie.class,
                        parentColumns = "id",
                        childColumns = "categorie_id",
                        onDelete = ForeignKey.CASCADE),

        }
)
public class Budgets {
    @PrimaryKey(autoGenerate = true)
    private int id;

    private Double montant_plafond;

    private String periode;

    private String mois;

    private Double annee;

    public Budgets(int id, Double montant_plafond, String periode, String mois, Double annee) {
        this.id = id;
        this.montant_plafond = montant_plafond;
        this.periode = periode;
        this.mois = mois;
        this.annee = annee;
    }

    public int getId() {
        return id;
    }

    public Double getMontant_plafond() {
        return montant_plafond;
    }

    public void setMontant_plafond(Double montant_plafond) {
        this.montant_plafond = montant_plafond;
    }

    public String getPeriode() {
        return periode;
    }

    public void setPeriode(String periode) {
        this.periode = periode;
    }

    public String getMois() {
        return mois;
    }

    public void setMois(String mois) {
        this.mois = mois;
    }

    public Double getAnnee() {
        return annee;
    }

    public void setAnnee(Double annee) {
        this.annee = annee;
    }
}
