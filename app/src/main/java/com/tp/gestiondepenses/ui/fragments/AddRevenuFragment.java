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
import com.tp.gestiondepenses.R;

import com.tp.gestiondepenses.databinding.FragmentAddRevenuBinding;
import com.tp.gestiondepenses.model.Revenu;
import com.tp.gestiondepenses.viewmodel.FinanceViewModel;

public class AddRevenuFragment extends Fragment {

    private FragmentAddRevenuBinding binding;
    private FinanceViewModel viewModel;
    private long selectedDate = System.currentTimeMillis();
    private int editId = -1;
    private Revenu existingRevenu;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        binding = FragmentAddRevenuBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(this).get(FinanceViewModel.class);
        setupSourceChips();

        if (getArguments() != null && getArguments().containsKey("transactionId")) {
            editId = getArguments().getInt("transactionId");
            binding.tvTitle.setText("Modifier un revenu");
            loadExistingRevenu();
        }

        binding.btnBack.setOnClickListener(v -> Navigation.findNavController(requireView()).navigateUp());
        binding.btnPickDate.setOnClickListener(v -> showDatePicker());
        binding.btnSave.setOnClickListener(v -> saveRevenu());

        if (editId == -1) {
            updateDateDisplay();
        }
    }

    private void updateDateDisplay() {
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy",
                java.util.Locale.getDefault());
        binding.tvSelectedDate.setText(sdf.format(new java.util.Date(selectedDate)));
    }

    private void showDatePicker() {
        java.util.Calendar cal = java.util.Calendar.getInstance();
        android.app.DatePickerDialog dialog = new android.app.DatePickerDialog(
                requireContext(),
                (view, year, month, dayOfMonth) -> {
                    cal.set(year, month, dayOfMonth);
                    selectedDate = cal.getTimeInMillis();
                    updateDateDisplay();
                },
                cal.get(java.util.Calendar.YEAR),
                cal.get(java.util.Calendar.MONTH),
                cal.get(java.util.Calendar.DAY_OF_MONTH));
        // Interdire les dates futures
        dialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        dialog.show();
    }

    private void setupSourceChips() {
        String[] sources = { "Salaire", "Commerce", "Freelance", "Don", "Autre" };
        binding.cgSource.removeAllViews();
        for (String s : sources) {
            com.google.android.material.chip.Chip chip = new com.google.android.material.chip.Chip(getContext());
            chip.setText(s);
            chip.setCheckable(true);
            chip.setId(View.generateViewId());
            binding.cgSource.addView(chip);
        }
        if (binding.cgSource.getChildCount() > 0) {
            binding.cgSource.check(binding.cgSource.getChildAt(0).getId());
        }
    }

    private void loadExistingRevenu() {
        viewModel.getRevenuById(editId).observe(getViewLifecycleOwner(), r -> {
            if (r != null && existingRevenu == null) {
                existingRevenu = r;
                binding.etMontant.setText(String.valueOf(r.montant));
                selectedDate = r.date;
                updateDateDisplay();

                // Check source chip
                for (int i = 0; i < binding.cgSource.getChildCount(); i++) {
                    com.google.android.material.chip.Chip chip = (com.google.android.material.chip.Chip) binding.cgSource.getChildAt(i);
                    if (chip.getText().toString().equals(r.source)) {
                        chip.setChecked(true);
                        break;
                    }
                }
            }
        });
    }

    private void saveRevenu() {
        String montantStr = binding.etMontant.getText().toString().trim();
        String source = "Source inconnue";
        int selectedSourceId = binding.cgSource.getCheckedChipId();
        if (selectedSourceId != View.NO_ID) {
            com.google.android.material.chip.Chip sourceChip = binding.cgSource.findViewById(selectedSourceId);
            if (sourceChip != null)
                source = sourceChip.getText().toString();
        }

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

        if (editId != -1 && existingRevenu != null) {
            existingRevenu.source = source.isEmpty() ? "Source inconnue" : source;
            existingRevenu.montant = montant;
            existingRevenu.date = selectedDate;
            viewModel.updateRevenu(existingRevenu);
        } else {
            Revenu r = new Revenu(source.isEmpty() ? "Source inconnue" : source, montant, selectedDate, "");
            viewModel.insertRevenu(r);
        }

        Toast.makeText(getContext(), R.string.msg_revenue_saved, Toast.LENGTH_SHORT).show();
        Navigation.findNavController(requireView()).navigateUp();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
