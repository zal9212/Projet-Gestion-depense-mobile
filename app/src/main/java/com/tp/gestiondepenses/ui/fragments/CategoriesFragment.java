package com.tp.gestiondepenses.ui.fragments;

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
import androidx.navigation.Navigation;

import com.tp.gestiondepenses.databinding.FragmentCategoriesBinding;
import com.tp.gestiondepenses.ui.adapters.CategorieAdapter;
import com.tp.gestiondepenses.viewmodel.FinanceViewModel;
import com.tp.gestiondepenses.model.Categorie;

import java.util.List;

public class CategoriesFragment extends Fragment {

    private FragmentCategoriesBinding binding;
    private FinanceViewModel viewModel;
    private CategorieAdapter adapter;
    private String selectedAddColor = "#FF5252";
    private Categorie editingCategorie = null;
    private final String[] allPotentialColors = {
        "#FF5252", "#448AFF", "#4CAF50", "#FFEB3B", "#FF9800",
        "#9C27B0", "#E91E63", "#009688", "#607D8B", "#212121",
        "#AAFF00", "#FF4081", "#7C4DFF", "#00BCD4", "#FF5722",
        "#795548", "#CDDC39", "#009688", "#3F51B5", "#F44336"
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentCategoriesBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(this).get(FinanceViewModel.class);

        adapter = new CategorieAdapter();
        adapter.setViewModel(viewModel, getViewLifecycleOwner());
        adapter.setOnCategorieActionListener(new CategorieAdapter.OnCategorieActionListener() {
            @Override
            public void onEdit(Categorie cat) {
                editingCategorie = cat;
                binding.btnAddCategory.setVisibility(View.GONE);
                binding.cardAddCategory.setVisibility(View.VISIBLE);
                binding.etCategoryName.setText(cat.nom);
                binding.btnConfirmAdd.setText("Modifier");
                selectedAddColor = cat.couleur;
                setupAddCategoryColors(adapter.categories != null ? adapter.categories : new java.util.ArrayList<>());
            }

            @Override
            public void onDelete(Categorie cat) {
                new com.google.android.material.dialog.MaterialAlertDialogBuilder(requireContext())
                    .setTitle("Supprimer la catégorie")
                    .setMessage("Voulez-vous supprimer la catégorie \"" + cat.nom + "\" et toutes ses rubriques ? Cette action est irréversible.")
                    .setNegativeButton("Annuler", null)
                    .setPositiveButton("Supprimer", (dialog, which) -> {
                        viewModel.deleteCategorie(cat);
                        Toast.makeText(getContext(), "Catégorie supprimée", Toast.LENGTH_SHORT).show();
                    })
                    .show();
            }
        });
        binding.rvCategories.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rvCategories.setAdapter(adapter);

        viewModel.getAllCategories().observe(getViewLifecycleOwner(), categories -> {
            if (categories != null) {
                adapter.setCategories(categories);
                setupAddCategoryColors(categories);
            }
        });

        binding.btnBack.setOnClickListener(v -> Navigation.findNavController(requireView()).navigateUp());

        binding.btnReset.setOnClickListener(v -> {
            new com.google.android.material.dialog.MaterialAlertDialogBuilder(requireContext())
                .setTitle("Réinitialiser les catégories")
                .setMessage("Voulez-vous restaurer les catégories par défaut ? Cela effacera toutes vos catégories personnalisées.")
                .setNegativeButton("Annuler", null)
                .setPositiveButton("Réinitialiser", (dialog, which) -> {
                    viewModel.resetCategories();
                })
                .show();
        });
        
        binding.btnAddCategory.setOnClickListener(v -> {
            editingCategorie = null;
            binding.btnConfirmAdd.setText("Ajouter");
            binding.btnAddCategory.setVisibility(View.GONE);
            binding.cardAddCategory.setVisibility(View.VISIBLE);
        });

        binding.btnCancelAdd.setOnClickListener(v -> {
            editingCategorie = null;
            binding.cardAddCategory.setVisibility(View.GONE);
            binding.btnAddCategory.setVisibility(View.VISIBLE);
            binding.etCategoryName.setText("");
        });

        binding.btnConfirmAdd.setOnClickListener(v -> {
            String name = binding.etCategoryName.getText().toString().trim();
            if (name.isEmpty()) {
                Toast.makeText(getContext(), "Le nom est requis", Toast.LENGTH_SHORT).show();
                return;
            }

            if (editingCategorie != null) {
                editingCategorie.nom = name;
                editingCategorie.couleur = selectedAddColor;
                viewModel.updateCategorie(editingCategorie);
                Toast.makeText(getContext(), "Catégorie modifiée", Toast.LENGTH_SHORT).show();
            } else {
                Categorie cat = new Categorie(name, "tag", selectedAddColor, false);
                viewModel.insertCategorie(cat);
                Toast.makeText(getContext(), "Catégorie ajoutée", Toast.LENGTH_SHORT).show();
            }
            
            editingCategorie = null;
            binding.cardAddCategory.setVisibility(View.GONE);
            binding.btnAddCategory.setVisibility(View.VISIBLE);
            binding.etCategoryName.setText("");
        });
    }

