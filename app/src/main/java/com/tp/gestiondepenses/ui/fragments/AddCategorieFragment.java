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
import androidx.navigation.Navigation;

import com.tp.gestiondepenses.databinding.FragmentAddCategorieBinding;
import com.tp.gestiondepenses.model.Categorie;
import com.tp.gestiondepenses.viewmodel.FinanceViewModel;

public class AddCategorieFragment extends Fragment {

    private FragmentAddCategorieBinding binding;
    private FinanceViewModel viewModel;
    private String selectedColor = "#448AFF"; // Default color

    private final String[] presetColors = {
        "#448AFF", "#4CAF50", "#FF5252", "#FFEB3B", "#FF9800",
        "#9C27B0", "#E91E63", "#009688", "#607D8B", "#212121",
        "#AAFF00", "#FF4081", "#7C4DFF", "#00BCD4"
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentAddCategorieBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(this).get(FinanceViewModel.class);

        setupColorPicker();

        binding.btnBack.setOnClickListener(v -> Navigation.findNavController(requireView()).navigateUp());
        binding.btnSave.setOnClickListener(v -> {
            String nom = binding.etNom.getText().toString().trim();
            
            if (nom.isEmpty()) {
                Toast.makeText(getContext(), "Le nom est requis", Toast.LENGTH_SHORT).show();
                return;
            }

            Categorie cat = new Categorie(nom, "tag", selectedColor, false);
            viewModel.insertCategorie(cat);
            
            Toast.makeText(getContext(), "Catégorie créée", Toast.LENGTH_SHORT).show();
            Navigation.findNavController(requireView()).navigateUp();
        });
    }

    private void setupColorPicker() {
        binding.cgColors.removeAllViews();
        for (String colorHex : presetColors) {
            com.google.android.material.chip.Chip chip = new com.google.android.material.chip.Chip(getContext());
            chip.setText("");
            chip.setCheckable(true);
            
            // Ensure a perfect circle
            int size = (int) (32 * getResources().getDisplayMetrics().density);
            chip.setChipMinHeight((float) size);
            chip.setChipCornerRadius((float) size / 2);
            chip.setPadding(0, 0, 0, 0);
            chip.setChipStartPadding(0f);
            chip.setChipEndPadding(0f);
            chip.setLayoutParams(new android.view.ViewGroup.LayoutParams(size, size));
            
            try {
                int color = android.graphics.Color.parseColor(colorHex);
                chip.setChipBackgroundColor(android.content.res.ColorStateList.valueOf(color));
                chip.setChipStrokeWidth(4f);
                chip.setChipStrokeColor(android.content.res.ColorStateList.valueOf(android.graphics.Color.WHITE));
            } catch (Exception e) {}

            chip.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    selectedColor = colorHex;
                }
            });

            binding.cgColors.addView(chip);
            
            // Sélectionner par défaut le premier
            if (colorHex.equals(selectedColor)) {
                chip.setChecked(true);
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
