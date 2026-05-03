package com.tp.gestiondepenses.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.tp.gestiondepenses.R;
import com.tp.gestiondepenses.databinding.FragmentDashboardBinding;
import com.tp.gestiondepenses.model.Budget;
import com.tp.gestiondepenses.model.Categorie;
import com.tp.gestiondepenses.model.Depense;
import com.tp.gestiondepenses.ui.adapters.BudgetAlertAdapter;
import com.tp.gestiondepenses.ui.adapters.TransactionAdapter;
import com.tp.gestiondepenses.ui.custom.CategoryDistributionView;
import com.tp.gestiondepenses.utils.DateUtils;
import com.tp.gestiondepenses.viewmodel.FinanceViewModel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DashboardFragment extends Fragment {

    private FragmentDashboardBinding binding;
    private FinanceViewModel viewModel;

    private double totalIncome = 0;
    private double totalExpenses = 0;

    private TransactionAdapter transactionAdapter;
    private BudgetAlertAdapter budgetAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(this).get(FinanceViewModel.class);

        binding.tvCurrentMonth.setText(DateUtils.getCurrentMonthName());

        android.content.SharedPreferences prefs = requireContext().getSharedPreferences("FinanceTrackPrefs", android.content.Context.MODE_PRIVATE);
        String username = prefs.getString("username", "Utilisateur");
        binding.tvGreeting.setText(getString(R.string.greeting_format, username));

        String profilePicUri = prefs.getString("profile_pic", null);
        if (profilePicUri != null) {
            try {
                binding.ivProfilePic.setImageURI(android.net.Uri.parse(profilePicUri));
            } catch (Exception e) {
                binding.ivProfilePic.setImageResource(R.drawable.ic_nav_home);
            }
        }

        setupTransactions();
        setupBudgetAlerts();

        // Cumulative totals for the small cards and the main balance
        viewModel.getTotalRevenus().observe(getViewLifecycleOwner(), total -> {
            totalIncome = total != null ? total : 0;
            binding.tvTotalIncome.setText(DateUtils.formatCurrency(totalIncome));
            updateBalance();
        });

        viewModel.getTotalDepenses().observe(getViewLifecycleOwner(), total -> {
            totalExpenses = total != null ? total : 0;
            binding.tvTotalExpenses.setText(DateUtils.formatCurrency(totalExpenses));
            updateBalance();
        });

        setupNavigation();
        observeChartData();
    }

    private void observeChartData() {
        viewModel.getAllDepenses().observe(getViewLifecycleOwner(), depenses -> {
            viewModel.getAllCategories().observe(getViewLifecycleOwner(), categories -> {
                if (depenses != null && categories != null) {
                    updateChart(depenses, categories);
                }
            });
        });
    }

    private void updateChart(List<Depense> depenses, List<Categorie> categories) {
        Map<Integer, Double> catTotals = new HashMap<>();
        Map<Integer, Categorie> catMap = new HashMap<>();
        for (Categorie c : categories) catMap.put(c.id, c);

        for (Depense d : depenses) {
            Double current = catTotals.get(d.categorie_id);
            if (current == null) current = 0.0;
            catTotals.put(d.categorie_id, current + d.montant);
        }

        List<CategoryDistributionView.CategoryData> chartData = new ArrayList<>();
        binding.llLegend.removeAllViews();

        for (Map.Entry<Integer, Double> entry : catTotals.entrySet()) {
            Categorie cat = catMap.get(entry.getKey());
            if (cat != null) {
                chartData.add(new CategoryDistributionView.CategoryData(cat.nom, entry.getValue(), cat.couleur));
                addLegendItem(cat, entry.getValue());
            }
        }
        binding.categoryChart.setData(chartData);
    }

    private void addLegendItem(Categorie cat, double amount) {
        View legendItem = getLayoutInflater().inflate(R.layout.item_legend, binding.llLegend, false);
        View colorIndicator = legendItem.findViewById(R.id.view_color);
        TextView tvName = legendItem.findViewById(R.id.tv_name);
        TextView tvAmount = legendItem.findViewById(R.id.tv_amount);

        try {
            colorIndicator.setBackgroundColor(android.graphics.Color.parseColor(cat.couleur));
        } catch (Exception e) {
            colorIndicator.setBackgroundColor(android.graphics.Color.GRAY);
        }
        tvName.setText(cat.nom);
        tvAmount.setText(DateUtils.formatCurrency(amount));

        binding.llLegend.addView(legendItem);
    }

    private void setupTransactions() {
        transactionAdapter = new TransactionAdapter();
        transactionAdapter.setLimit(5);
        transactionAdapter.setOnTransactionClickListener((id, type) -> {
            Bundle args = new Bundle();
            args.putInt("transactionId", id);
            args.putString("type", type);
            Navigation.findNavController(requireView()).navigate(R.id.navigation_transaction_detail, args);
        });
        binding.rvRecentTransactions.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rvRecentTransactions.setAdapter(transactionAdapter);

        viewModel.getAllCategories().observe(getViewLifecycleOwner(), categories -> {
            if (categories != null) {
                transactionAdapter.setCategories(categories);
            }
            
            viewModel.getAllDepenses().observe(getViewLifecycleOwner(), depenses -> {
                viewModel.getAllRevenus().observe(getViewLifecycleOwner(), revenus -> {
                    if ((depenses == null || depenses.isEmpty()) && (revenus == null || revenus.isEmpty())) {
                        binding.rvRecentTransactions.setVisibility(View.GONE);
                        binding.llEmptyState.setVisibility(View.VISIBLE);
                    } else {
                        binding.rvRecentTransactions.setVisibility(View.VISIBLE);
                        binding.llEmptyState.setVisibility(View.GONE);
                        transactionAdapter.setTransactions(depenses, revenus);
                    }
                });
            });
        });
    }

    private void setupBudgetAlerts() {
        budgetAdapter = new BudgetAlertAdapter();
        binding.rvBudgetAlerts.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rvBudgetAlerts.setAdapter(budgetAdapter);

        Calendar cal = Calendar.getInstance();
        int currentMois = cal.get(Calendar.MONTH) + 1;
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
                                double percentage = (spent / b.montant_plafond) * 100;
                                if (percentage >= 70) {
                                    items.add(new BudgetAlertAdapter.BudgetAlertItem(b, cat, spent));
                                }
                            }
                        }
                        budgetAdapter.setItems(items);
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
            if (cal.get(Calendar.MONTH) + 1 == budget.mois && cal.get(Calendar.YEAR) == budget.annee) {
                if (budget.categorie_id == null || d.categorie_id == budget.categorie_id) {
                    total += d.montant;
                }
            }
        }
        return total;
    }

    private void setupNavigation() {
        binding.btnSettings.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.navigation_settings));
        binding.btnQuickExpense.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.navigation_add_depense));
        binding.btnQuickIncome.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.navigation_add_revenu));
        binding.btnQuickBudgets.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.navigation_budgets));
        binding.btnQuickCategories.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.navigation_categories));
        binding.fabAdd.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.navigation_add_depense));
        binding.tvSeeAll.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.navigation_depenses));
    }

    private void updateBalance() {
        double balance = totalIncome - totalExpenses;
        binding.tvBalance.setText(DateUtils.formatCurrency(Math.abs(balance)));
        if (balance >= 0) {
            binding.tvBalance.setTextColor(ContextCompat.getColor(requireContext(), R.color.accent_blue));
        } else {
            binding.tvBalance.setTextColor(ContextCompat.getColor(requireContext(), R.color.expense));
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
