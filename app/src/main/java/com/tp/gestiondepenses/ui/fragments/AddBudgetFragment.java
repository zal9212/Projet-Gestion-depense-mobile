package com.tp.gestiondepenses.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import com.tp.gestiondepenses.R;

import com.tp.gestiondepenses.databinding.FragmentAddBudgetBinding;
import com.tp.gestiondepenses.model.Budget;
import com.tp.gestiondepenses.model.Categorie;
import com.tp.gestiondepenses.utils.DateUtils;
import com.tp.gestiondepenses.viewmodel.FinanceViewModel;

import java.util.ArrayList;
import java.util.List;

public class AddBudgetFragment extends Fragment {

    private FragmentAddBudgetBinding binding;
    private FinanceViewModel viewModel;
    private List<Categorie> categories = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        binding = FragmentAddBudgetBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    private int editId = -1;
    private Budget existingBudget;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(this).get(FinanceViewModel.class);

        if (getArguments() != null && getArguments().containsKey("transactionId")) {
            editId = getArguments().getInt("transactionId");
        }

        viewModel.getAllCategories().observe(getViewLifecycleOwner(), cats -> {
            if (cats != null) {
                categories = cats;
                binding.cgCategorie.removeAllViews();

                com.google.android.material.chip.Chip chipGlobal = new com.google.android.material.chip.Chip(
                        getContext());
                chipGlobal.setText("Budget Global");
                chipGlobal.setCheckable(true);
                chipGlobal.setId(View.generateViewId());
                binding.cgCategorie.addView(chipGlobal);
                binding.cgCategorie.check(chipGlobal.getId());

                for (Categorie c : cats) {
                    com.google.android.material.chip.Chip chip = new com.google.android.material.chip.Chip(
                            getContext());
                    chip.setText(c.nom);
                    chip.setCheckable(true);
                    chip.setId(View.generateViewId());
                    chip.setTag(c.id);
                    binding.cgCategorie.addView(chip);
                }

                if (editId != -1) {
                    loadExistingBudget();
                }
            }
        });

        binding.btnBack.setOnClickListener(v -> Navigation.findNavController(requireView()).navigateUp());
        binding.btnSave.setOnClickListener(v -> saveBudget());
    }

    private void loadExistingBudget() {
        viewModel.getBudgetById(editId).observe(getViewLifecycleOwner(), b -> {
            if (b != null && existingBudget == null) {
                existingBudget = b;
                binding.etMontant.setText(String.valueOf(b.montant_plafond));
                
                // Set Category
                if (b.categorie_id == null) {
                    binding.cgCategorie.check(binding.cgCategorie.getChildAt(0).getId());
                } else {
                    for (int i = 0; i < binding.cgCategorie.getChildCount(); i++) {
                        com.google.android.material.chip.Chip chip = (com.google.android.material.chip.Chip) binding.cgCategorie.getChildAt(i);
                        if (chip.getTag() != null && (Integer) chip.getTag() == b.categorie_id) {
                            chip.setChecked(true);
                            break;
                        }
                    }
                }

                // Set Period
                if ("MENSUEL".equals(b.periode)) binding.cgPeriode.check(R.id.chip_mensuel);
                else if ("TRIMESTRIEL".equals(b.periode)) binding.cgPeriode.check(R.id.chip_trimestriel);
                else if ("ANNUEL".equals(b.periode)) binding.cgPeriode.check(R.id.chip_annuel);
            }
        });
    }

    private void saveBudget() {
        String montantStr = binding.etMontant.getText().toString().trim();
        if (montantStr.isEmpty()) {
            binding.etMontant.setError("Montant requis");
            return;
        }

        double montant;
        try {
            montant = Double.parseDouble(montantStr);
            if (montant <= 0) {
                binding.etMontant.setError("Le montant doit être supérieur à 0");
                return;
            }
        } catch (NumberFormatException e) {
            binding.etMontant.setError("Montant invalide");
            return;
        }

        int selectedChipId = binding.cgCategorie.getCheckedChipId();
        Integer catId = null;
        if (selectedChipId != View.NO_ID) {
            com.google.android.material.chip.Chip selectedChip = binding.cgCategorie.findViewById(selectedChipId);
            if (selectedChip != null && selectedChip.getTag() != null) {
                catId = (Integer) selectedChip.getTag();
            }
        }

        String periode = "MENSUEL";
        int selectedPeriodeId = binding.cgPeriode.getCheckedChipId();
        if (selectedPeriodeId == R.id.chip_trimestriel) periode = "TRIMESTRIEL";
        else if (selectedPeriodeId == R.id.chip_annuel) periode = "ANNUEL";

        int mois = Integer.parseInt(DateUtils.getCurrentMonth());
        int annee = Integer.parseInt(DateUtils.getCurrentYear());

        if (editId != -1 && existingBudget != null) {
            existingBudget.categorie_id = catId;
            existingBudget.montant_plafond = montant;
            existingBudget.periode = periode;
            viewModel.updateBudget(existingBudget);
        } else {
            Budget budget = new Budget(catId, montant, periode, mois, annee);
            viewModel.insertBudget(budget);
        }

        Toast.makeText(getContext(), R.string.msg_budget_saved, Toast.LENGTH_SHORT).show();
        Navigation.findNavController(requireView()).navigateUp();
    }
}
