package sn.esmt.financetrack.ui.categories;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import sn.esmt.financetrack.R;
import sn.esmt.financetrack.adapter.CategorieAdapter;
import sn.esmt.financetrack.model.Categorie;
import sn.esmt.financetrack.viewmodel.CategorieViewModel;


// On implémente OnCategorieListener pour réagir quand l'Adapter crie "On a cliqué sur Supprimer !"
public class CategoriesFragment extends Fragment implements CategorieAdapter.OnCategorieListener {

    // Nos deux outils principaux pour cet écran :
    private CategorieViewModel viewModel; // Pour parler avec la base de données
    private CategorieAdapter adapter;     // Notre fameux cuisinier pour le RecyclerView

    // ÉTAPE 3 : onCreateView (Lier le design)
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // On demande à Android d'afficher notre fichier XML fragment_categories
        return inflater.inflate(R.layout.fragment_categories, container, false);
    }
     // ÉTAPE 4 : onViewCreated (Initialiser les composants)
    // Cette méthode est appelée automatiquement juste après que le design (XML) ait été chargé
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // 1. On prépare notre ViewModel (qui fait le pont avec la base de données)
        viewModel = new ViewModelProvider(this).get(CategorieViewModel.class);

        // 2. On prépare le RecyclerView (notre "salle à manger")
        RecyclerView rv = view.findViewById(R.id.recyclerCategories);
        rv.setLayoutManager(new LinearLayoutManager(getContext())); // Affichage en liste de haut en bas
        
        // 3. On crée notre Cuisinier (l'Adapter) et on le donne au RecyclerView
        adapter = new CategorieAdapter(this); // 'this' fonctionne car on a signé le contrat !
        rv.setAdapter(adapter);

        // 4. LA MAGIE DE ROOM : On observe les données
        // Dès qu'une catégorie est ajoutée, modifiée ou supprimée, ce bloc se déclenche tout seul !
        viewModel.getAllCategories().observe(getViewLifecycleOwner(), listeCategories -> {
            adapter.setCategories(listeCategories); // On donne les nouvelles données à l'Adapter
        });

        // 5. On écoute le clic sur le gros bouton + (fabAddCategorie)
        view.findViewById(R.id.fabAddCategorie).setOnClickListener(v -> {
            showAddDialog(null); // On veut ajouter, donc on passe 'null' (vide)
        });
    }

        // ÉTAPE 5 : La méthode qui construit et affiche le popup (Dialog)
    private void showAddDialog(Categorie categorieAEditer) {
        // 1. On charge le design de notre popup
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_add_categorie, null);

        // 2. On récupère les champs de texte
        TextInputEditText etNom = dialogView.findViewById(R.id.etNomCategorie);
        TextInputEditText etIcone = dialogView.findViewById(R.id.etIcone);

        // 3. Si on est en mode "Modification", on pré-remplit les champs
        if (categorieAEditer != null) {
            etNom.setText(categorieAEditer.getNom());
            etIcone.setText(categorieAEditer.getIcone());
        }

        // 4. On détermine le titre du popup
        String titre = (categorieAEditer == null) ? "Nouvelle catégorie" : "Modifier la catégorie";

        // 5. On crée et on affiche la fenêtre (Material Design)
        new MaterialAlertDialogBuilder(requireContext())
            .setTitle(titre)
            .setView(dialogView)
            .setNegativeButton("Annuler", null) // Bouton Annuler qui ferme juste la fenêtre
            .setPositiveButton("Enregistrer", (dialog, which) -> {
                
                // --- QUE FAIRE QUAND ON CLIQUE SUR ENREGISTRER ? ---
                
                // On récupère ce que l'utilisateur a tapé
                String nom = etNom.getText() != null ? etNom.getText().toString().trim() : "";
                String icone = etIcone.getText() != null ? etIcone.getText().toString().trim() : "📦"; // par défaut

                // Vérification : le nom ne doit pas être vide
                if (nom.isEmpty()) {
                    Snackbar.make(requireView(), "Le nom est obligatoire", Snackbar.LENGTH_SHORT).show();
                    return; // On arrête là
                }

                // Sauvegarde dans la base de données via le ViewModel
                if (categorieAEditer == null) {
                    // C'est une NOUVELLE catégorie
                    // (On met un bleu par défaut pour le cercle)
                    viewModel.addCategorie(nom, icone, "#1A237E"); 
                    Snackbar.make(requireView(), "Catégorie ajoutée avec succès", Snackbar.LENGTH_SHORT).show();
                } else {
                    // C'est une MODIFICATION
                    categorieAEditer.setNom(nom);
                    categorieAEditer.setIcone(icone);
                    viewModel.updateCategorie(categorieAEditer);
                    Snackbar.make(requireView(), "Catégorie modifiée", Snackbar.LENGTH_SHORT).show();
                }

            })
            .show(); // Affiche le popup à l'écran !
    }

    @Override
    public void onEdit(Categorie c) {
        showAddDialog(c); // On veut modifier, donc on donne la catégorie cliquée
    }

       // ÉTAPE 6 : Gérer le clic sur le bouton "Supprimer"
    @Override
    public void onDelete(Categorie c) {
        // On crée une boîte de dialogue pour confirmer avant de supprimer
        new MaterialAlertDialogBuilder(requireContext())
            .setTitle("Supprimer la catégorie ?")
            .setMessage("Attention, cette action est irréversible.")
            .setNegativeButton("Annuler", null)
            .setPositiveButton("Supprimer", (dialog, which) -> {
                // Si l'utilisateur clique sur "Supprimer" :
                viewModel.deleteCategorie(c); // On demande au ViewModel de supprimer
                Snackbar.make(requireView(), "Catégorie supprimée", Snackbar.LENGTH_SHORT).show();
            })
            .show();
    }

    // ÉTAPE 7 : Gérer le clic sur la carte entière (pour voir les rubriques)
    @Override
    public void onClick(Categorie c) {
        // Création du bundle avec les paramètres à envoyer
        Bundle bundle = new Bundle();
        bundle.putInt("categorieId", c.getId());
        bundle.putString("categorieNom", c.getNom());
        
        // Navigation vers l'écran des rubriques
        androidx.navigation.Navigation.findNavController(requireView())
                .navigate(R.id.action_categories_to_rubriques, bundle);
    }
}