package com.tp.gestiondepenses.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.tp.gestiondepenses.model.Budget;
import com.tp.gestiondepenses.model.Categorie;
import com.tp.gestiondepenses.model.Depense;
import com.tp.gestiondepenses.model.Revenu;
import com.tp.gestiondepenses.model.Rubrique;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Categorie.class, Rubrique.class, Depense.class, Revenu.class, Budget.class}, version = 2, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    public abstract CategorieDao categorieDao();
    public abstract DepenseDao depenseDao();
    public abstract RevenuDao revenuDao();
    public abstract BudgetDao budgetDao();
    public abstract RubriqueDao rubriqueDao();

    private static volatile AppDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDatabase.class, "finance_db")
                            .addCallback(sRoomDatabaseCallback)
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            databaseWriteExecutor.execute(() -> {
                CategorieDao catDao = INSTANCE.categorieDao();
                RubriqueDao rubDao = INSTANCE.rubriqueDao();
                
                // 1. Alimentation
                long cat1 = catDao.insertCategorieWithId(new Categorie("Alimentation", "fast-food", "#546E7A", true));
                rubDao.insertRubrique(new Rubrique((int)cat1, "Restaurant"));
                rubDao.insertRubrique(new Rubrique((int)cat1, "Marche"));
                rubDao.insertRubrique(new Rubrique((int)cat1, "Épicerie"));
                rubDao.insertRubrique(new Rubrique((int)cat1, "Fast-food"));

                // 2. Transport
                long cat2 = catDao.insertCategorieWithId(new Categorie("Transport", "taxi", "#5C6BC0", true));
                rubDao.insertRubrique(new Rubrique((int)cat2, "Taxi"));
                rubDao.insertRubrique(new Rubrique((int)cat2, "Bus"));
                rubDao.insertRubrique(new Rubrique((int)cat2, "Carburant"));
                rubDao.insertRubrique(new Rubrique((int)cat2, "Parking"));

                // 3. Logement
                long cat3 = catDao.insertCategorieWithId(new Categorie("Logement", "home", "#00897B", true));
                rubDao.insertRubrique(new Rubrique((int)cat3, "Loyer"));
                rubDao.insertRubrique(new Rubrique((int)cat3, "Electricité"));
                rubDao.insertRubrique(new Rubrique((int)cat3, "Eau"));
                rubDao.insertRubrique(new Rubrique((int)cat3, "Internet"));

                // 4. Santé
                long cat4 = catDao.insertCategorieWithId(new Categorie("Santé", "local-hospital", "#E53935", true));
                rubDao.insertRubrique(new Rubrique((int)cat4, "Pharmacie"));
                rubDao.insertRubrique(new Rubrique((int)cat4, "Consultation"));
                rubDao.insertRubrique(new Rubrique((int)cat4, "Hôpital"));

                // 5. Éducation
                long cat5 = catDao.insertCategorieWithId(new Categorie("Éducation", "school", "#8E24AA", true));
                rubDao.insertRubrique(new Rubrique((int)cat5, "Frais scolaires"));
                rubDao.insertRubrique(new Rubrique((int)cat5, "Fournitures"));
                rubDao.insertRubrique(new Rubrique((int)cat5, "Livres"));

                // 6. Loisirs
                long cat6 = catDao.insertCategorieWithId(new Categorie("Loisirs", "sports-esports", "#F4511E", true));
                rubDao.insertRubrique(new Rubrique((int)cat6, "Divertissement"));
                rubDao.insertRubrique(new Rubrique((int)cat6, "Sport"));
                rubDao.insertRubrique(new Rubrique((int)cat6, "Voyage"));

                // 7. Habillement
                long cat7 = catDao.insertCategorieWithId(new Categorie("Habillement", "checkroom", "#6D4C41", true));
                rubDao.insertRubrique(new Rubrique((int)cat7, "Vêtements"));
                rubDao.insertRubrique(new Rubrique((int)cat7, "Chaussures"));
                rubDao.insertRubrique(new Rubrique((int)cat7, "Accessoires"));

                // 8. Autre
                catDao.insertCategorieWithId(new Categorie("Autre", "more-horiz", "#455A64", true));
            });
        }
    };
}
