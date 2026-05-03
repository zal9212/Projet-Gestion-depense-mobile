package com.tp.gestiondepenses.ui.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tp.gestiondepenses.databinding.ItemRubriqueManageBinding;
import com.tp.gestiondepenses.model.Rubrique;

import java.util.ArrayList;
import java.util.List;

public class RubriqueManageAdapter extends RecyclerView.Adapter<RubriqueManageAdapter.ViewHolder> {

    private List<Rubrique> rubriques = new ArrayList<>();
    private OnRubriqueDeleteListener listener;

    public interface OnRubriqueDeleteListener {
        void onDelete(Rubrique rubrique);
    }

    public void setOnRubriqueDeleteListener(OnRubriqueDeleteListener listener) {
        this.listener = listener;
    }

    public void setRubriques(List<Rubrique> rubriques) {
        this.rubriques = rubriques;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemRubriqueManageBinding binding = ItemRubriqueManageBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Rubrique r = rubriques.get(position);
        holder.binding.tvRubriqueName.setText(r.nom);
        holder.binding.btnDeleteRubrique.setOnClickListener(v -> {
            new androidx.appcompat.app.AlertDialog.Builder(v.getContext())
                .setTitle("Supprimer la rubrique")
                .setMessage("Voulez-vous supprimer '" + r.nom + "' ?")
                .setPositiveButton("Supprimer", (dialog, which) -> {
                    if (listener != null) listener.onDelete(r);
                })
                .setNegativeButton("Annuler", null)
                .show();
        });
    }

    @Override
    public int getItemCount() {
        return rubriques.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ItemRubriqueManageBinding binding;
        public ViewHolder(ItemRubriqueManageBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
