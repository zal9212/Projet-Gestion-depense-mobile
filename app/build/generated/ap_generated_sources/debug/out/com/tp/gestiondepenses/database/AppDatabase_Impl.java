package com.tp.gestiondepenses.database;

import androidx.annotation.NonNull;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomDatabase;
import androidx.room.RoomOpenHelper;
import androidx.room.migration.AutoMigrationSpec;
import androidx.room.migration.Migration;
import androidx.room.util.DBUtil;
import androidx.room.util.TableInfo;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@SuppressWarnings({"unchecked", "deprecation"})
public final class AppDatabase_Impl extends AppDatabase {
  private volatile CategorieDao _categorieDao;

  private volatile DepenseDao _depenseDao;

  private volatile RevenuDao _revenuDao;

  private volatile BudgetDao _budgetDao;

  private volatile RubriqueDao _rubriqueDao;

  @Override
  @NonNull
  protected SupportSQLiteOpenHelper createOpenHelper(@NonNull final DatabaseConfiguration config) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(config, new RoomOpenHelper.Delegate(2) {
      @Override
      public void createAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS `categories` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `nom` TEXT, `icone` TEXT, `couleur` TEXT, `est_defaut` INTEGER NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `rubriques` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `categorie_id` INTEGER NOT NULL, `nom` TEXT, FOREIGN KEY(`categorie_id`) REFERENCES `categories`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE )");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_rubriques_categorie_id` ON `rubriques` (`categorie_id`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `depenses` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `categorie_id` INTEGER NOT NULL, `rubrique_id` INTEGER, `montant` REAL NOT NULL, `date` INTEGER NOT NULL, `description` TEXT, `moyen_paiement` TEXT, `created_at` INTEGER NOT NULL, FOREIGN KEY(`categorie_id`) REFERENCES `categories`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE , FOREIGN KEY(`rubrique_id`) REFERENCES `rubriques`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE )");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_depenses_categorie_id` ON `depenses` (`categorie_id`)");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_depenses_rubrique_id` ON `depenses` (`rubrique_id`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `revenus` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `source` TEXT, `montant` REAL NOT NULL, `date` INTEGER NOT NULL, `description` TEXT, `created_at` INTEGER NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `budgets` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `categorie_id` INTEGER, `montant_plafond` REAL NOT NULL, `periode` TEXT, `mois` INTEGER NOT NULL, `annee` INTEGER NOT NULL, FOREIGN KEY(`categorie_id`) REFERENCES `categories`(`id`) ON UPDATE NO ACTION ON DELETE NO ACTION )");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_budgets_categorie_id` ON `budgets` (`categorie_id`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, 'e706a74cd06bf4624a215fe4a7bd6ee8')");
      }

      @Override
      public void dropAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS `categories`");
        db.execSQL("DROP TABLE IF EXISTS `rubriques`");
        db.execSQL("DROP TABLE IF EXISTS `depenses`");
        db.execSQL("DROP TABLE IF EXISTS `revenus`");
        db.execSQL("DROP TABLE IF EXISTS `budgets`");
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onDestructiveMigration(db);
          }
        }
      }

      @Override
      public void onCreate(@NonNull final SupportSQLiteDatabase db) {
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onCreate(db);
          }
        }
      }

      @Override
      public void onOpen(@NonNull final SupportSQLiteDatabase db) {
        mDatabase = db;
        db.execSQL("PRAGMA foreign_keys = ON");
        internalInitInvalidationTracker(db);
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onOpen(db);
          }
        }
      }

      @Override
      public void onPreMigrate(@NonNull final SupportSQLiteDatabase db) {
        DBUtil.dropFtsSyncTriggers(db);
      }

      @Override
      public void onPostMigrate(@NonNull final SupportSQLiteDatabase db) {
      }

      @Override
      @NonNull
      public RoomOpenHelper.ValidationResult onValidateSchema(
          @NonNull final SupportSQLiteDatabase db) {
        final HashMap<String, TableInfo.Column> _columnsCategories = new HashMap<String, TableInfo.Column>(5);
        _columnsCategories.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCategories.put("nom", new TableInfo.Column("nom", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCategories.put("icone", new TableInfo.Column("icone", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCategories.put("couleur", new TableInfo.Column("couleur", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCategories.put("est_defaut", new TableInfo.Column("est_defaut", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysCategories = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesCategories = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoCategories = new TableInfo("categories", _columnsCategories, _foreignKeysCategories, _indicesCategories);
        final TableInfo _existingCategories = TableInfo.read(db, "categories");
        if (!_infoCategories.equals(_existingCategories)) {
          return new RoomOpenHelper.ValidationResult(false, "categories(com.tp.gestiondepenses.model.Categorie).\n"
                  + " Expected:\n" + _infoCategories + "\n"
                  + " Found:\n" + _existingCategories);
        }
        final HashMap<String, TableInfo.Column> _columnsRubriques = new HashMap<String, TableInfo.Column>(3);
        _columnsRubriques.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRubriques.put("categorie_id", new TableInfo.Column("categorie_id", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRubriques.put("nom", new TableInfo.Column("nom", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysRubriques = new HashSet<TableInfo.ForeignKey>(1);
        _foreignKeysRubriques.add(new TableInfo.ForeignKey("categories", "CASCADE", "NO ACTION", Arrays.asList("categorie_id"), Arrays.asList("id")));
        final HashSet<TableInfo.Index> _indicesRubriques = new HashSet<TableInfo.Index>(1);
        _indicesRubriques.add(new TableInfo.Index("index_rubriques_categorie_id", false, Arrays.asList("categorie_id"), Arrays.asList("ASC")));
        final TableInfo _infoRubriques = new TableInfo("rubriques", _columnsRubriques, _foreignKeysRubriques, _indicesRubriques);
        final TableInfo _existingRubriques = TableInfo.read(db, "rubriques");
        if (!_infoRubriques.equals(_existingRubriques)) {
          return new RoomOpenHelper.ValidationResult(false, "rubriques(com.tp.gestiondepenses.model.Rubrique).\n"
                  + " Expected:\n" + _infoRubriques + "\n"
                  + " Found:\n" + _existingRubriques);
        }
        final HashMap<String, TableInfo.Column> _columnsDepenses = new HashMap<String, TableInfo.Column>(8);
        _columnsDepenses.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDepenses.put("categorie_id", new TableInfo.Column("categorie_id", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDepenses.put("rubrique_id", new TableInfo.Column("rubrique_id", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDepenses.put("montant", new TableInfo.Column("montant", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDepenses.put("date", new TableInfo.Column("date", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDepenses.put("description", new TableInfo.Column("description", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDepenses.put("moyen_paiement", new TableInfo.Column("moyen_paiement", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDepenses.put("created_at", new TableInfo.Column("created_at", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysDepenses = new HashSet<TableInfo.ForeignKey>(2);
        _foreignKeysDepenses.add(new TableInfo.ForeignKey("categories", "CASCADE", "NO ACTION", Arrays.asList("categorie_id"), Arrays.asList("id")));
        _foreignKeysDepenses.add(new TableInfo.ForeignKey("rubriques", "CASCADE", "NO ACTION", Arrays.asList("rubrique_id"), Arrays.asList("id")));
        final HashSet<TableInfo.Index> _indicesDepenses = new HashSet<TableInfo.Index>(2);
        _indicesDepenses.add(new TableInfo.Index("index_depenses_categorie_id", false, Arrays.asList("categorie_id"), Arrays.asList("ASC")));
        _indicesDepenses.add(new TableInfo.Index("index_depenses_rubrique_id", false, Arrays.asList("rubrique_id"), Arrays.asList("ASC")));
        final TableInfo _infoDepenses = new TableInfo("depenses", _columnsDepenses, _foreignKeysDepenses, _indicesDepenses);
        final TableInfo _existingDepenses = TableInfo.read(db, "depenses");
        if (!_infoDepenses.equals(_existingDepenses)) {
          return new RoomOpenHelper.ValidationResult(false, "depenses(com.tp.gestiondepenses.model.Depense).\n"
                  + " Expected:\n" + _infoDepenses + "\n"
                  + " Found:\n" + _existingDepenses);
        }
        final HashMap<String, TableInfo.Column> _columnsRevenus = new HashMap<String, TableInfo.Column>(6);
        _columnsRevenus.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRevenus.put("source", new TableInfo.Column("source", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRevenus.put("montant", new TableInfo.Column("montant", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRevenus.put("date", new TableInfo.Column("date", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRevenus.put("description", new TableInfo.Column("description", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRevenus.put("created_at", new TableInfo.Column("created_at", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysRevenus = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesRevenus = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoRevenus = new TableInfo("revenus", _columnsRevenus, _foreignKeysRevenus, _indicesRevenus);
        final TableInfo _existingRevenus = TableInfo.read(db, "revenus");
        if (!_infoRevenus.equals(_existingRevenus)) {
          return new RoomOpenHelper.ValidationResult(false, "revenus(com.tp.gestiondepenses.model.Revenu).\n"
                  + " Expected:\n" + _infoRevenus + "\n"
                  + " Found:\n" + _existingRevenus);
        }
        final HashMap<String, TableInfo.Column> _columnsBudgets = new HashMap<String, TableInfo.Column>(6);
        _columnsBudgets.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBudgets.put("categorie_id", new TableInfo.Column("categorie_id", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBudgets.put("montant_plafond", new TableInfo.Column("montant_plafond", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBudgets.put("periode", new TableInfo.Column("periode", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBudgets.put("mois", new TableInfo.Column("mois", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBudgets.put("annee", new TableInfo.Column("annee", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysBudgets = new HashSet<TableInfo.ForeignKey>(1);
        _foreignKeysBudgets.add(new TableInfo.ForeignKey("categories", "NO ACTION", "NO ACTION", Arrays.asList("categorie_id"), Arrays.asList("id")));
        final HashSet<TableInfo.Index> _indicesBudgets = new HashSet<TableInfo.Index>(1);
        _indicesBudgets.add(new TableInfo.Index("index_budgets_categorie_id", false, Arrays.asList("categorie_id"), Arrays.asList("ASC")));
        final TableInfo _infoBudgets = new TableInfo("budgets", _columnsBudgets, _foreignKeysBudgets, _indicesBudgets);
        final TableInfo _existingBudgets = TableInfo.read(db, "budgets");
        if (!_infoBudgets.equals(_existingBudgets)) {
          return new RoomOpenHelper.ValidationResult(false, "budgets(com.tp.gestiondepenses.model.Budget).\n"
                  + " Expected:\n" + _infoBudgets + "\n"
                  + " Found:\n" + _existingBudgets);
        }
        return new RoomOpenHelper.ValidationResult(true, null);
      }
    }, "e706a74cd06bf4624a215fe4a7bd6ee8", "776dd20d831a5ed4838e4faae5942550");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(config.context).name(config.name).callback(_openCallback).build();
    final SupportSQLiteOpenHelper _helper = config.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  @NonNull
  protected InvalidationTracker createInvalidationTracker() {
    final HashMap<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    final HashMap<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "categories","rubriques","depenses","revenus","budgets");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    final boolean _supportsDeferForeignKeys = android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP;
    try {
      if (!_supportsDeferForeignKeys) {
        _db.execSQL("PRAGMA foreign_keys = FALSE");
      }
      super.beginTransaction();
      if (_supportsDeferForeignKeys) {
        _db.execSQL("PRAGMA defer_foreign_keys = TRUE");
      }
      _db.execSQL("DELETE FROM `categories`");
      _db.execSQL("DELETE FROM `rubriques`");
      _db.execSQL("DELETE FROM `depenses`");
      _db.execSQL("DELETE FROM `revenus`");
      _db.execSQL("DELETE FROM `budgets`");
      super.setTransactionSuccessful();
    } finally {
      super.endTransaction();
      if (!_supportsDeferForeignKeys) {
        _db.execSQL("PRAGMA foreign_keys = TRUE");
      }
      _db.query("PRAGMA wal_checkpoint(FULL)").close();
      if (!_db.inTransaction()) {
        _db.execSQL("VACUUM");
      }
    }
  }

  @Override
  @NonNull
  protected Map<Class<?>, List<Class<?>>> getRequiredTypeConverters() {
    final HashMap<Class<?>, List<Class<?>>> _typeConvertersMap = new HashMap<Class<?>, List<Class<?>>>();
    _typeConvertersMap.put(CategorieDao.class, CategorieDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(DepenseDao.class, DepenseDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(RevenuDao.class, RevenuDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(BudgetDao.class, BudgetDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(RubriqueDao.class, RubriqueDao_Impl.getRequiredConverters());
    return _typeConvertersMap;
  }

  @Override
  @NonNull
  public Set<Class<? extends AutoMigrationSpec>> getRequiredAutoMigrationSpecs() {
    final HashSet<Class<? extends AutoMigrationSpec>> _autoMigrationSpecsSet = new HashSet<Class<? extends AutoMigrationSpec>>();
    return _autoMigrationSpecsSet;
  }

  @Override
  @NonNull
  public List<Migration> getAutoMigrations(
      @NonNull final Map<Class<? extends AutoMigrationSpec>, AutoMigrationSpec> autoMigrationSpecs) {
    final List<Migration> _autoMigrations = new ArrayList<Migration>();
    return _autoMigrations;
  }

  @Override
  public CategorieDao categorieDao() {
    if (_categorieDao != null) {
      return _categorieDao;
    } else {
      synchronized(this) {
        if(_categorieDao == null) {
          _categorieDao = new CategorieDao_Impl(this);
        }
        return _categorieDao;
      }
    }
  }

  @Override
  public DepenseDao depenseDao() {
    if (_depenseDao != null) {
      return _depenseDao;
    } else {
      synchronized(this) {
        if(_depenseDao == null) {
          _depenseDao = new DepenseDao_Impl(this);
        }
        return _depenseDao;
      }
    }
  }

  @Override
  public RevenuDao revenuDao() {
    if (_revenuDao != null) {
      return _revenuDao;
    } else {
      synchronized(this) {
        if(_revenuDao == null) {
          _revenuDao = new RevenuDao_Impl(this);
        }
        return _revenuDao;
      }
    }
  }

  @Override
  public BudgetDao budgetDao() {
    if (_budgetDao != null) {
      return _budgetDao;
    } else {
      synchronized(this) {
        if(_budgetDao == null) {
          _budgetDao = new BudgetDao_Impl(this);
        }
        return _budgetDao;
      }
    }
  }

  @Override
  public RubriqueDao rubriqueDao() {
    if (_rubriqueDao != null) {
      return _rubriqueDao;
    } else {
      synchronized(this) {
        if(_rubriqueDao == null) {
          _rubriqueDao = new RubriqueDao_Impl(this);
        }
        return _rubriqueDao;
      }
    }
  }
}
