package com.tp.gestiondepenses.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.tp.gestiondepenses.model.Categorie;

import java.util.List;

@Dao
public interface CategorieDao {
    @Insert
    void insertCategorie(Categorie categorie);

    @Insert
    long insertCategorieWithId(Categorie categorie);

    @Update
    void updateCategorie(Categorie categorie);

    @Delete
    void deleteCategorie(Categorie categorie);

    @Query("SELECT * FROM categories")
    LiveData<List<Categorie>> getAllCategories();

    @Query("SELECT * FROM categories WHERE id = :id")
    Categorie getCategorieById(int id);

    @Query("DELETE FROM categories")
    void deleteAll();
}
