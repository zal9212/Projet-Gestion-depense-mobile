package sn.esmt.financetrack.ui.categorie;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import sn.esmt.financetrack.R;
import sn.esmt.financetrack.data.entity.Categorie;
import sn.esmt.financetrack.databinding.FragmentCategorieBinding;

public class CategorieFragment extends Fragment {

    private FragmentCategorieBinding binding;
    private CategorieViewModel categorieViewModel;
    private CategorieAdapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentCategorieBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        adapter = new CategorieAdapter(this::showDeleteConfirmation);
        binding.recyclerCategories.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.recyclerCategories.setAdapter(adapter);

        categorieViewModel = new ViewModelProvider(this).get(CategorieViewModel.class);
        categorieViewModel.getAllCategories().observe(getViewLifecycleOwner(), categories -> {
            adapter.setCategories(categories);
        });

        binding.fabAddCategorie.setOnClickListener(v -> showAddCategorieDialog());
    }

    private void showAddCategorieDialog() {
        View dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_add_categorie, null);
        EditText editNom = dialogView.findViewById(R.id.edit_nom_categorie);
        EditText editCouleur = dialogView.findViewById(R.id.edit_couleur);

        new AlertDialog.Builder(requireContext())
                .setView(dialogView)
                .setPositiveButton("Ajouter", (dialog, which) -> {
                    String nom = editNom.getText().toString();
                    String couleur = editCouleur.getText().toString();
                    if (!nom.isEmpty()) {
                        categorieViewModel.insert(new Categorie(nom, "category", couleur, false));
                    }
                })
                .setNegativeButton("Annuler", null)
                .show();
    }

    private void showDeleteConfirmation(Categorie categorie) {
        new AlertDialog.Builder(requireContext())
                .setTitle("Supprimer la catégorie")
                .setMessage("Voulez-vous vraiment supprimer la catégorie '" + categorie.getNom() + "' ?")
                .setPositiveButton("Supprimer", (dialog, which) -> {
                    categorieViewModel.delete(categorie);
                })
                .setNegativeButton("Annuler", null)
                .show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
