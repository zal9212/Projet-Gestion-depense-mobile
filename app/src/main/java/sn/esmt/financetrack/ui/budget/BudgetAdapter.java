package sn.esmt.financetrack.ui.budget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import sn.esmt.financetrack.R;

public class BudgetAdapter extends RecyclerView.Adapter<BudgetAdapter.BudgetViewHolder> {

    private List<BudgetWithProgress> budgets = new ArrayList<>();
    private final Context context;

    public BudgetAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public BudgetViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_budget, parent, false);
        return new BudgetViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull BudgetViewHolder holder, int position) {
        BudgetWithProgress current = budgets.get(position);
        holder.textCategoryName.setText(current.getCategoryName());
        
        String amountText = String.format("%.0f / %.0f FCFA", 
                current.getCurrentExpenses(), current.getBudget().getPlafond());
        holder.textBudgetAmount.setText(amountText);
        
        int percentage = current.getPercentage();
        holder.progressBudget.setProgress(percentage);
        holder.textPercentage.setText(percentage + "%");

        // UI Dynamique : Logique de couleur (Vert/Orange/Rouge)
        int color;
        if (percentage < 70) {
            color = Color.parseColor("#10B981"); // Vert
        } else if (percentage < 90) {
            color = Color.parseColor("#F59E0B"); // Orange
        } else {
            color = Color.parseColor("#EF4444"); // Rouge
        }
        holder.progressBudget.setProgressTintList(ColorStateList.valueOf(color));
    }

    @Override
    public int getItemCount() {
        return budgets.size();
    }

    public void setBudgets(List<BudgetWithProgress> budgets) {
        this.budgets = budgets;
        notifyDataSetChanged();
    }

    static class BudgetViewHolder extends RecyclerView.ViewHolder {
        private final TextView textCategoryName;
        private final TextView textBudgetAmount;
        private final ProgressBar progressBudget;
        private final TextView textPercentage;

        public BudgetViewHolder(@NonNull View itemView) {
            super(itemView);
            textCategoryName = itemView.findViewById(R.id.text_category_name);
            textBudgetAmount = itemView.findViewById(R.id.text_budget_amount);
            progressBudget = itemView.findViewById(R.id.progress_budget);
            textPercentage = itemView.findViewById(R.id.text_percentage);
        }
    }
}
