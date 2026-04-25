package sn.esmt.financetrack.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.util.Date;

    @Entity(
            tableName = "depenses",
            foreignKeys = {
                    @ForeignKey(entity = User.class,
                            parentColumns = "id",
                            childColumns = "user_id",
                            onDelete = ForeignKey.CASCADE),

                    @ForeignKey(entity = Categorie.class,
                            parentColumns = "id",
                            childColumns = "categorie_id",
                            onDelete = ForeignKey.CASCADE),

                    @ForeignKey(entity = Rubrique.class,
                            parentColumns = "id",
                            childColumns = "rubrique_id",
                            onDelete = ForeignKey.CASCADE)
            }
    )
    public class Depenses {

        @PrimaryKey(autoGenerate = true)
        private int id;

        private Double montant;
        private String date;
        private String description;
        private String moyen_paiement;
        private String created_at;

        @ColumnInfo(name = "categorie_id")
        private int categorieId;

        @ColumnInfo(name = "rubrique_id")
        private int rubriqueId;

        @ColumnInfo(name = "user_id")
        private int userId;



        public Depenses(int id, double montant, String date, String description, int categorieId, String moyen_paiement, String created_at, int rubriqueId, int userId) {
            this.id = id;
            this.montant = montant;
            this.date = date;
            this.description = description;
            this.categorieId = categorieId;
            this.moyen_paiement = moyen_paiement;
            this.created_at = created_at;
            this.rubriqueId = rubriqueId;
            this.userId = userId;
        }
        // Getters et Setters

        public int getId() {
            return id;
        }

        public double getMontant() {
            return montant;
        }

        public String getDate() {
            return date;
        }

        public String getDescription() {
            return description;
        }

        public String getMoyen_paiement() {
            return moyen_paiement;
        }

        public String getCreated_at() {
            return created_at;
        }

        public int getCategorieId() {
            return categorieId;
        }

        public int getRubriqueId() {
            return rubriqueId;
        }

        public int getUserId() {
            return userId;
        }

        public void setMontant(double montant) {
            this.montant = montant;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public void setMoyen_paiement(String moyen_paiement) {
            this.moyen_paiement = moyen_paiement;
        }

        public void setCreated_at(String created_at) {
            this.created_at = created_at;
        }

        public void setCategorieId(int categorieId) {
            this.categorieId = categorieId;
        }

        public void setRubriqueId(int rubriqueId) {
            this.rubriqueId = rubriqueId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }
    }

