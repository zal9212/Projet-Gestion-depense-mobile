package com.tp.gestiondepenses.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.tp.gestiondepenses.model.Revenu;

import java.util.List;

@Dao
public interface RevenuDao {
    @Insert
    void insertRevenu(Revenu revenu);

    @Update
    void updateRevenu(Revenu revenu);

    @Delete
    void deleteRevenu(Revenu revenu);

    @Query("SELECT * FROM revenus ORDER BY date DESC")
    LiveData<List<Revenu>> getAllRevenus();

    @Query("SELECT SUM(montant) FROM revenus WHERE strftime('%m', datetime(date/1000, 'unixepoch')) = :mois AND strftime('%Y', datetime(date/1000, 'unixepoch')) = :annee")
    LiveData<Double> getTotalRevenusParMois(String mois, String annee);

    @Query("SELECT * FROM revenus WHERE id = :id")
    LiveData<Revenu> getRevenuById(int id);

    @Query("SELECT SUM(montant) FROM revenus")
    LiveData<Double> getTotalRevenus();

    @Query("DELETE FROM revenus")
    void deleteAll();
}
