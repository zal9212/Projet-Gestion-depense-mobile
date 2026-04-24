package sn.esmt.financetrack.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import sn.esmt.financetrack.model.Categorie;

@Dao
public interface CategorieDao {

    @Insert
    void insertCategorie(Categorie categorie);

    @Update
    void updateCategorie(Categorie categorie);

    @Delete
    void deleteCategorie(Categorie categorie);

    // On utilise LiveData pour que notre RecyclerView se mette à jour tout seul quand la table change !
    @Query("SELECT * FROM categories ORDER BY nom ASC")
    LiveData<List<Categorie>> getAllCategories();

    @Query("SELECT * FROM categories WHERE id = :id LIMIT 1")
    LiveData<Categorie> getCategorieById(int id);
}
