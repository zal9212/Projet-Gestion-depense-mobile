package sn.esmt.financetrack.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import sn.esmt.financetrack.data.entity.Budget;

@Dao
public interface BudgetDao {
    @Insert
    void insert(Budget budget);

    @Update
    void update(Budget budget);

    @Delete
    void delete(Budget budget);

    @Query("SELECT * FROM budgets WHERE mois = :mois AND annee = :annee")
    LiveData<List<Budget>> getBudgetsByPeriod(int mois, int annee);

    @Query("SELECT * FROM budgets WHERE categorieId = :categorieId AND mois = :mois AND annee = :annee LIMIT 1")
    Budget getBudgetForCategory(int categorieId, int mois, int annee);
}
