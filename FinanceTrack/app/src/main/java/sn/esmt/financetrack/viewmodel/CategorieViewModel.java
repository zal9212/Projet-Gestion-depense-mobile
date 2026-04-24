package sn.esmt.financetrack.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import sn.esmt.financetrack.model.Categorie;
import sn.esmt.financetrack.repository.CategorieRepository;

public class CategorieViewModel extends AndroidViewModel {
    private CategorieRepository repository;
    private LiveData<List<Categorie>> allCategories;

    public CategorieViewModel(@NonNull Application application) {
        super(application);
        repository = new CategorieRepository(application);
        allCategories = repository.getAllCategories();
    }

    public LiveData<List<Categorie>> getAllCategories() {
        return allCategories;
    }

    public void addCategorie(String nom, String icone, String couleur) {
        Categorie categorie = new Categorie(nom, icone, couleur, false);
        repository.insert(categorie);
    }

    public void updateCategorie(Categorie categorie) {
        repository.update(categorie);
    }

    public void deleteCategorie(Categorie categorie) {
        repository.delete(categorie);
    }
}
