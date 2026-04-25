package sn.esmt.financetrack.repository;

import android.app.Application;
import androidx.lifecycle.LiveData;
import java.util.List;

import sn.esmt.financetrack.database.AppDatabase;
import sn.esmt.financetrack.database.RubriqueDao;
import sn.esmt.financetrack.model.Rubrique;

public class RubriqueRepository {
    private RubriqueDao rubriqueDao;

    public RubriqueRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        rubriqueDao = db.rubriqueDao();
    }

    public LiveData<List<Rubrique>> getRubriquesByCategorie(int categorieId) {
        return rubriqueDao.getRubriquesByCategorie(categorieId);
    }

    public LiveData<List<Rubrique>> getAllRubriques() {
        return rubriqueDao.getAllRubriques();
    }

    public void insert(Rubrique rubrique) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            rubriqueDao.insertRubrique(rubrique);
        });
    }

    public void update(Rubrique rubrique) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            rubriqueDao.updateRubrique(rubrique);
        });
    }

    public void delete(Rubrique rubrique) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            rubriqueDao.deleteRubrique(rubrique);
        });
    }
}
