package com.tp.gestiondepenses.ui.fragments;

import android.app.DatePickerDialog;
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

import com.tp.gestiondepenses.databinding.FragmentAddDepenseBinding;
import com.tp.gestiondepenses.model.Categorie;
import com.tp.gestiondepenses.model.Depense;
import com.tp.gestiondepenses.viewmodel.FinanceViewModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AddDepenseFragment extends Fragment {

    private FragmentAddDepenseBinding binding;
    private FinanceViewModel viewModel;
    private List<Categorie> categories = new ArrayList<>();
    private List<com.tp.gestiondepenses.model.Rubrique> currentRubriques = new ArrayList<>();
    private long selectedDate = System.currentTimeMillis();
    private int editId = -1;
    private Depense existingDepense;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        binding = FragmentAddDepenseBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(this).get(FinanceViewModel.class);

        setupMoyenPaiementChips();

        if (getArguments() != null && getArguments().containsKey("transactionId")) {
            editId = getArguments().getInt("transactionId");
            binding.tvTitle.setText("Modifier une dépense");
        }

        viewModel.getAllCategories().observe(getViewLifecycleOwner(), cats -> {
            if (cats != null) {
                categories = cats;
                binding.cgCategorie.removeAllViews();
                for (Categorie c : cats) {
                    com.google.android.material.chip.Chip chip = new com.google.android.material.chip.Chip(
                            getContext());
                    chip.setText(c.nom);
                    chip.setCheckable(true);
                    chip.setId(View.generateViewId());
                    chip.setTag(c.id);
                    if (c.couleur != null && !c.couleur.isEmpty()) {
                        try {
                            chip.setChipIcon(
                                    androidx.core.content.ContextCompat.getDrawable(getContext(), R.drawable.ic_dot));
                            chip.setChipIconTint(android.content.res.ColorStateList
                                    .valueOf(android.graphics.Color.parseColor(c.couleur)));
                            chip.setChipIconVisible(true);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    binding.cgCategorie.addView(chip);
                }
                
                if (editId != -1) {
                    loadExistingDepense();
                }
            }
        });

        binding.cgCategorie.setOnCheckedStateChangeListener((group, checkedIds) -> {
            if (!checkedIds.isEmpty()) {
                com.google.android.material.chip.Chip selectedChip = group.findViewById(checkedIds.get(0));
                if (selectedChip != null && selectedChip.getTag() != null) {
                    int catId = (Integer) selectedChip.getTag();
                    viewModel.getRubriquesByCategorie(catId).observe(getViewLifecycleOwner(), rubriques -> {
                        if (rubriques != null) {
                            currentRubriques = rubriques;
                            binding.cgRubrique.removeAllViews();

                            com.google.android.material.chip.Chip chipAucune = new com.google.android.material.chip.Chip(
                                    getContext());
                            chipAucune.setText("Aucune");
                            chipAucune.setCheckable(true);
                            chipAucune.setId(View.generateViewId());
                            binding.cgRubrique.addView(chipAucune);
                            binding.cgRubrique.check(chipAucune.getId());

                            for (com.tp.gestiondepenses.model.Rubrique r : rubriques) {
                                com.google.android.material.chip.Chip chip = new com.google.android.material.chip.Chip(
                                        getContext());
                                chip.setText(r.nom);
                                chip.setCheckable(true);
                                chip.setId(View.generateViewId());
                                chip.setTag(r.id);
                                binding.cgRubrique.addView(chip);
                            }
                        }
                    });
                }
            }
        });

        binding.btnBack.setOnClickListener(v -> Navigation.findNavController(requireView()).navigateUp());
        binding.btnPickDate.setOnClickListener(v -> showDatePicker());
        binding.btnSave.setOnClickListener(v -> saveDepense());

        if (editId == -1) {
            updateDateDisplay();
        }
    }

    private void updateDateDisplay() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        binding.tvSelectedDate.setText(sdf.format(new Date(selectedDate)));
    }

    private void setupMoyenPaiementChips() {
        String[] moyens = { "Espèces", "Mobile Money", "Carte", "Autre" };
        binding.cgMoyenPaiement.removeAllViews();
        for (String m : moyens) {
            com.google.android.material.chip.Chip chip = new com.google.android.material.chip.Chip(getContext());
            chip.setText(m);
            chip.setCheckable(true);
            chip.setId(View.generateViewId());
            binding.cgMoyenPaiement.addView(chip);
        }
        if (binding.cgMoyenPaiement.getChildCount() > 0) {
            binding.cgMoyenPaiement.check(binding.cgMoyenPaiement.getChildAt(0).getId());
        }
    }

    private void showDatePicker() {
        Calendar cal = Calendar.getInstance();
        DatePickerDialog dialog = new DatePickerDialog(requireContext(), (view, year, month, dayOfMonth) -> {
            cal.set(year, month, dayOfMonth);
            selectedDate = cal.getTimeInMillis();
            updateDateDisplay();
        }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
        dialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        dialog.show();
    }

    private void loadExistingDepense() {
        viewModel.getDepenseById(editId).observe(getViewLifecycleOwner(), d -> {
            if (d != null && existingDepense == null) {
                existingDepense = d;
                binding.etMontant.setText(String.valueOf(d.montant));
                binding.etDescription.setText(d.description);
                selectedDate = d.date;
                updateDateDisplay();

                // Check category chip
                for (int i = 0; i < binding.cgCategorie.getChildCount(); i++) {
                    com.google.android.material.chip.Chip chip = (com.google.android.material.chip.Chip) binding.cgCategorie.getChildAt(i);
                    if (chip.getTag() != null && (Integer) chip.getTag() == d.categorie_id) {
                        chip.setChecked(true);
                        break;
                    }
                }

                // Check moyen paiement chip
                for (int i = 0; i < binding.cgMoyenPaiement.getChildCount(); i++) {
                    com.google.android.material.chip.Chip chip = (com.google.android.material.chip.Chip) binding.cgMoyenPaiement.getChildAt(i);
                    if (chip.getText().toString().equals(d.moyen_paiement)) {
                        chip.setChecked(true);
                        break;
                    }
                }
                
                // Note: Rubriques depend on Category observer which will trigger automatically
            }
        });
    }

    private void saveDepense() {
        String montantStr = binding.etMontant.getText().toString().trim();
        String description = binding.etDescription.getText().toString().trim();

        if (montantStr.isEmpty()) {
            binding.etMontant.setError("Montant requis");
            return;
        }

        // Sécurité : bloquer une date future
        if (selectedDate > System.currentTimeMillis()) {
            Toast.makeText(getContext(), "La date ne peut pas être dans le futur", Toast.LENGTH_SHORT).show();
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

        int selectedCatId = binding.cgCategorie.getCheckedChipId();
        if (selectedCatId == View.NO_ID) {
            Toast.makeText(getContext(), "Veuillez choisir une catégorie", Toast.LENGTH_SHORT).show();
            return;
        }
        com.google.android.material.chip.Chip catChip = binding.cgCategorie.findViewById(selectedCatId);
        int catId = (Integer) catChip.getTag();

        int selectedRubId = binding.cgRubrique.getCheckedChipId();
        Integer rubId = null;
        if (selectedRubId != View.NO_ID) {
            com.google.android.material.chip.Chip rubChip = binding.cgRubrique.findViewById(selectedRubId);
            if (rubChip != null && rubChip.getTag() != null) {
                rubId = (Integer) rubChip.getTag();
            }
        }

        String moyen = "Espèces";
        int selectedMoyenId = binding.cgMoyenPaiement.getCheckedChipId();
        if (selectedMoyenId != View.NO_ID) {
            com.google.android.material.chip.Chip moyenChip = binding.cgMoyenPaiement.findViewById(selectedMoyenId);
            if (moyenChip != null)
                moyen = moyenChip.getText().toString();
        }

        if (editId != -1 && existingDepense != null) {
            existingDepense.categorie_id = catId;
            existingDepense.rubrique_id = rubId;
            existingDepense.montant = montant;
            existingDepense.date = selectedDate;
            existingDepense.description = description;
            existingDepense.moyen_paiement = moyen;
            viewModel.updateDepense(existingDepense);
        } else {
            Depense d = new Depense(catId, rubId, montant, selectedDate, description, moyen);
            viewModel.insertDepense(d);
        }

        Toast.makeText(getContext(), R.string.msg_expense_saved, Toast.LENGTH_SHORT).show();
        Navigation.findNavController(requireView()).navigateUp();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
