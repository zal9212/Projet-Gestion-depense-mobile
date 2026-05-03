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

import com.tp.gestiondepenses.R;
import com.tp.gestiondepenses.databinding.FragmentRevenusBinding;
import com.tp.gestiondepenses.model.Revenu;
import com.tp.gestiondepenses.ui.adapters.TransactionAdapter;
import com.tp.gestiondepenses.utils.DateUtils;
import com.tp.gestiondepenses.viewmodel.FinanceViewModel;

import java.util.ArrayList;
import java.util.List;

public class RevenusFragment extends Fragment {

    private FragmentRevenusBinding binding;
    private FinanceViewModel viewModel;
    private TransactionAdapter adapter;
    private List<Revenu> allRevenus = new ArrayList<>();
    private String searchQuery = "";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentRevenusBinding.inflate(inflater, container, false);
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
            args.putString("type", "revenu");
            androidx.navigation.Navigation.findNavController(requireView()).navigate(R.id.navigation_transaction_detail, args);
        });
        binding.rvRevenus.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rvRevenus.setAdapter(adapter);

        viewModel.getAllRevenus().observe(getViewLifecycleOwner(), revenus -> {
            allRevenus = revenus;
            applyFilters();
        });

        setupSearch();

        binding.fabAdd.setOnClickListener(v -> {
            androidx.navigation.Navigation.findNavController(v).navigate(R.id.navigation_add_revenu);
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

    private void applyFilters() {
        List<Revenu> filtered = new ArrayList<>();
        for (Revenu r : allRevenus) {
            if (!searchQuery.isEmpty() && r.description != null && !r.description.toLowerCase().contains(searchQuery) && !r.source.toLowerCase().contains(searchQuery)) {
                continue;
            }
            filtered.add(r);
        }

        adapter.setTransactions(new ArrayList<>(), filtered);
        
        double total = 0;
        for (Revenu r : filtered) total += r.montant;
        binding.tvSummaryAmount.setText(DateUtils.formatCurrency(total));
        binding.tvSummaryLabel.setText(getString(R.string.label_total_count, filtered.size()));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
