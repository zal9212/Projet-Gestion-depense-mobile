package com.tp.gestiondepenses.ui.adapters;

import android.content.Context;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.tp.gestiondepenses.R;
import com.tp.gestiondepenses.databinding.ItemTransactionBinding;
import com.tp.gestiondepenses.model.Categorie;
import com.tp.gestiondepenses.model.Depense;
import com.tp.gestiondepenses.model.Revenu;
import com.tp.gestiondepenses.utils.DateUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.ViewHolder> {

    private List<Object> transactions = new ArrayList<>();
    private Map<Integer, Categorie> categoryMap = new HashMap<>();
    private Context context;
    private int limit = -1;

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public void setCategories(List<Categorie> categories) {
        categoryMap.clear();
        if (categories != null) {
            for (Categorie c : categories) {
                categoryMap.put(c.id, c);
            }
        }
        notifyDataSetChanged();
    }

    public void setTransactions(List<Depense> depenses, List<Revenu> revenus) {
        transactions.clear();
        if (depenses != null) transactions.addAll(depenses);
        if (revenus != null) transactions.addAll(revenus);
        
        // Sort by date descending (Compatible with API 21+)
        Collections.sort(transactions, (a, b) -> {
            long dateA = (a instanceof Depense) ? ((Depense) a).date : ((Revenu) a).date;
            long dateB = (b instanceof Depense) ? ((Depense) b).date : ((Revenu) b).date;
            return Long.compare(dateB, dateA);
        });
        
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        ItemTransactionBinding binding = ItemTransactionBinding.inflate(LayoutInflater.from(context), parent, false);
        return new ViewHolder(binding);
    }

    public interface OnTransactionClickListener {
        void onTransactionClick(int id, String type);
    }

    private OnTransactionClickListener listener;

    public void setOnTransactionClickListener(OnTransactionClickListener listener) {
        this.listener = listener;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Object item = transactions.get(position);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());

        if (item instanceof Depense) {
            Depense d = (Depense) item;
            Categorie cat = categoryMap.get(d.categorie_id);
            
            // Titre : On utilise la catégorie comme titre principal si la description est "Dépense" ou vide
            String title;
            String subTitle = null;
            
            if (d.description == null || d.description.isEmpty() || d.description.equalsIgnoreCase("Dépense")) {
                title = (cat != null) ? cat.nom : "Dépense";
            } else {
                title = d.description;
                // Si on a une description personnalisée, on peut mettre la catégorie en sous-titre
                subTitle = (cat != null) ? cat.nom : null;
            }
            
            holder.binding.tvTitle.setText(title);
            
            // Gestion de la description secondaire
            if (subTitle != null) {
                holder.binding.tvDescription.setText(subTitle);
                holder.binding.tvDescription.setVisibility(View.VISIBLE);
            } else {
                holder.binding.tvDescription.setVisibility(View.GONE);
            }

            holder.binding.tvDate.setText(sdf.format(new Date(d.date)));
            holder.binding.tvAmount.setText("-" + DateUtils.formatCurrency(d.montant));
            holder.binding.tvAmount.setTextColor(ContextCompat.getColor(context, R.color.expense));
            
            holder.binding.flIconContainer.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.icon_expense_bg)));
            holder.binding.ivTypeIcon.setImageResource(android.R.drawable.ic_input_delete);
            holder.binding.ivTypeIcon.setImageTintList(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.expense)));

            holder.itemView.setOnClickListener(v -> {
                if (listener != null) listener.onTransactionClick(d.id, "depense");
            });
            
        } else if (item instanceof Revenu) {
            Revenu r = (Revenu) item;
            holder.binding.tvTitle.setText(r.source != null && !r.source.isEmpty() ? r.source : "Revenu");
            
            if (r.description != null && !r.description.isEmpty()) {
                holder.binding.tvDescription.setText(r.description);
                holder.binding.tvDescription.setVisibility(View.VISIBLE);
            } else {
                holder.binding.tvDescription.setVisibility(View.GONE);
            }

            holder.binding.tvDate.setText(sdf.format(new Date(r.date)));
            holder.binding.tvAmount.setText("+" + DateUtils.formatCurrency(r.montant));
            holder.binding.tvAmount.setTextColor(ContextCompat.getColor(context, R.color.income));
            
            holder.binding.flIconContainer.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.icon_income_bg)));
            holder.binding.ivTypeIcon.setImageResource(android.R.drawable.ic_input_add);
            holder.binding.ivTypeIcon.setImageTintList(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.income)));

            holder.itemView.setOnClickListener(v -> {
                if (listener != null) listener.onTransactionClick(r.id, "revenu");
            });
        }
    }

    @Override
    public int getItemCount() {
        if (limit > 0) {
            return Math.min(transactions.size(), limit);
        }
        return transactions.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ItemTransactionBinding binding;
        public ViewHolder(ItemTransactionBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
