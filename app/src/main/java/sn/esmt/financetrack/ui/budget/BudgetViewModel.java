package sn.esmt.financetrack.ui.budget;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.Calendar;
import java.util.List;

import sn.esmt.financetrack.data.entity.Budget;
import sn.esmt.financetrack.data.repository.BudgetRepository;

public class BudgetViewModel extends AndroidViewModel {
    private BudgetRepository mRepository;
    private LiveData<List<Budget>> mAllBudgets;

    public BudgetViewModel(Application application) {
        super(application);
        mRepository = new BudgetRepository(application);
        Calendar calendar = Calendar.getInstance();
        mAllBudgets = mRepository.getBudgetsByPeriod(calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.YEAR));
    }

    public LiveData<List<Budget>> getAllBudgets() {
        return mAllBudgets;
    }

    public void insert(Budget budget) { mRepository.insert(budget); }
    public void update(Budget budget) { mRepository.update(budget); }
    public void delete(Budget budget) { mRepository.delete(budget); }
}
