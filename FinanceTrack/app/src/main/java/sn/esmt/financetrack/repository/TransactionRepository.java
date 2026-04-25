package sn.esmt.financetrack.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

import sn.esmt.financetrack.database.AppDatabase;
import sn.esmt.financetrack.database.TransactionDao;
import sn.esmt.financetrack.model.Transaction;

public class TransactionRepository {

    private TransactionDao transactionDao;
    private LiveData<List<Transaction>> allTransactions;

    public TransactionRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        transactionDao = db.transactionDao();
        allTransactions = transactionDao.getAllTransactions();
    }

    public LiveData<List<Transaction>> getAllTransactions() {
        return allTransactions;
    }

    public LiveData<List<Transaction>> getTransactionsByType(String type) {
        return transactionDao.getTransactionsByType(type);
    }

    public LiveData<Double> getTotalRevenu() {
        return transactionDao.getTotalRevenu();
    }

    public LiveData<Double> getTotalDepense() {
        return transactionDao.getTotalDepense();
    }

    public void insert(Transaction transaction) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            transactionDao.insert(transaction);
        });
    }

    public void update(Transaction transaction) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            transactionDao.update(transaction);
        });
    }

    public void delete(Transaction transaction) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            transactionDao.delete(transaction);
        });
    }
}
