package com.tp.gestiondepenses.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.tp.gestiondepenses.model.Rubrique;

import java.util.List;

@Dao
public interface RubriqueDao {
    @Insert
    void insertRubrique(Rubrique rubrique);

    @Update
    void update(Rubrique rubrique);

    @Delete
    void delete(Rubrique rubrique);

    @Query("SELECT * FROM rubriques WHERE categorie_id = :categorieId")
    LiveData<List<Rubrique>> getRubriquesByCategorie(int categorieId);

    @Query("SELECT * FROM rubriques")
    LiveData<List<Rubrique>> getAllRubriques();

    @Query("DELETE FROM rubriques")
    void deleteAll();
}
