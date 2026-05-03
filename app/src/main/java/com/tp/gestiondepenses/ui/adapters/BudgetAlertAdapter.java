package com.tp.gestiondepenses.ui.adapters;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.tp.gestiondepenses.R;
import com.tp.gestiondepenses.databinding.ItemBudgetBinding;
import com.tp.gestiondepenses.model.Budget;
import com.tp.gestiondepenses.model.Categorie;
import com.tp.gestiondepenses.utils.DateUtils;

import java.util.ArrayList;
import java.util.List;

public class BudgetAlertAdapter extends RecyclerView.Adapter<BudgetAlertAdapter.ViewHolder> {

    public static class BudgetAlertItem {
        public Budget budget;
        public Categorie categorie;
        public double spent;
        public int percentage;

        public BudgetAlertItem(Budget budget, Categorie categorie, double spent) {
            this.budget = budget;
            this.categorie = categorie;
            this.spent = spent;
            this.percentage = (int) ((spent / budget.montant_plafond) * 100);
        }
    }

    public interface OnBudgetDeleteListener {
        void onDelete(Budget budget);
    }

    public interface OnBudgetEditListener {
        void onEdit(Budget budget);
    }

    private OnBudgetDeleteListener deleteListener;
    private OnBudgetEditListener editListener;
    private List<BudgetAlertItem> items = new ArrayList<>();

    public void setOnBudgetDeleteListener(OnBudgetDeleteListener listener) {
        this.deleteListener = listener;
    }

    public void setOnBudgetEditListener(OnBudgetEditListener listener) {
        this.editListener = listener;
    }

    public void setItems(List<BudgetAlertItem> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemBudgetBinding binding = ItemBudgetBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        BudgetAlertItem item = items.get(position);
        holder.binding.tvBudgetLabel.setText(item.categorie != null ? item.categorie.nom : "Budget Global");
        holder.binding.tvBudgetAmount.setText(DateUtils.formatCurrency(item.budget.montant_plafond));
        holder.binding.tvBudgetPeriode.setText(item.budget.periode != null ? item.budget.periode : "MENSUEL");
        holder.binding.progressBar.setProgress(Math.min(item.percentage, 100));

        int color;
        if (item.percentage < 80) {
            color = ContextCompat.getColor(holder.itemView.getContext(), R.color.income);
        } else if (item.percentage < 100) {
            color = ContextCompat.getColor(holder.itemView.getContext(), R.color.warning);
        } else {
            color = ContextCompat.getColor(holder.itemView.getContext(), R.color.expense);
        }
        
        holder.binding.progressBar.setProgressTintList(ColorStateList.valueOf(color));
        
        holder.binding.tvBudgetStatus.setText("Consommé: " + DateUtils.formatCurrency(item.spent));
        holder.binding.tvBudgetRemaining.setText("Restant: " + DateUtils.formatCurrency(Math.max(0, item.budget.montant_plafond - item.spent)));
        
        if (item.percentage >= 100) {
            holder.binding.tvAlertMessage.setVisibility(View.VISIBLE);
            holder.binding.tvAlertMessage.setText("Dépassement de budget !");
        } else {
            holder.binding.tvAlertMessage.setVisibility(View.GONE);
        }

        // Handle delete
        holder.binding.btnDelete.setOnClickListener(v -> {
            if (deleteListener != null) {
                deleteListener.onDelete(item.budget);
            }
        });

        // Handle edit
        holder.binding.btnEdit.setOnClickListener(v -> {
            if (editListener != null) {
                editListener.onEdit(item.budget);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ItemBudgetBinding binding;
        public ViewHolder(ItemBudgetBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
