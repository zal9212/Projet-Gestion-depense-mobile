package sn.esmt.financetrack.data.dao;

import androidx.room.Dao;
import androidx.room.Query;

@Dao
public interface TransactionDao {
    @Query("SELECT SUM(montant) FROM transactions WHERE categorieId = :categorieId AND type = 'DEPENSE' AND strftime('%m', date) = :mois AND strftime('%Y', date) = :annee")
    double getSumDepensesByCategory(int categorieId, String mois, String annee);
}
