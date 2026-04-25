package sn.esmt.financetrack.data.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

import sn.esmt.financetrack.data.dao.BudgetDao;
import sn.esmt.financetrack.data.database.AppDatabase;
import sn.esmt.financetrack.data.entity.Budget;

public class BudgetRepository {
    private BudgetDao mBudgetDao;

    public BudgetRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        mBudgetDao = db.budgetDao();
    }

    public LiveData<List<Budget>> getBudgetsByPeriod(int mois, int annee) {
        return mBudgetDao.getBudgetsByPeriod(mois, annee);
    }

    public void insert(Budget budget) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            mBudgetDao.insert(budget);
        });
    }

    public void update(Budget budget) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            mBudgetDao.update(budget);
        });
    }

    public void delete(Budget budget) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            mBudgetDao.delete(budget);
        });
    }
}
