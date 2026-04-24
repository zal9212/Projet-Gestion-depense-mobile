package sn.esmt.financetrack.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import sn.esmt.financetrack.model.Rubrique;
import sn.esmt.financetrack.repository.RubriqueRepository;

public class RubriqueViewModel extends AndroidViewModel {
    private RubriqueRepository repository;

    public RubriqueViewModel(@NonNull Application application) {
        super(application);
        repository = new RubriqueRepository(application);
    }

    // On récupère les rubriques pour une catégorie spécifique
    public LiveData<List<Rubrique>> getRubriquesByCategorie(int categorieId) {
        return repository.getRubriquesByCategorie(categorieId);
    }

    public void addRubrique(int categorieId, String nom) {
        Rubrique rubrique = new Rubrique(categorieId, nom);
        repository.insert(rubrique);
    }

    public void updateRubrique(Rubrique rubrique) {
        repository.update(rubrique);
    }

    public void deleteRubrique(Rubrique rubrique) {
        repository.delete(rubrique);
    }
}
