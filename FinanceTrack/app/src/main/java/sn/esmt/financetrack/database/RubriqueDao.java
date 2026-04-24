package sn.esmt.financetrack.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import sn.esmt.financetrack.model.Rubrique;

@Dao
public interface RubriqueDao {

    @Insert
    void insertRubrique(Rubrique rubrique);

    @Update
    void updateRubrique(Rubrique rubrique);

    @Delete
    void deleteRubrique(Rubrique rubrique);

    // On récupère toutes les rubriques liées à une catégorie spécifique
    @Query("SELECT * FROM rubriques WHERE categorieId = :categorieId ORDER BY nom ASC")
    LiveData<List<Rubrique>> getRubriquesByCategorie(int categorieId);
}
