package com.tp.gestiondepenses.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.tp.gestiondepenses.database.AppDatabase;
import com.tp.gestiondepenses.database.BudgetDao;
import com.tp.gestiondepenses.database.CategorieDao;
import com.tp.gestiondepenses.database.DepenseDao;
import com.tp.gestiondepenses.database.RevenuDao;
import com.tp.gestiondepenses.database.RubriqueDao;
import com.tp.gestiondepenses.model.Budget;
import com.tp.gestiondepenses.model.Categorie;
import com.tp.gestiondepenses.model.Depense;
import com.tp.gestiondepenses.model.Revenu;
import com.tp.gestiondepenses.model.Rubrique;

import java.util.List;

public class FinanceRepository {
    private DepenseDao depenseDao;
    private RevenuDao revenuDao;
    private CategorieDao categorieDao;
    private BudgetDao budgetDao;
    private RubriqueDao rubriqueDao;

    public FinanceRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        depenseDao = db.depenseDao();
        revenuDao = db.revenuDao();
        categorieDao = db.categorieDao();
        budgetDao = db.budgetDao();
        rubriqueDao = db.rubriqueDao();
    }

    public LiveData<List<Depense>> getAllDepenses() { return depenseDao.getAllDepenses(); }
    public LiveData<List<Revenu>> getAllRevenus() { return revenuDao.getAllRevenus(); }
    public LiveData<List<Categorie>> getAllCategories() { return categorieDao.getAllCategories(); }
    public LiveData<List<Budget>> getAllBudgets() { return budgetDao.getAllBudgets(); }
    public LiveData<List<Rubrique>> getAllRubriques() { return rubriqueDao.getAllRubriques(); }
    public LiveData<Double> getTotalDepenses() { return depenseDao.getTotalDepenses(); }
    public LiveData<Double> getTotalRevenus() { return revenuDao.getTotalRevenus(); }

    public LiveData<Double> getTotalDepensesParMois(String mois, String annee) {
        return depenseDao.getTotalDepensesParMois(mois, annee);
    }

    public LiveData<Double> getTotalRevenusParMois(String mois, String annee) {
        return revenuDao.getTotalRevenusParMois(mois, annee);
    }

    public LiveData<Depense> getDepenseById(int id) { return depenseDao.getDepenseById(id); }
    public LiveData<Revenu> getRevenuById(int id) { return revenuDao.getRevenuById(id); }
    public LiveData<Budget> getBudgetById(int id) { return budgetDao.getBudgetById(id); }

    public void insertDepense(Depense depense) {
        AppDatabase.databaseWriteExecutor.execute(() -> depenseDao.insertDepense(depense));
    }

    public void insertRevenu(Revenu revenu) {
        AppDatabase.databaseWriteExecutor.execute(() -> revenuDao.insertRevenu(revenu));
    }

    public void updateDepense(Depense depense) {
        AppDatabase.databaseWriteExecutor.execute(() -> depenseDao.updateDepense(depense));
    }

    public void updateRevenu(Revenu revenu) {
        AppDatabase.databaseWriteExecutor.execute(() -> revenuDao.updateRevenu(revenu));
    }

    public void insertBudget(Budget budget) {
        AppDatabase.databaseWriteExecutor.execute(() -> budgetDao.insertBudget(budget));
    }

    public void updateBudget(Budget budget) {
        AppDatabase.databaseWriteExecutor.execute(() -> budgetDao.updateBudget(budget));
    }

    public void deleteBudget(Budget budget) {
        AppDatabase.databaseWriteExecutor.execute(() -> budgetDao.deleteBudget(budget));
    }

    public void insertCategorie(Categorie categorie) {
        AppDatabase.databaseWriteExecutor.execute(() -> categorieDao.insertCategorie(categorie));
    }

    public void deleteCategorie(Categorie categorie) {
        AppDatabase.databaseWriteExecutor.execute(() -> categorieDao.deleteCategorie(categorie));
    }

    public void updateCategorie(Categorie categorie) {
        AppDatabase.databaseWriteExecutor.execute(() -> categorieDao.updateCategorie(categorie));
    }

    public void deleteDepense(Depense depense) {
        AppDatabase.databaseWriteExecutor.execute(() -> depenseDao.deleteDepense(depense));
    }

    public void deleteRevenu(Revenu revenu) {
        AppDatabase.databaseWriteExecutor.execute(() -> revenuDao.deleteRevenu(revenu));
    }

    public void insertRubrique(Rubrique rubrique) {
        AppDatabase.databaseWriteExecutor.execute(() -> rubriqueDao.insertRubrique(rubrique));
    }

    public LiveData<List<Rubrique>> getRubriquesByCategorie(int catId) {
        return rubriqueDao.getRubriquesByCategorie(catId);
    }

    public void deleteRubrique(Rubrique rubrique) {
        AppDatabase.databaseWriteExecutor.execute(() -> rubriqueDao.delete(rubrique));
    }

    public void resetCategories() {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            AppDatabase db = AppDatabase.getDatabase(null);
            db.runInTransaction(() -> {
                rubriqueDao.deleteAll();
                categorieDao.deleteAll();

                // 1. Alimentation
                long cat1 = categorieDao.insertCategorieWithId(new Categorie("Alimentation", "fast-food", "#FF5252", true));
                rubriqueDao.insertRubrique(new Rubrique((int)cat1, "Restaurant"));
                rubriqueDao.insertRubrique(new Rubrique((int)cat1, "Marche"));
                rubriqueDao.insertRubrique(new Rubrique((int)cat1, "Épicerie"));
                rubriqueDao.insertRubrique(new Rubrique((int)cat1, "Fast-food"));

                // 2. Transport
                long cat2 = categorieDao.insertCategorieWithId(new Categorie("Transport", "taxi", "#448AFF", true));
                rubriqueDao.insertRubrique(new Rubrique((int)cat2, "Taxi"));
                rubriqueDao.insertRubrique(new Rubrique((int)cat2, "Bus"));
                rubriqueDao.insertRubrique(new Rubrique((int)cat2, "Carburant"));
                rubriqueDao.insertRubrique(new Rubrique((int)cat2, "Parking"));

                // 3. Logement
                long cat3 = categorieDao.insertCategorieWithId(new Categorie("Logement", "home", "#4CAF50", true));
                rubriqueDao.insertRubrique(new Rubrique((int)cat3, "Loyer"));
                rubriqueDao.insertRubrique(new Rubrique((int)cat3, "Electricité"));
                rubriqueDao.insertRubrique(new Rubrique((int)cat3, "Eau"));
                rubriqueDao.insertRubrique(new Rubrique((int)cat3, "Internet"));

                // 4. Santé
                long cat4 = categorieDao.insertCategorieWithId(new Categorie("Santé", "local-hospital", "#E91E63", true));
                rubriqueDao.insertRubrique(new Rubrique((int)cat4, "Pharmacie"));
                rubriqueDao.insertRubrique(new Rubrique((int)cat4, "Consultation"));
                rubriqueDao.insertRubrique(new Rubrique((int)cat4, "Hôpital"));

                // 5. Éducation
                long cat5 = categorieDao.insertCategorieWithId(new Categorie("Éducation", "school", "#9C27B0", true));
                rubriqueDao.insertRubrique(new Rubrique((int)cat5, "Frais scolaires"));
                rubriqueDao.insertRubrique(new Rubrique((int)cat5, "Fournitures"));
                rubriqueDao.insertRubrique(new Rubrique((int)cat5, "Livres"));

                // 6. Loisirs
                long cat6 = categorieDao.insertCategorieWithId(new Categorie("Loisirs", "sports-esports", "#FF9800", true));
                rubriqueDao.insertRubrique(new Rubrique((int)cat6, "Divertissement"));
                rubriqueDao.insertRubrique(new Rubrique((int)cat6, "Sport"));
                rubriqueDao.insertRubrique(new Rubrique((int)cat6, "Voyage"));

                // 7. Habillement
                long cat7 = categorieDao.insertCategorieWithId(new Categorie("Habillement", "checkroom", "#795548", true));
                rubriqueDao.insertRubrique(new Rubrique((int)cat7, "Vêtements"));
                rubriqueDao.insertRubrique(new Rubrique((int)cat7, "Chaussures"));
                rubriqueDao.insertRubrique(new Rubrique((int)cat7, "Accessoires"));

                // 8. Autre
                categorieDao.insertCategorieWithId(new Categorie("Autre", "more-horiz", "#607D8B", true));
            });
        });
    }

    public void deleteAllData() {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            depenseDao.deleteAll();
            revenuDao.deleteAll();
            budgetDao.deleteAll();
        });
    }
}