    private void setupAddCategoryColors(List<Categorie> existingCategories) {
        java.util.Set<String> usedColors = new java.util.HashSet<>();
        for (Categorie c : existingCategories) {
            usedColors.add(c.couleur.toUpperCase());
        }

        binding.cgAddColors.removeAllViews();
        int addedCount = 0;
        for (String colorHex : allPotentialColors) {
            if (!usedColors.contains(colorHex.toUpperCase())) {
                addColorChip(colorHex);
                addedCount++;
                if (addedCount >= 8) break; // Propose 8 unused colors
            }
        }

        // If not enough unused colors, just show some anyway or generate random
        if (addedCount < 4) {
            for (String colorHex : allPotentialColors) {
                if (addedCount >= 8) break;
                if (!usedColors.contains(colorHex.toUpperCase())) continue; // Already added
                addColorChip(colorHex);
                addedCount++;
            }
        }
        
        if (binding.cgAddColors.getChildCount() > 0) {
            View firstColor = binding.cgAddColors.getChildAt(0);
            firstColor.performClick();
        }
    }

    private void addColorChip(String colorHex) {
        View colorDot = new View(getContext());
        int size = (int) (36 * getResources().getDisplayMetrics().density);
        int margin = (int) (8 * getResources().getDisplayMetrics().density);
        
        android.view.ViewGroup.MarginLayoutParams params = 
            new android.view.ViewGroup.MarginLayoutParams(size, size);
        params.setMargins(0, 0, margin, 0);
        colorDot.setLayoutParams(params);
        
        android.graphics.drawable.GradientDrawable drawable = new android.graphics.drawable.GradientDrawable();
        drawable.setShape(android.graphics.drawable.GradientDrawable.OVAL);
        try {
            drawable.setColor(android.graphics.Color.parseColor(colorHex));
        } catch (Exception e) {
            drawable.setColor(android.graphics.Color.GRAY);
        }
        
        // Selection border
        colorDot.setBackground(drawable);
        
        colorDot.setOnClickListener(v -> {
            selectedAddColor = colorHex;
            // Update UI for selection (simple way: alpha or border)
            for (int i = 0; i < binding.cgAddColors.getChildCount(); i++) {
                View child = binding.cgAddColors.getChildAt(i);
                child.setAlpha(0.5f);
                ((android.graphics.drawable.GradientDrawable)child.getBackground()).setStroke(0, 0);
            }
            v.setAlpha(1.0f);
            ((android.graphics.drawable.GradientDrawable)v.getBackground()).setStroke(6, android.graphics.Color.WHITE);
        });

        // Default selection for the first one
        if (binding.cgAddColors.getChildCount() == 0) {
            colorDot.setAlpha(1.0f);
            drawable.setStroke(6, android.graphics.Color.WHITE);
            selectedAddColor = colorHex;
        } else {
            colorDot.setAlpha(0.5f);
        }

        binding.cgAddColors.addView(colorDot);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
