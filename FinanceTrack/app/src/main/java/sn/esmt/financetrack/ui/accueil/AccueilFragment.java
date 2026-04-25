package sn.esmt.financetrack.ui.accueil;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import sn.esmt.financetrack.databinding.FragmentAccueilBinding;
import sn.esmt.financetrack.viewmodel.TransactionViewModel;

public class AccueilFragment extends Fragment {

    private FragmentAccueilBinding binding;
    private TransactionViewModel transactionViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentAccueilBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        transactionViewModel = new ViewModelProvider(this).get(TransactionViewModel.class);

        transactionViewModel.getTotalRevenu().observe(getViewLifecycleOwner(), totalRevenu -> {
            double rev = totalRevenu != null ? totalRevenu : 0.0;
            binding.tvRevenusTotal.setText(String.format("%.2f CFA", rev));
            updateSolde();
        });

        transactionViewModel.getTotalDepense().observe(getViewLifecycleOwner(), totalDepense -> {
            double dep = totalDepense != null ? totalDepense : 0.0;
            binding.tvDepensesTotal.setText(String.format("%.2f CFA", dep));
            updateSolde();
        });
    }

    private void updateSolde() {
        if (binding == null) return;
        
        String revStr = binding.tvRevenusTotal.getText().toString().replace(" CFA", "").replace(",", ".").trim();
        String depStr = binding.tvDepensesTotal.getText().toString().replace(" CFA", "").replace(",", ".").trim();
        
        try {
            double rev = revStr.isEmpty() ? 0.0 : Double.parseDouble(revStr);
            double dep = depStr.isEmpty() ? 0.0 : Double.parseDouble(depStr);
            double solde = rev - dep;
            binding.tvSoldeTotal.setText(String.format("%.2f CFA", solde));
        } catch (NumberFormatException e) {
            binding.tvSoldeTotal.setText("0.00 CFA");
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
