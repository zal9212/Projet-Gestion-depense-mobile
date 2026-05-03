package com.tp.gestiondepenses.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.tp.gestiondepenses.model.Depense;

import java.util.List;

@Dao
public interface DepenseDao {
    @Insert
    void insertDepense(Depense depense);

    @Update
    void updateDepense(Depense depense);

    @Delete
    void deleteDepense(Depense depense);

    @Query("SELECT * FROM depenses ORDER BY date DESC")
    LiveData<List<Depense>> getAllDepenses();

    @Query("SELECT * FROM depenses WHERE strftime('%m', datetime(date/1000, 'unixepoch')) = :mois AND strftime('%Y', datetime(date/1000, 'unixepoch')) = :annee ORDER BY date DESC")
    LiveData<List<Depense>> getDepensesByMois(String mois, String annee);

    @Query("SELECT SUM(montant) FROM depenses WHERE strftime('%m', datetime(date/1000, 'unixepoch')) = :mois AND strftime('%Y', datetime(date/1000, 'unixepoch')) = :annee")
    LiveData<Double> getTotalDepensesParMois(String mois, String annee);

    @Query("SELECT * FROM depenses WHERE id = :id")
    LiveData<Depense> getDepenseById(int id);

    @Query("SELECT SUM(montant) FROM depenses")
    LiveData<Double> getTotalDepenses();

    @Query("DELETE FROM depenses")
    void deleteAll();
}
