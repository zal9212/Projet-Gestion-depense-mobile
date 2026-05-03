package com.tp.gestiondepenses.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.tp.gestiondepenses.model.Budget;
import com.tp.gestiondepenses.model.Categorie;
import com.tp.gestiondepenses.model.Depense;
import com.tp.gestiondepenses.model.Revenu;
import com.tp.gestiondepenses.model.Rubrique;
import com.tp.gestiondepenses.repository.FinanceRepository;

import java.util.List;

public class FinanceViewModel extends AndroidViewModel {
    private FinanceRepository repository;

    public FinanceViewModel(@NonNull Application application) {
        super(application);
        repository = new FinanceRepository(application);
    }

    public LiveData<List<Depense>> getAllDepenses() { return repository.getAllDepenses(); }
    public LiveData<List<Revenu>> getAllRevenus() { return repository.getAllRevenus(); }
    public LiveData<List<Categorie>> getAllCategories() { return repository.getAllCategories(); }
    public LiveData<List<Budget>> getAllBudgets() { return repository.getAllBudgets(); }
    public LiveData<Double> getTotalDepenses() { return repository.getTotalDepenses(); }
    public LiveData<Double> getTotalRevenus() { return repository.getTotalRevenus(); }

    public LiveData<Double> getTotalDepensesParMois(String mois, String annee) {
        return repository.getTotalDepensesParMois(mois, annee);
    }

    public LiveData<Double> getTotalRevenusParMois(String mois, String annee) {
        return repository.getTotalRevenusParMois(mois, annee);
    }

    public LiveData<Depense> getDepenseById(int id) { return repository.getDepenseById(id); }
    public LiveData<Revenu> getRevenuById(int id) { return repository.getRevenuById(id); }
    public LiveData<Budget> getBudgetById(int id) { return repository.getBudgetById(id); }

    public void insertDepense(Depense depense) { repository.insertDepense(depense); }
    public void insertRevenu(Revenu revenu) { repository.insertRevenu(revenu); }
    public void insertBudget(Budget budget) { repository.insertBudget(budget); }
    public void updateBudget(Budget budget) { repository.updateBudget(budget); }
    public void insertCategorie(Categorie categorie) { repository.insertCategorie(categorie); }
    public void insertRubrique(Rubrique rubrique) { repository.insertRubrique(rubrique); }
    public void deleteCategorie(Categorie categorie) { repository.deleteCategorie(categorie); }
    public void updateCategorie(Categorie categorie) { repository.updateCategorie(categorie); }

    public void updateDepense(Depense depense) { repository.updateDepense(depense); }
    public void updateRevenu(Revenu revenu) { repository.updateRevenu(revenu); }

    public void deleteDepense(Depense depense) { repository.deleteDepense(depense); }
    public void deleteRevenu(Revenu revenu) { repository.deleteRevenu(revenu); }
    public void deleteBudget(Budget budget) { repository.deleteBudget(budget); }

    public void deleteAllData() { repository.deleteAllData(); }

    public LiveData<List<Rubrique>> getRubriquesByCategorie(int catId) {
        return repository.getRubriquesByCategorie(catId);
    }

    public void deleteRubrique(Rubrique rubrique) {
        repository.deleteRubrique(rubrique);
    }

    public void resetCategories() {
        repository.resetCategories();
    }
}
