package sn.esmt.financetrack.data.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import sn.esmt.financetrack.data.dao.BudgetDao;
import sn.esmt.financetrack.data.dao.CategorieDao;
import sn.esmt.financetrack.data.dao.TransactionDao;
import sn.esmt.financetrack.data.entity.Budget;
import sn.esmt.financetrack.data.entity.Categorie;
import sn.esmt.financetrack.data.entity.Transaction;

@Database(entities = {Categorie.class, Budget.class, Transaction.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    public abstract CategorieDao categorieDao();
    public abstract BudgetDao budgetDao();
    public abstract TransactionDao transactionDao();

    private static volatile AppDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDatabase.class, "finance_track_db")
                            .addCallback(sRoomDatabaseCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static final RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            databaseWriteExecutor.execute(() -> {
                CategorieDao dao = INSTANCE.categorieDao();
                // Pré-remplissage des catégories par défaut (Étudiant 1/3)
                dao.insert(new Categorie("Alimentation", "restaurant", "#FF5722", true));
                dao.insert(new Categorie("Transport", "directions_car", "#2196F3", true));
                dao.insert(new Categorie("Loisirs", "sports_esports", "#9C27B0", true));
                dao.insert(new Categorie("Santé", "medical_services", "#E91E63", true));
                dao.insert(new Categorie("Loyer", "home", "#795548", true));
                dao.insert(new Categorie("Éducation", "school", "#4CAF50", true));
            });
        }
    };
}
