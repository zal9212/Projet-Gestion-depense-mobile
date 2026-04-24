package sn.esmt.financetrack.repository;

import android.app.Application;
import androidx.lifecycle.LiveData;
import java.util.List;

import sn.esmt.financetrack.database.AppDatabase;
import sn.esmt.financetrack.database.CategorieDao;
import sn.esmt.financetrack.model.Categorie;

public class CategorieRepository {
    private CategorieDao categorieDao;
    private LiveData<List<Categorie>> allCategories;

    public CategorieRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        categorieDao = db.categorieDao();
        allCategories = categorieDao.getAllCategories();
    }

    public LiveData<List<Categorie>> getAllCategories() {
        return allCategories;
    }

    public void insert(Categorie categorie) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            categorieDao.insertCategorie(categorie);
        });
    }

    public void update(Categorie categorie) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            categorieDao.updateCategorie(categorie);
        });
    }

    public void delete(Categorie categorie) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            categorieDao.deleteCategorie(categorie);
        });
    }
}
