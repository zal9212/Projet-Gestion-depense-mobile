package sn.esmt.financetrack.ui.rubriques;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import sn.esmt.financetrack.R;
import sn.esmt.financetrack.adapter.RubriqueAdapter;
import sn.esmt.financetrack.model.Rubrique;
import sn.esmt.financetrack.viewmodel.RubriqueViewModel;

public class RubriquesFragment extends Fragment implements RubriqueAdapter.OnRubriqueListener {

    private RubriqueViewModel viewModel;
    private RubriqueAdapter adapter;
    private int categorieId = -1; // ID de la catégorie parent
    private String categorieNom = "";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // On récupère les informations de la catégorie cliquée (si elles sont envoyées)
        if (getArguments() != null) {
            categorieId = getArguments().getInt("categorieId", -1);
            categorieNom = getArguments().getString("categorieNom", "");
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_rubriques, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Sécurité : si on n'a pas la catégorie, on prévient l'utilisateur
        if (categorieId == -1) {
            Toast.makeText(getContext(), "Erreur : Catégorie introuvable", Toast.LENGTH_SHORT).show();
        }

        // 1. Initialisation du ViewModel
        viewModel = new ViewModelProvider(this).get(RubriqueViewModel.class);

        // 2. Configuration du RecyclerView (la liste)
        RecyclerView rv = view.findViewById(R.id.recyclerRubriques);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        
        adapter = new RubriqueAdapter(this);
        rv.setAdapter(adapter);

        // 3. On observe uniquement les rubriques qui appartiennent à cette catégorie !
        viewModel.getRubriquesByCategorie(categorieId).observe(getViewLifecycleOwner(), listeRubriques -> {
            adapter.setRubriques(listeRubriques);
        });

        // 4. Action sur le bouton "Ajouter"
        view.findViewById(R.id.fabAddRubrique).setOnClickListener(v -> showAddDialog());
    }

    private void showAddDialog() {
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_add_rubrique, null);
        TextInputEditText etNom = dialogView.findViewById(R.id.etNomRubrique);

        new MaterialAlertDialogBuilder(requireContext())
            .setTitle("Nouvelle rubrique (" + categorieNom + ")")
            .setView(dialogView)
            .setNegativeButton("Annuler", null)
            .setPositiveButton("Ajouter", (dialog, which) -> {
                String nom = etNom.getText() != null ? etNom.getText().toString().trim() : "";
                
                if (nom.isEmpty()) {
                    Snackbar.make(requireView(), "Le nom est obligatoire", Snackbar.LENGTH_SHORT).show();
                    return;
                }
                
                // On enregistre la rubrique avec l'ID de sa catégorie maman
                viewModel.addRubrique(categorieId, nom);
                Snackbar.make(requireView(), "Rubrique ajoutée", Snackbar.LENGTH_SHORT).show();
            })
            .show();
    }

    @Override
    public void onDelete(Rubrique r) {
        new MaterialAlertDialogBuilder(requireContext())
            .setTitle("Supprimer la rubrique ?")
            .setMessage("Voulez-vous vraiment supprimer " + r.getNom() + " ?")
            .setNegativeButton("Annuler", null)
            .setPositiveButton("Supprimer", (dialog, which) -> {
                viewModel.deleteRubrique(r);
                Snackbar.make(requireView(), "Rubrique supprimée", Snackbar.LENGTH_SHORT).show();
            })
            .show();
    }
}
