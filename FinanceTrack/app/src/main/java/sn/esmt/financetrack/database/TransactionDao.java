package sn.esmt.financetrack.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import sn.esmt.financetrack.model.Transaction;

@Dao
public interface TransactionDao {

    @Insert
    void insert(Transaction transaction);

    @Update
    void update(Transaction transaction);

    @Delete
    void delete(Transaction transaction);

    @Query("SELECT * FROM transactions ORDER BY date DESC")
    LiveData<List<Transaction>> getAllTransactions();

    @Query("SELECT * FROM transactions WHERE type = :type ORDER BY date DESC")
    LiveData<List<Transaction>> getTransactionsByType(String type);

    @Query("SELECT SUM(montant) FROM transactions WHERE type = 'REVENU'")
    LiveData<Double> getTotalRevenu();

    @Query("SELECT SUM(montant) FROM transactions WHERE type = 'DEPENSE'")
    LiveData<Double> getTotalDepense();
}
