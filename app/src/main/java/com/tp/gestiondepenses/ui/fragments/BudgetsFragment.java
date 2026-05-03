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
import androidx.recyclerview.widget.LinearLayoutManager;

import com.tp.gestiondepenses.R;
import com.tp.gestiondepenses.databinding.FragmentBudgetsBinding;
import com.tp.gestiondepenses.model.Budget;
import com.tp.gestiondepenses.model.Categorie;
import com.tp.gestiondepenses.model.Depense;
import com.tp.gestiondepenses.ui.adapters.BudgetAlertAdapter;
import com.tp.gestiondepenses.utils.DateUtils;
import com.tp.gestiondepenses.viewmodel.FinanceViewModel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class BudgetsFragment extends Fragment {

    private FragmentBudgetsBinding binding;
    private FinanceViewModel viewModel;
    private BudgetAlertAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentBudgetsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(this).get(FinanceViewModel.class);

        adapter = new BudgetAlertAdapter();
        adapter.setOnBudgetDeleteListener(budget -> {
            new com.google.android.material.dialog.MaterialAlertDialogBuilder(requireContext())
                .setTitle("Supprimer le budget")
                .setMessage("Voulez-vous vraiment supprimer ce budget ?")
                .setNegativeButton("Annuler", null)
                .setPositiveButton("Supprimer", (dialog, which) -> {
                    viewModel.deleteBudget(budget);
                    Toast.makeText(getContext(), "Budget supprimé", Toast.LENGTH_SHORT).show();
                })
                .show();
        });
        adapter.setOnBudgetEditListener(budget -> {
            Bundle args = new Bundle();
            args.putInt("transactionId", budget.id);
            Navigation.findNavController(requireView()).navigate(R.id.navigation_add_budget, args);
        });
        binding.rvBudgets.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rvBudgets.setAdapter(adapter);

        binding.fabAddBudget.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.navigation_add_budget);
        });

        loadBudgets();
    }

    private void loadBudgets() {
        Calendar cal = Calendar.getInstance();
        int currentMois = cal.get(Calendar.MONTH) + 1; // 1-12
        int currentAnnee = cal.get(Calendar.YEAR);

        viewModel.getAllBudgets().observe(getViewLifecycleOwner(), budgets -> {
            viewModel.getAllCategories().observe(getViewLifecycleOwner(), categories -> {
                viewModel.getAllDepenses().observe(getViewLifecycleOwner(), depenses -> {
                    if (budgets != null && categories != null && depenses != null) {
                        List<BudgetAlertAdapter.BudgetAlertItem> items = new ArrayList<>();
                        for (Budget b : budgets) {
                            if (b.mois == currentMois && b.annee == currentAnnee) {
                                double spent = calculateSpentForBudget(b, depenses);
                                Categorie cat = null;
                                if (b.categorie_id != null) {
                                    for (Categorie c : categories) {
                                        if (c.id == b.categorie_id) {
                                            cat = c;
                                            break;
                                        }
                                    }
                                }
                                items.add(new BudgetAlertAdapter.BudgetAlertItem(b, cat, spent));
                            }
                        }
                        adapter.setItems(items);
                    }
                });
            });
        });
    }

    private double calculateSpentForBudget(Budget budget, List<Depense> depenses) {
        double total = 0;
        Calendar cal = Calendar.getInstance();
        for (Depense d : depenses) {
            cal.setTimeInMillis(d.date);
            int dMois = cal.get(Calendar.MONTH) + 1;
            int dAnnee = cal.get(Calendar.YEAR);

            if (dMois == budget.mois && dAnnee == budget.annee) {
                // If it's a specific category budget
                if (budget.categorie_id != null) {
                    if (d.categorie_id == budget.categorie_id) {
                        total += d.montant;
                    }
                } else {
                    // Global budget
                    total += d.montant;
                }
            }
        }
        return total;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
