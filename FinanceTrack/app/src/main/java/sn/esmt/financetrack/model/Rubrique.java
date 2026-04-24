package sn.esmt.financetrack.model;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

// @Entity pour créer la table 'rubriques'
// ForeignKey indique la relation : une Rubrique appartient à une Categorie.
// CASCADE veut dire que si on supprime la catégorie, ça supprime toutes ses rubriques automatiquement !
@Entity(
    tableName = "rubriques",
    foreignKeys = @ForeignKey(
        entity = Categorie.class,
        parentColumns = "id",
        childColumns = "categorieId",
        onDelete = ForeignKey.CASCADE
    ),
    indices = {@Index("categorieId")} // Index pour accélérer les recherches dans la base
)
public class Rubrique {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private int categorieId; // La fameuse clé étrangère (l'ID de la catégorie parente)
    private String nom;

    // --- CONSTRUCTEUR ---
    public Rubrique(int categorieId, String nom) {
        this.categorieId = categorieId;
        this.nom = nom;
    }

    // --- GETTERS ET SETTERS ---
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getCategorieId() { return categorieId; }
    public void setCategorieId(int categorieId) { this.categorieId = categorieId; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
}
