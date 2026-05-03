package com.tp.gestiondepenses.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.tp.gestiondepenses.R;
import com.tp.gestiondepenses.databinding.FragmentTransactionDetailBinding;
import com.tp.gestiondepenses.model.Depense;
import com.tp.gestiondepenses.model.Revenu;
import com.tp.gestiondepenses.utils.DateUtils;
import com.tp.gestiondepenses.viewmodel.FinanceViewModel;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class TransactionDetailFragment extends Fragment {

    private FragmentTransactionDetailBinding binding;
    private FinanceViewModel viewModel;
    private int transactionId;
    private String type;

    private Depense currentDepense;
    private Revenu currentRevenu;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentTransactionDetailBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(this).get(FinanceViewModel.class);

        if (getArguments() != null) {
            transactionId = getArguments().getInt("transactionId");
            type = getArguments().getString("type");
        }

        loadData();

        binding.btnDelete.setOnClickListener(v -> showDeleteConfirmation());
        binding.btnEdit.setOnClickListener(v -> {
            Bundle args = new Bundle();
            args.putInt("transactionId", transactionId);
            int destinationId = "depense".equals(type) ? R.id.navigation_add_depense : R.id.navigation_add_revenu;
            Navigation.findNavController(v).navigate(destinationId, args);
        });
    }

    private void loadData() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());

        binding.btnBack.setOnClickListener(v -> Navigation.findNavController(requireView()).navigateUp());

        if ("depense".equals(type)) {
            viewModel.getDepenseById(transactionId).observe(getViewLifecycleOwner(), d -> {
                if (d != null) {
                    currentDepense = d;
                    binding.tvAmount.setText("-" + DateUtils.formatCurrency(d.montant));
                    binding.tvAmount.setTextColor(ContextCompat.getColor(requireContext(), R.color.expense));
                    binding.tvDate.setText(sdf.format(new Date(d.date)));
                    binding.tvDescription.setText(d.description != null ? d.description : "Pas de description");
                    binding.tvMoyen.setText(d.moyen_paiement != null ? d.moyen_paiement : "Non spécifié");
                    
                    viewModel.getAllCategories().observe(getViewLifecycleOwner(), categories -> {
                        for (com.tp.gestiondepenses.model.Categorie c : categories) {
                            if (c.id == d.categorie_id) {
                                binding.tvCategory.setText(c.nom);
                                break;
                            }
                        }
                    });
                }
            });
        } else {
            viewModel.getRevenuById(transactionId).observe(getViewLifecycleOwner(), r -> {
                if (r != null) {
                    currentRevenu = r;
                    binding.tvAmount.setText("+" + DateUtils.formatCurrency(r.montant));
                    binding.tvAmount.setTextColor(ContextCompat.getColor(requireContext(), R.color.income));
                    binding.tvDate.setText(sdf.format(new Date(r.date)));
                    binding.tvDescription.setText(r.description != null && !r.description.isEmpty() ? r.description : getString(R.string.none));
                    binding.tvMoyen.setText(getString(R.string.source_label) + ": " + r.source);
                    binding.tvCategory.setText(getString(R.string.income_label));
                }
            });
        }
    }

    private void showDeleteConfirmation() {
        new AlertDialog.Builder(requireContext())
                .setTitle("Confirmer la suppression")
                .setMessage("Voulez-vous vraiment supprimer cette transaction ?")
                .setPositiveButton("Supprimer", (dialog, which) -> {
                    if ("depense".equals(type) && currentDepense != null) {
                        viewModel.deleteDepense(currentDepense);
                    } else if (currentRevenu != null) {
                        viewModel.deleteRevenu(currentRevenu);
                    }
                    Toast.makeText(getContext(), "Transaction supprimée", Toast.LENGTH_SHORT).show();
                    Navigation.findNavController(requireView()).navigateUp();
                })
                .setNegativeButton("Annuler", null)
                .show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
