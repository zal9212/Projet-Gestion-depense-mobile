package sn.esmt.financetrack.adapter;
import android.graphics.Color; // converti une couleur exadecimal en couleur android

import android.view.LayoutInflater; // permet d'afficher une interface
import android.view.View; // permet d'afficher une vue
import android.view.ViewGroup; // permet d'afficher un groupe de vues
import android.widget.ImageButton; // permet d'afficher un bouton avec une icone
import android.widget.TextView; // permet d'afficher du texte

import androidx.annotation.NonNull; // permet d'indiquer qu'une variable ne peut pas être nulle
import androidx.recyclerview.widget.RecyclerView; // la classe de base pour afficher une liste
import java.util.ArrayList; //permet de cree une liste
import java.util.List; // permet de definir une liste
import sn.esmt.financetrack.R; // permet d'afficher une ressource ( layout) R contient tous les ressources layouts
import sn.esmt.financetrack.model.Categorie; // permet d'importer la classe Categorie

public class CategorieAdapter extends RecyclerView.Adapter<CategorieAdapter.CategorieViewHolder> {
  
   public interface OnCategorieListener {
    void onEdit(Categorie c); //appelee quand on clique sur "Modifier"
    void onDelete(Categorie c); //appelee quand on clique sur "Suprimer"
    void onClick(Categorie c); //cette fonction est appeler quand on cliqye sur la liste entiere
   }
   // la liste des categories a afficher
private List<Categorie> categories = new ArrayList<>();
 // Le listener (le Fragment) qi reagira aux clics

 private final OnCategorieListener listener;
 // Constructeur : le fragment passe son listener ici 
 public CategorieAdapter(OnCategorieListener listener){
   this.listener = listener;
  }

 public void setCategories(List<Categorie> categories){
   this.categories = categories;
   notifyDataSetChanged(); //ici il indique au recycleView qu'il doit se redessiner

 } 
// Étape 6 : La classe interne ViewHolder
 public static class CategorieViewHolder extends RecyclerView.ViewHolder {
   TextView tvIcone, tvNom, tvRubriques;
   ImageButton btnEdit,btnDelete;

   public CategorieViewHolder(View v) {
      super(v);
      //ici on fait le lien entre nos variables java et les IDs dans notre fichier XML(item_categorie.xml)
      tvIcone = v.findViewById(R.id.tvIcone);
      tvNom = v.findViewById(R.id.tvNomCategorie);
      tvRubriques = v.findViewById(R.id.tvNbRubriques);
      btnEdit = v.findViewById(R.id.btnEditCategorie);
      btnDelete = v.findViewById(R.id.btnDeleteCategorie);

   }
 }

 // Étape 7 : Création de la carte visuelle vierge

 @NonNull
 @Override

 public CategorieViewHolder onCreateViewHolder (@NonNull ViewGroup parent, int viewType) {
//on gonfle le fichier xml item_categorie.xml pour le transformer en objet java view
View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_categorie, parent, false);

//on donne cette vue a notre viewHolder
return new  CategorieViewHolder(v);

 }

 //etape 8 le nombre total d'elements
 @Override
 public int getItemCount(){
   return categories.size(); // on retourne simplement la taille de notre liste 
 }

     // Étape 9 : Remplissage de la carte avec les vraies données
    @Override
    public void onBindViewHolder(@NonNull CategorieViewHolder holder, int position) {
        // 1. On récupère la catégorie correspondant à la position actuelle (ex: ligne 0, ligne 1...)
        Categorie c = categories.get(position);

        // 2. On affiche l'icône (émoji) et le nom
        holder.tvIcone.setText(c.getIcone());
        holder.tvNom.setText(c.getNom());

        // 3. On applique la couleur de fond pour l'icône (le cercle coloré)
        try {
            holder.tvIcone.getBackground().setTint(Color.parseColor(c.getCouleur()));
        } catch (Exception e) {
            holder.tvIcone.getBackground().setTint(Color.GRAY); // Couleur grise de sécurité si erreur
        }

        // 4. On écoute les clics et on avertit notre Fragment (le fameux 'listener')
        holder.itemView.setOnClickListener(v -> listener.onClick(c)); // Clic sur n'importe où sur la carte
        holder.btnEdit.setOnClickListener(v -> listener.onEdit(c));   // Clic sur l'icône crayon

        // 5. Règle métier : On ne peut pas supprimer les catégories par défaut de l'application
        if (c.isEstDefaut()) {
            holder.btnDelete.setVisibility(View.GONE); // On fait disparaître le bouton poubelle
        } else {
            holder.btnDelete.setVisibility(View.VISIBLE); // On l'affiche
            holder.btnDelete.setOnClickListener(v -> listener.onDelete(c)); // Clic sur la poubelle
        }
    }
} 
