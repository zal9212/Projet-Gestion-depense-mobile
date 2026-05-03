package com.tp.gestiondepenses.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.tp.gestiondepenses.model.Budget;

import java.util.List;

@Dao
public interface BudgetDao {
    @Insert
    void insertBudget(Budget budget);

    @Update
    void updateBudget(Budget budget);

    @Delete
    void deleteBudget(Budget budget);

    @Query("SELECT * FROM budgets WHERE categorie_id = :cat_id AND mois = :mois AND annee = :annee")
    LiveData<Budget> getBudgetByCategorie(int cat_id, int mois, int annee);

    @Query("SELECT * FROM budgets")
    LiveData<List<Budget>> getAllBudgets();

    @Query("SELECT * FROM budgets WHERE id = :id LIMIT 1")
    LiveData<Budget> getBudgetById(int id);

    @Query("SELECT * FROM budgets WHERE categorie_id IS NULL AND mois = :mois AND annee = :annee")
    LiveData<Budget> getBudgetGlobal(int mois, int annee);

    @Query("DELETE FROM budgets")
    void deleteAll();
}
