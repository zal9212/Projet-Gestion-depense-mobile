package sn.esmt.financetrack.data.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

import sn.esmt.financetrack.data.dao.CategorieDao;
import sn.esmt.financetrack.data.database.AppDatabase;
import sn.esmt.financetrack.data.entity.Categorie;

public class CategorieRepository {
    private CategorieDao mCategorieDao;
    private LiveData<List<Categorie>> mAllCategories;

    public CategorieRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        mCategorieDao = db.categorieDao();
        mAllCategories = mCategorieDao.getAllCategories();
    }

    public LiveData<List<Categorie>> getAllCategories() {
        return mAllCategories;
    }

    public void insert(Categorie categorie) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            mCategorieDao.insert(categorie);
        });
    }

    public void update(Categorie categorie) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            mCategorieDao.update(categorie);
        });
    }

    public void delete(Categorie categorie) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            // Logique Student 3 : Vérifier si la catégorie est utilisée avant de supprimer
            // Pour l'instant on supprime, mais l'UI devra gérer l'alerte
            if (!categorie.isDefault()) {
                mCategorieDao.delete(categorie);
            }
        });
    }
}
