package sn.esmt.financetrack.ui.categorie;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import sn.esmt.financetrack.data.entity.Categorie;
import sn.esmt.financetrack.data.repository.CategorieRepository;

public class CategorieViewModel extends AndroidViewModel {
    private CategorieRepository mRepository;
    private LiveData<List<Categorie>> mAllCategories;

    public CategorieViewModel(Application application) {
        super(application);
        mRepository = new CategorieRepository(application);
        mAllCategories = mRepository.getAllCategories();
    }

    public LiveData<List<Categorie>> getAllCategories() {
        return mAllCategories;
    }

    public void insert(Categorie categorie) { mRepository.insert(categorie); }
    public void update(Categorie categorie) { mRepository.update(categorie); }
    public void delete(Categorie categorie) { mRepository.delete(categorie); }
}
