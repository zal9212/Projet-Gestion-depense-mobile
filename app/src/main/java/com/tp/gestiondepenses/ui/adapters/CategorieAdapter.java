package com.tp.gestiondepenses.ui.adapters;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tp.gestiondepenses.databinding.ItemCategorieExpandableBinding;
import com.tp.gestiondepenses.model.Categorie;
import com.tp.gestiondepenses.model.Rubrique;
import com.tp.gestiondepenses.viewmodel.FinanceViewModel;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CategorieAdapter extends RecyclerView.Adapter<CategorieAdapter.ViewHolder> {

    public List<Categorie> categories = new ArrayList<>();
    private FinanceViewModel viewModel;
    private LifecycleOwner lifecycleOwner;
    private Set<Integer> expandedIds = new HashSet<>();
    private OnCategorieActionListener listener;

    public interface OnCategorieActionListener {
        void onEdit(Categorie categorie);
        void onDelete(Categorie categorie);
    }

    public void setOnCategorieActionListener(OnCategorieActionListener listener) {
        this.listener = listener;
    }

    public void setCategories(List<Categorie> categories) {
        this.categories = categories;
        notifyDataSetChanged();
    }

    public void setViewModel(FinanceViewModel viewModel, LifecycleOwner lifecycleOwner) {
        this.viewModel = viewModel;
        this.lifecycleOwner = lifecycleOwner;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemCategorieExpandableBinding binding = ItemCategorieExpandableBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Categorie cat = categories.get(position);
        holder.binding.tvCategoryName.setText(cat.nom);
        
        try {
            holder.binding.vColorDot.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(cat.couleur)));
        } catch (Exception e) {
            holder.binding.vColorDot.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));
        }

        holder.binding.tvDefaultLabel.setVisibility(cat.est_defaut ? View.VISIBLE : View.GONE);

        boolean isExpanded = expandedIds.contains(cat.id);
        holder.binding.llExpandable.setVisibility(isExpanded ? View.VISIBLE : View.GONE);
        holder.binding.ivExpand.setRotation(isExpanded ? 90 : 270);

        holder.binding.llHeader.setOnClickListener(v -> {
            if (isExpanded) {
                expandedIds.remove(cat.id);
            } else {
                expandedIds.add(cat.id);
            }
            notifyItemChanged(position);
        });

        holder.binding.btnEditCategory.setOnClickListener(v -> {
            if (listener != null) listener.onEdit(cat);
        });

        holder.binding.btnDeleteCategory.setOnClickListener(v -> {
            if (listener != null) listener.onDelete(cat);
        });

        if (isExpanded && viewModel != null) {
            setupRubriques(holder, cat);
        }
    }

    private void setupRubriques(ViewHolder holder, Categorie cat) {
        RubriqueManageAdapter rubriqueAdapter = new RubriqueManageAdapter();
        holder.binding.rvRubriques.setLayoutManager(new LinearLayoutManager(holder.itemView.getContext()));
        holder.binding.rvRubriques.setAdapter(rubriqueAdapter);

        viewModel.getRubriquesByCategorie(cat.id).observe(lifecycleOwner, rubriques -> {
            if (rubriques != null) {
                rubriqueAdapter.setRubriques(rubriques);
            }
        });

        rubriqueAdapter.setOnRubriqueDeleteListener(r -> {
            new com.google.android.material.dialog.MaterialAlertDialogBuilder(holder.itemView.getContext())
                .setTitle("Supprimer la rubrique")
                .setMessage("Voulez-vous vraiment supprimer la rubrique \"" + r.nom + "\" ?")
                .setNegativeButton("Annuler", null)
                .setPositiveButton("Supprimer", (dialog, which) -> {
                    viewModel.deleteRubrique(r);
                })
                .show();
        });

        holder.binding.btnAddRubrique.setOnClickListener(v -> {
            String name = holder.binding.etNewRubrique.getText().toString().trim();
            if (!name.isEmpty()) {
                viewModel.insertRubrique(new Rubrique(cat.id, name));
                holder.binding.etNewRubrique.setText("");
            }
        });
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ItemCategorieExpandableBinding binding;
        public ViewHolder(ItemCategorieExpandableBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
