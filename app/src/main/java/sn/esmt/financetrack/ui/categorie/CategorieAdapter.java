package sn.esmt.financetrack.ui.categorie;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import sn.esmt.financetrack.R;
import sn.esmt.financetrack.data.entity.Categorie;

public class CategorieAdapter extends RecyclerView.Adapter<CategorieAdapter.CategorieViewHolder> {

    private List<Categorie> categories = new ArrayList<>();
    private final OnDeleteClickListener deleteClickListener;

    public interface OnDeleteClickListener {
        void onDeleteClick(Categorie categorie);
    }

    public CategorieAdapter(OnDeleteClickListener deleteClickListener) {
        this.deleteClickListener = deleteClickListener;
    }

    @NonNull
    @Override
    public CategorieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_categorie, parent, false);
        return new CategorieViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CategorieViewHolder holder, int position) {
        Categorie current = categories.get(position);
        holder.textNom.setText(current.getNom());
        
        try {
            holder.viewColor.setBackgroundColor(Color.parseColor(current.getCouleur()));
        } catch (Exception e) {
            holder.viewColor.setBackgroundColor(Color.GRAY);
        }

        // Student 3 : Les catégories par défaut ne peuvent pas être supprimées
        if (current.isDefault()) {
            holder.btnDelete.setVisibility(View.GONE);
        } else {
            holder.btnDelete.setVisibility(View.VISIBLE);
            holder.btnDelete.setOnClickListener(v -> deleteClickListener.onDeleteClick(current));
        }
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public void setCategories(List<Categorie> categories) {
        this.categories = categories;
        notifyDataSetChanged();
    }

    static class CategorieViewHolder extends RecyclerView.ViewHolder {
        private final TextView textNom;
        private final View viewColor;
        private final ImageButton btnDelete;

        public CategorieViewHolder(@NonNull View itemView) {
            super(itemView);
            textNom = itemView.findViewById(R.id.text_categorie_nom);
            viewColor = itemView.findViewById(R.id.view_color_indicator);
            btnDelete = itemView.findViewById(R.id.btn_delete);
        }
    }
}
