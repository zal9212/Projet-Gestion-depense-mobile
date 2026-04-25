package sn.esmt.financetrack;

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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import sn.esmt.financetrack.databinding.FragmentAddRevenusBinding;
import sn.esmt.financetrack.model.Rubrique;
import sn.esmt.financetrack.model.Transaction;
import sn.esmt.financetrack.viewmodel.RubriqueViewModel;
import sn.esmt.financetrack.viewmodel.TransactionViewModel;

public class AddRevenus extends Fragment {

    private FragmentAddRevenusBinding binding;
    private TransactionViewModel transactionViewModel;
    private RubriqueViewModel rubriqueViewModel;
    private List<Rubrique> rubriquesList = new ArrayList<>();
    private final Calendar calendar = Calendar.getInstance();

    public AddRevenus() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAddRevenusBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        transactionViewModel = new ViewModelProvider(this).get(TransactionViewModel.class);
        rubriqueViewModel = new ViewModelProvider(this).get(RubriqueViewModel.class);

        setupToolbar();
        setupDatePicker();
        setupSpinner();
        setupSaveButton();
    }

    private void setupToolbar() {
        binding.btnBack.setOnClickListener(v -> Navigation.findNavController(v).navigateUp());
    }

    private void setupDatePicker() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.FRANCE);
        binding.etDate.setText(sdf.format(calendar.getTime()));

        binding.etDate.setOnClickListener(v -> {
            new DatePickerDialog(requireContext(), (view, year, month, dayOfMonth) -> {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                binding.etDate.setText(sdf.format(calendar.getTime()));
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
        });
    }

    private void setupSpinner() {
        rubriqueViewModel.getAllRubriques().observe(getViewLifecycleOwner(), rubriques -> {
            this.rubriquesList = rubriques;
            List<String> names = new ArrayList<>();
            for (Rubrique r : rubriques) {
                names.add(r.getNom());
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, names);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            binding.spinnerSource.setAdapter(adapter);
        });
    }

    private void setupSaveButton() {
        binding.btnSave.setOnClickListener(v -> {
            String montantStr = binding.etMontant.getText().toString().trim();
            String description = binding.etDescription.getText().toString().trim();
            int selectedPos = binding.spinnerSource.getSelectedItemPosition();

            if (montantStr.isEmpty() || selectedPos == -1) {
                Toast.makeText(getContext(), "Veuillez remplir le montant et sélectionner une source", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                double montant = Double.parseDouble(montantStr.replace(" ", ""));
                int rubriqueId = rubriquesList.get(selectedPos).getId();

                Transaction transaction = new Transaction(montant, description, calendar.getTimeInMillis(), "REVENU", rubriqueId);
                transactionViewModel.insert(transaction);

                Toast.makeText(getContext(), "Revenu enregistré avec succès", Toast.LENGTH_SHORT).show();
                Navigation.findNavController(v).navigateUp();
            } catch (NumberFormatException e) {
                Toast.makeText(getContext(), "Montant invalide", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}