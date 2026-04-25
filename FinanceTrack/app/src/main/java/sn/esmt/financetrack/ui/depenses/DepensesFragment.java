package sn.esmt.financetrack.ui.depenses;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.ArrayList;
import java.util.List;

import sn.esmt.financetrack.adapter.TransactionAdapter;
import sn.esmt.financetrack.databinding.DialogAddTransactionBinding;
import sn.esmt.financetrack.databinding.FragmentDepensesBinding;
import sn.esmt.financetrack.model.Rubrique;
import sn.esmt.financetrack.model.Transaction;
import sn.esmt.financetrack.viewmodel.RubriqueViewModel;
import sn.esmt.financetrack.viewmodel.TransactionViewModel;

public class DepensesFragment extends Fragment {

    private FragmentDepensesBinding binding;
    private TransactionViewModel transactionViewModel;
    private RubriqueViewModel rubriqueViewModel;
    private TransactionAdapter adapter;
    private List<Rubrique> rubriquesList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentDepensesBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        adapter = new TransactionAdapter();
        binding.rvDepenses.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rvDepenses.setAdapter(adapter);

        transactionViewModel = new ViewModelProvider(this).get(TransactionViewModel.class);
        rubriqueViewModel = new ViewModelProvider(this).get(RubriqueViewModel.class);

        transactionViewModel.getTransactionsByType("DEPENSE").observe(getViewLifecycleOwner(), transactions -> {
            adapter.setTransactions(transactions);
            double total = 0;
            for (Transaction t : transactions) total += t.getMontant();
            binding.tvTotalDepenses.setText(String.format("%.2f CFA", total));
        });

        rubriqueViewModel.getAllRubriques().observe(getViewLifecycleOwner(), rubriques -> {
            this.rubriquesList = rubriques;
        });

        binding.fabAddDepense.setOnClickListener(v -> showAddDepenseDialog());
    }

    private void showAddDepenseDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        DialogAddTransactionBinding dialogBinding = DialogAddTransactionBinding.inflate(getLayoutInflater());
        builder.setView(dialogBinding.getRoot());
        
        dialogBinding.tvDialogTitle.setText("Nouvelle Dépense");

        List<String> rubriqueNoms = new ArrayList<>();
        for (Rubrique r : rubriquesList) rubriqueNoms.add(r.getNom());
        
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, rubriqueNoms);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dialogBinding.spinnerRubriques.setAdapter(spinnerAdapter);

        AlertDialog dialog = builder.create();

        dialogBinding.btnSave.setOnClickListener(v -> {
            String montantStr = dialogBinding.etMontant.getText().toString();
            String description = dialogBinding.etDescription.getText().toString();
            int selectedPos = dialogBinding.spinnerRubriques.getSelectedItemPosition();

            if (montantStr.isEmpty() || description.isEmpty() || selectedPos == -1) {
                Toast.makeText(getContext(), "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
                return;
            }

            double montant = Double.parseDouble(montantStr);
            int rubriqueId = rubriquesList.get(selectedPos).getId();

            Transaction transaction = new Transaction(montant, description, System.currentTimeMillis(), "DEPENSE", rubriqueId);
            transactionViewModel.insert(transaction);
            dialog.dismiss();
        });

        dialog.show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
