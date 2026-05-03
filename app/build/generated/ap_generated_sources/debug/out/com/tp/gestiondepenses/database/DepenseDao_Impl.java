package com.tp.gestiondepenses.database;

import android.database.Cursor;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.tp.gestiondepenses.model.Depense;
import java.lang.Class;
import java.lang.Double;
import java.lang.Exception;
import java.lang.Integer;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;

@SuppressWarnings({"unchecked", "deprecation"})
public final class DepenseDao_Impl implements DepenseDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<Depense> __insertionAdapterOfDepense;

  private final EntityDeletionOrUpdateAdapter<Depense> __deletionAdapterOfDepense;

  private final EntityDeletionOrUpdateAdapter<Depense> __updateAdapterOfDepense;

  private final SharedSQLiteStatement __preparedStmtOfDeleteAll;

  public DepenseDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfDepense = new EntityInsertionAdapter<Depense>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `depenses` (`id`,`categorie_id`,`rubrique_id`,`montant`,`date`,`description`,`moyen_paiement`,`created_at`) VALUES (nullif(?, 0),?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement, final Depense entity) {
        statement.bindLong(1, entity.id);
        statement.bindLong(2, entity.categorie_id);
        if (entity.rubrique_id == null) {
          statement.bindNull(3);
        } else {
          statement.bindLong(3, entity.rubrique_id);
        }
        statement.bindDouble(4, entity.montant);
        statement.bindLong(5, entity.date);
        if (entity.description == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.description);
        }
        if (entity.moyen_paiement == null) {
          statement.bindNull(7);
        } else {
          statement.bindString(7, entity.moyen_paiement);
        }
        statement.bindLong(8, entity.created_at);
      }
    };
    this.__deletionAdapterOfDepense = new EntityDeletionOrUpdateAdapter<Depense>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `depenses` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement, final Depense entity) {
        statement.bindLong(1, entity.id);
      }
    };
    this.__updateAdapterOfDepense = new EntityDeletionOrUpdateAdapter<Depense>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `depenses` SET `id` = ?,`categorie_id` = ?,`rubrique_id` = ?,`montant` = ?,`date` = ?,`description` = ?,`moyen_paiement` = ?,`created_at` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement, final Depense entity) {
        statement.bindLong(1, entity.id);
        statement.bindLong(2, entity.categorie_id);
        if (entity.rubrique_id == null) {
          statement.bindNull(3);
        } else {
          statement.bindLong(3, entity.rubrique_id);
        }
        statement.bindDouble(4, entity.montant);
        statement.bindLong(5, entity.date);
        if (entity.description == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.description);
        }
        if (entity.moyen_paiement == null) {
          statement.bindNull(7);
        } else {
          statement.bindString(7, entity.moyen_paiement);
        }
        statement.bindLong(8, entity.created_at);
        statement.bindLong(9, entity.id);
      }
    };
    this.__preparedStmtOfDeleteAll = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM depenses";
        return _query;
      }
    };
  }

  @Override
  public void insertDepense(final Depense depense) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfDepense.insert(depense);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void deleteDepense(final Depense depense) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __deletionAdapterOfDepense.handle(depense);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void updateDepense(final Depense depense) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __updateAdapterOfDepense.handle(depense);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void deleteAll() {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteAll.acquire();
    try {
      __db.beginTransaction();
      try {
        _stmt.executeUpdateDelete();
        __db.setTransactionSuccessful();
      } finally {
        __db.endTransaction();
      }
    } finally {
      __preparedStmtOfDeleteAll.release(_stmt);
    }
  }

  @Override
  public LiveData<List<Depense>> getAllDepenses() {
    final String _sql = "SELECT * FROM depenses ORDER BY date DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return __db.getInvalidationTracker().createLiveData(new String[] {"depenses"}, false, new Callable<List<Depense>>() {
      @Override
      @Nullable
      public List<Depense> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfCategorieId = CursorUtil.getColumnIndexOrThrow(_cursor, "categorie_id");
          final int _cursorIndexOfRubriqueId = CursorUtil.getColumnIndexOrThrow(_cursor, "rubrique_id");
          final int _cursorIndexOfMontant = CursorUtil.getColumnIndexOrThrow(_cursor, "montant");
          final int _cursorIndexOfDate = CursorUtil.getColumnIndexOrThrow(_cursor, "date");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final int _cursorIndexOfMoyenPaiement = CursorUtil.getColumnIndexOrThrow(_cursor, "moyen_paiement");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "created_at");
          final List<Depense> _result = new ArrayList<Depense>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Depense _item;
            final int _tmpCategorie_id;
            _tmpCategorie_id = _cursor.getInt(_cursorIndexOfCategorieId);
            final Integer _tmpRubrique_id;
            if (_cursor.isNull(_cursorIndexOfRubriqueId)) {
              _tmpRubrique_id = null;
            } else {
              _tmpRubrique_id = _cursor.getInt(_cursorIndexOfRubriqueId);
            }
            final double _tmpMontant;
            _tmpMontant = _cursor.getDouble(_cursorIndexOfMontant);
            final long _tmpDate;
            _tmpDate = _cursor.getLong(_cursorIndexOfDate);
            final String _tmpDescription;
            if (_cursor.isNull(_cursorIndexOfDescription)) {
              _tmpDescription = null;
            } else {
              _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
            }
            final String _tmpMoyen_paiement;
            if (_cursor.isNull(_cursorIndexOfMoyenPaiement)) {
              _tmpMoyen_paiement = null;
            } else {
              _tmpMoyen_paiement = _cursor.getString(_cursorIndexOfMoyenPaiement);
            }
            _item = new Depense(_tmpCategorie_id,_tmpRubrique_id,_tmpMontant,_tmpDate,_tmpDescription,_tmpMoyen_paiement);
            _item.id = _cursor.getInt(_cursorIndexOfId);
            _item.created_at = _cursor.getLong(_cursorIndexOfCreatedAt);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public LiveData<List<Depense>> getDepensesByMois(final String mois, final String annee) {
    final String _sql = "SELECT * FROM depenses WHERE strftime('%m', datetime(date/1000, 'unixepoch')) = ? AND strftime('%Y', datetime(date/1000, 'unixepoch')) = ? ORDER BY date DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    if (mois == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, mois);
    }
    _argIndex = 2;
    if (annee == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, annee);
    }
    return __db.getInvalidationTracker().createLiveData(new String[] {"depenses"}, false, new Callable<List<Depense>>() {
      @Override
      @Nullable
      public List<Depense> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfCategorieId = CursorUtil.getColumnIndexOrThrow(_cursor, "categorie_id");
          final int _cursorIndexOfRubriqueId = CursorUtil.getColumnIndexOrThrow(_cursor, "rubrique_id");
          final int _cursorIndexOfMontant = CursorUtil.getColumnIndexOrThrow(_cursor, "montant");
          final int _cursorIndexOfDate = CursorUtil.getColumnIndexOrThrow(_cursor, "date");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final int _cursorIndexOfMoyenPaiement = CursorUtil.getColumnIndexOrThrow(_cursor, "moyen_paiement");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "created_at");
          final List<Depense> _result = new ArrayList<Depense>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Depense _item;
            final int _tmpCategorie_id;
            _tmpCategorie_id = _cursor.getInt(_cursorIndexOfCategorieId);
            final Integer _tmpRubrique_id;
            if (_cursor.isNull(_cursorIndexOfRubriqueId)) {
              _tmpRubrique_id = null;
            } else {
              _tmpRubrique_id = _cursor.getInt(_cursorIndexOfRubriqueId);
            }
            final double _tmpMontant;
            _tmpMontant = _cursor.getDouble(_cursorIndexOfMontant);
            final long _tmpDate;
            _tmpDate = _cursor.getLong(_cursorIndexOfDate);
            final String _tmpDescription;
            if (_cursor.isNull(_cursorIndexOfDescription)) {
              _tmpDescription = null;
            } else {
              _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
            }
            final String _tmpMoyen_paiement;
            if (_cursor.isNull(_cursorIndexOfMoyenPaiement)) {
              _tmpMoyen_paiement = null;
            } else {
              _tmpMoyen_paiement = _cursor.getString(_cursorIndexOfMoyenPaiement);
            }
            _item = new Depense(_tmpCategorie_id,_tmpRubrique_id,_tmpMontant,_tmpDate,_tmpDescription,_tmpMoyen_paiement);
            _item.id = _cursor.getInt(_cursorIndexOfId);
            _item.created_at = _cursor.getLong(_cursorIndexOfCreatedAt);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public LiveData<Double> getTotalDepensesParMois(final String mois, final String annee) {
    final String _sql = "SELECT SUM(montant) FROM depenses WHERE strftime('%m', datetime(date/1000, 'unixepoch')) = ? AND strftime('%Y', datetime(date/1000, 'unixepoch')) = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    if (mois == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, mois);
    }
    _argIndex = 2;
    if (annee == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, annee);
    }
    return __db.getInvalidationTracker().createLiveData(new String[] {"depenses"}, false, new Callable<Double>() {
      @Override
      @Nullable
      public Double call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Double _result;
          if (_cursor.moveToFirst()) {
            final Double _tmp;
            if (_cursor.isNull(0)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getDouble(0);
            }
            _result = _tmp;
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public LiveData<Depense> getDepenseById(final int id) {
    final String _sql = "SELECT * FROM depenses WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    return __db.getInvalidationTracker().createLiveData(new String[] {"depenses"}, false, new Callable<Depense>() {
      @Override
      @Nullable
      public Depense call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfCategorieId = CursorUtil.getColumnIndexOrThrow(_cursor, "categorie_id");
          final int _cursorIndexOfRubriqueId = CursorUtil.getColumnIndexOrThrow(_cursor, "rubrique_id");
          final int _cursorIndexOfMontant = CursorUtil.getColumnIndexOrThrow(_cursor, "montant");
          final int _cursorIndexOfDate = CursorUtil.getColumnIndexOrThrow(_cursor, "date");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final int _cursorIndexOfMoyenPaiement = CursorUtil.getColumnIndexOrThrow(_cursor, "moyen_paiement");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "created_at");
          final Depense _result;
          if (_cursor.moveToFirst()) {
            final int _tmpCategorie_id;
            _tmpCategorie_id = _cursor.getInt(_cursorIndexOfCategorieId);
            final Integer _tmpRubrique_id;
            if (_cursor.isNull(_cursorIndexOfRubriqueId)) {
              _tmpRubrique_id = null;
            } else {
              _tmpRubrique_id = _cursor.getInt(_cursorIndexOfRubriqueId);
            }
            final double _tmpMontant;
            _tmpMontant = _cursor.getDouble(_cursorIndexOfMontant);
            final long _tmpDate;
            _tmpDate = _cursor.getLong(_cursorIndexOfDate);
            final String _tmpDescription;
            if (_cursor.isNull(_cursorIndexOfDescription)) {
              _tmpDescription = null;
            } else {
              _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
            }
            final String _tmpMoyen_paiement;
            if (_cursor.isNull(_cursorIndexOfMoyenPaiement)) {
              _tmpMoyen_paiement = null;
            } else {
              _tmpMoyen_paiement = _cursor.getString(_cursorIndexOfMoyenPaiement);
            }
            _result = new Depense(_tmpCategorie_id,_tmpRubrique_id,_tmpMontant,_tmpDate,_tmpDescription,_tmpMoyen_paiement);
            _result.id = _cursor.getInt(_cursorIndexOfId);
            _result.created_at = _cursor.getLong(_cursorIndexOfCreatedAt);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public LiveData<Double> getTotalDepenses() {
    final String _sql = "SELECT SUM(montant) FROM depenses";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return __db.getInvalidationTracker().createLiveData(new String[] {"depenses"}, false, new Callable<Double>() {
      @Override
      @Nullable
      public Double call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Double _result;
          if (_cursor.moveToFirst()) {
            final Double _tmp;
            if (_cursor.isNull(0)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getDouble(0);
            }
            _result = _tmp;
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
