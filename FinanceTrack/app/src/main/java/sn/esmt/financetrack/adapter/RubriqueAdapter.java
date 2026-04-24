package sn.esmt.financetrack.adapter;

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
import sn.esmt.financetrack.model.Rubrique;

public class RubriqueAdapter extends RecyclerView.Adapter<RubriqueAdapter.RubriqueViewHolder> {

    // 1. L'interface pour écouter les clics (seulement la suppression)
    public interface OnRubriqueListener {
        void onDelete(Rubrique r);
    }

    // 2. Les attributs
    private List<Rubrique> rubriques = new ArrayList<>();
    private final OnRubriqueListener listener;

    // 3. Le Constructeur
    public RubriqueAdapter(OnRubriqueListener listener) {
        this.listener = listener;
    }

    // 4. La méthode pour mettre à jour la liste
    public void setRubriques(List<Rubrique> rubriques) {
        this.rubriques = rubriques;
        notifyDataSetChanged();
    }
    // 5. Le ViewHolder (la "boîte" qui garde les éléments visuels de la carte)
    public static class RubriqueViewHolder extends RecyclerView.ViewHolder {
        TextView tvNom;
        ImageButton btnDelete;

        public RubriqueViewHolder(@NonNull View itemView) {
            super(itemView);
            // On fait le lien avec le design XML 
            tvNom = itemView.findViewById(R.id.tvNomRubrique);
            btnDelete = itemView.findViewById(R.id.btnDeleteRubrique);
        }
    }

    // 6. Création d'une nouvelle carte vierge
    @NonNull
    @Override
    public RubriqueViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_rubrique, parent, false);
        return new RubriqueViewHolder(v);
    }

    // 7. Remplissage de la carte avec les vraies données
    @Override
    public void onBindViewHolder(@NonNull RubriqueViewHolder holder, int position) {
        Rubrique r = rubriques.get(position); // On prend la rubrique concernée

        // On affiche le nom
        holder.tvNom.setText(r.getNom());

        // On écoute le clic sur le bouton supprimer
        holder.btnDelete.setOnClickListener(v -> listener.onDelete(r));
    }

    // 8. Le nombre total de rubriques
    @Override
    public int getItemCount() {
        return rubriques.size();
    }

} 
