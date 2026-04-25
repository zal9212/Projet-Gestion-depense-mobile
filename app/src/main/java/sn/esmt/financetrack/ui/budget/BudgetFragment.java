package sn.esmt.financetrack.ui.budget;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import sn.esmt.financetrack.R;
import sn.esmt.financetrack.data.entity.Budget;
import sn.esmt.financetrack.data.entity.Categorie;
import sn.esmt.financetrack.ui.categorie.CategorieViewModel;
import sn.esmt.financetrack.databinding.FragmentBudgetBinding;

public class BudgetFragment extends Fragment {

    private FragmentBudgetBinding binding;
    private BudgetViewModel budgetViewModel;
    private CategorieViewModel categorieViewModel;
    private BudgetAdapter adapter;
    private List<Categorie> availableCategories = new ArrayList<>();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentBudgetBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        adapter = new BudgetAdapter(requireContext());
        binding.recyclerBudgets.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.recyclerBudgets.setAdapter(adapter);

        budgetViewModel = new ViewModelProvider(this).get(BudgetViewModel.class);
        categorieViewModel = new ViewModelProvider(this).get(CategorieViewModel.class);

        categorieViewModel.getAllCategories().observe(getViewLifecycleOwner(), categories -> {
            this.availableCategories = categories;
        });
        
        budgetViewModel.getAllBudgets().observe(getViewLifecycleOwner(), budgets -> {
            List<BudgetWithProgress> displayList = new ArrayList<>();
            for (Budget b : budgets) {
                String catName = "Inconnu";
                for (Categorie c : availableCategories) {
                    if (c.getId() == b.getCategorieId()) {
                        catName = c.getNom();
                        break;
                    }
                }
                double fakeExpense = Math.random() * b.getPlafond() * 1.1; 
                displayList.add(new BudgetWithProgress(b, fakeExpense, catName));
            }
            adapter.setBudgets(displayList);
        });

        binding.fabAddBudget.setOnClickListener(v -> showAddBudgetDialog());
    }

    private void showAddBudgetDialog() {
        View dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_add_budget, null);
        EditText editPlafond = dialogView.findViewById(R.id.edit_plafond);
        Spinner spinner = dialogView.findViewById(R.id.spinner_categories);

        ArrayAdapter<Categorie> adapterCat = new ArrayAdapter<>(requireContext(), 
                android.R.layout.simple_spinner_item, availableCategories);
        adapterCat.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapterCat);

        new AlertDialog.Builder(requireContext())
                .setView(dialogView)
                .setPositiveButton("Enregistrer", (dialog, which) -> {
                    String val = editPlafond.getText().toString();
                    Categorie selected = (Categorie) spinner.getSelectedItem();
                    if (!val.isEmpty() && selected != null) {
                        Calendar cal = Calendar.getInstance();
                        Budget budget = new Budget(selected.getId(), Double.parseDouble(val), 
                                cal.get(Calendar.MONTH) + 1, cal.get(Calendar.YEAR));
                        budgetViewModel.insert(budget);
                    }
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
