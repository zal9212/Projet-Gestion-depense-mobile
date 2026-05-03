package com.tp.gestiondepenses.ui.fragments;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.chip.Chip;
import com.tp.gestiondepenses.R;
import com.tp.gestiondepenses.databinding.FragmentDepensesBinding;
import com.tp.gestiondepenses.model.Categorie;
import com.tp.gestiondepenses.model.Depense;
import com.tp.gestiondepenses.ui.adapters.TransactionAdapter;
import com.tp.gestiondepenses.utils.DateUtils;
import com.tp.gestiondepenses.viewmodel.FinanceViewModel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class DepensesFragment extends Fragment {

    private FragmentDepensesBinding binding;
    private FinanceViewModel viewModel;
    private TransactionAdapter adapter;
    private List<Depense> allDepenses = new ArrayList<>();
    private String currentFilter = "Mois";
    private String searchQuery = "";
    private Integer selectedCategoryId = null;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentDepensesBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(this).get(FinanceViewModel.class);

        adapter = new TransactionAdapter();
        adapter.setOnTransactionClickListener((id, type) -> {
            Bundle args = new Bundle();
            args.putInt("transactionId", id);
            args.putString("type", "depense");
            androidx.navigation.Navigation.findNavController(requireView()).navigate(R.id.navigation_transaction_detail, args);
        });
        binding.rvDepenses.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rvDepenses.setAdapter(adapter);

        viewModel.getAllDepenses().observe(getViewLifecycleOwner(), depenses -> {
            allDepenses = depenses;
            applyFilters();
        });

        viewModel.getAllCategories().observe(getViewLifecycleOwner(), categories -> {
            if (categories != null) {
                adapter.setCategories(categories);
            }
            setupCategoryChips(categories);
        });

        setupFilterListeners();
        setupSearch();

        binding.fabAdd.setOnClickListener(v -> {
            androidx.navigation.Navigation.findNavController(v).navigate(R.id.navigation_add_depense);
        });
    }

    private void setupSearch() {
        binding.etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchQuery = s.toString().toLowerCase();
                applyFilters();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void setupFilterListeners() {
        binding.cgFilters.setOnCheckedStateChangeListener((group, checkedIds) -> {
            if (checkedIds.isEmpty()) return;
            int id = checkedIds.get(0);
            if (id == binding.chipTout.getId()) currentFilter = "Tout";
            else if (id == binding.chipJour.getId()) currentFilter = "Jour";
            else if (id == binding.chipSemaine.getId()) currentFilter = "Semaine";
            else if (id == binding.chipMois.getId()) currentFilter = "Mois";
            applyFilters();
        });
    }

    private void setupCategoryChips(List<Categorie> categories) {
        binding.cgCategories.removeAllViews();

        Chip allChip = new Chip(getContext());
        allChip.setText(getString(R.string.all_categories));
        allChip.setCheckable(true);
        allChip.setChecked(selectedCategoryId == null);
        allChip.setId(View.generateViewId());
        allChip.setOnClickListener(v -> {
            selectedCategoryId = null;
            applyFilters();
        });
        binding.cgCategories.addView(allChip);

        for (Categorie cat : categories) {
            Chip chip = new Chip(getContext());
            chip.setText(cat.nom);
            chip.setCheckable(true);
            chip.setId(View.generateViewId());
            // Ajouter le point de couleur comme sur les chips d'ajout
            if (cat.couleur != null && !cat.couleur.isEmpty()) {
                try {
                    chip.setChipIcon(androidx.core.content.ContextCompat.getDrawable(getContext(), R.drawable.ic_dot));
                    chip.setChipIconTint(android.content.res.ColorStateList.valueOf(android.graphics.Color.parseColor(cat.couleur)));
                    chip.setChipIconVisible(true);
                } catch (Exception e) { e.printStackTrace(); }
            }
            chip.setOnClickListener(v -> {
                selectedCategoryId = cat.id;
                applyFilters();
            });
            binding.cgCategories.addView(chip);
        }
    }

    private void applyFilters() {
        List<Depense> filtered = new ArrayList<>();
        long now = System.currentTimeMillis();
        Calendar cal = Calendar.getInstance();
        int currentMois = cal.get(Calendar.MONTH);
        int currentAnnee = cal.get(Calendar.YEAR);
        int currentDay = cal.get(Calendar.DAY_OF_YEAR);

        for (Depense d : allDepenses) {
            // Category filter
            if (selectedCategoryId != null && d.categorie_id != selectedCategoryId) continue;

            // Search filter
            if (!searchQuery.isEmpty() && d.description != null && !d.description.toLowerCase().contains(searchQuery)) {
                continue;
            }

            cal.setTimeInMillis(d.date);
            boolean pass = false;

            if (currentFilter.equals("Tout")) pass = true;
            else if (currentFilter.equals("Jour")) {
                pass = (cal.get(Calendar.DAY_OF_YEAR) == currentDay && cal.get(Calendar.YEAR) == currentAnnee);
            }
            else if (currentFilter.equals("Semaine")) {
                pass = (d.date >= now - 7 * 24 * 60 * 60 * 1000);
            }
            else if (currentFilter.equals("Mois")) {
                pass = (cal.get(Calendar.MONTH) == currentMois && cal.get(Calendar.YEAR) == currentAnnee);
            }

            if (pass) filtered.add(d);
        }

        adapter.setTransactions(filtered, new ArrayList<>());
        
        double total = 0;
        for (Depense d : filtered) total += d.montant;
        binding.tvSummaryLabel.setText(getString(R.string.label_total_count, filtered.size()));
        binding.tvSummaryAmount.setText("-" + DateUtils.formatCurrency(total));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
