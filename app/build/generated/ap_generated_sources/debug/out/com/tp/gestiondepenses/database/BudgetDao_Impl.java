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
import com.tp.gestiondepenses.model.Budget;
import java.lang.Class;
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
public final class BudgetDao_Impl implements BudgetDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<Budget> __insertionAdapterOfBudget;

  private final EntityDeletionOrUpdateAdapter<Budget> __deletionAdapterOfBudget;

  private final EntityDeletionOrUpdateAdapter<Budget> __updateAdapterOfBudget;

  private final SharedSQLiteStatement __preparedStmtOfDeleteAll;

  public BudgetDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfBudget = new EntityInsertionAdapter<Budget>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `budgets` (`id`,`categorie_id`,`montant_plafond`,`periode`,`mois`,`annee`) VALUES (nullif(?, 0),?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement, final Budget entity) {
        statement.bindLong(1, entity.id);
        if (entity.categorie_id == null) {
          statement.bindNull(2);
        } else {
          statement.bindLong(2, entity.categorie_id);
        }
        statement.bindDouble(3, entity.montant_plafond);
        if (entity.periode == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.periode);
        }
        statement.bindLong(5, entity.mois);
        statement.bindLong(6, entity.annee);
      }
    };
    this.__deletionAdapterOfBudget = new EntityDeletionOrUpdateAdapter<Budget>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `budgets` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement, final Budget entity) {
        statement.bindLong(1, entity.id);
      }
    };
    this.__updateAdapterOfBudget = new EntityDeletionOrUpdateAdapter<Budget>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `budgets` SET `id` = ?,`categorie_id` = ?,`montant_plafond` = ?,`periode` = ?,`mois` = ?,`annee` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement, final Budget entity) {
        statement.bindLong(1, entity.id);
        if (entity.categorie_id == null) {
          statement.bindNull(2);
        } else {
          statement.bindLong(2, entity.categorie_id);
        }
        statement.bindDouble(3, entity.montant_plafond);
        if (entity.periode == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.periode);
        }
        statement.bindLong(5, entity.mois);
        statement.bindLong(6, entity.annee);
        statement.bindLong(7, entity.id);
      }
    };
    this.__preparedStmtOfDeleteAll = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM budgets";
        return _query;
      }
    };
  }

  @Override
  public void insertBudget(final Budget budget) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfBudget.insert(budget);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void deleteBudget(final Budget budget) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __deletionAdapterOfBudget.handle(budget);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void updateBudget(final Budget budget) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __updateAdapterOfBudget.handle(budget);
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
  public LiveData<Budget> getBudgetByCategorie(final int cat_id, final int mois, final int annee) {
    final String _sql = "SELECT * FROM budgets WHERE categorie_id = ? AND mois = ? AND annee = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 3);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, cat_id);
    _argIndex = 2;
    _statement.bindLong(_argIndex, mois);
    _argIndex = 3;
    _statement.bindLong(_argIndex, annee);
    return __db.getInvalidationTracker().createLiveData(new String[] {"budgets"}, false, new Callable<Budget>() {
      @Override
      @Nullable
      public Budget call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfCategorieId = CursorUtil.getColumnIndexOrThrow(_cursor, "categorie_id");
          final int _cursorIndexOfMontantPlafond = CursorUtil.getColumnIndexOrThrow(_cursor, "montant_plafond");
          final int _cursorIndexOfPeriode = CursorUtil.getColumnIndexOrThrow(_cursor, "periode");
          final int _cursorIndexOfMois = CursorUtil.getColumnIndexOrThrow(_cursor, "mois");
          final int _cursorIndexOfAnnee = CursorUtil.getColumnIndexOrThrow(_cursor, "annee");
          final Budget _result;
          if (_cursor.moveToFirst()) {
            final Integer _tmpCategorie_id;
            if (_cursor.isNull(_cursorIndexOfCategorieId)) {
              _tmpCategorie_id = null;
            } else {
              _tmpCategorie_id = _cursor.getInt(_cursorIndexOfCategorieId);
            }
            final double _tmpMontant_plafond;
            _tmpMontant_plafond = _cursor.getDouble(_cursorIndexOfMontantPlafond);
            final String _tmpPeriode;
            if (_cursor.isNull(_cursorIndexOfPeriode)) {
              _tmpPeriode = null;
            } else {
              _tmpPeriode = _cursor.getString(_cursorIndexOfPeriode);
            }
            final int _tmpMois;
            _tmpMois = _cursor.getInt(_cursorIndexOfMois);
            final int _tmpAnnee;
            _tmpAnnee = _cursor.getInt(_cursorIndexOfAnnee);
            _result = new Budget(_tmpCategorie_id,_tmpMontant_plafond,_tmpPeriode,_tmpMois,_tmpAnnee);
            _result.id = _cursor.getInt(_cursorIndexOfId);
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
  public LiveData<List<Budget>> getAllBudgets() {
    final String _sql = "SELECT * FROM budgets";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return __db.getInvalidationTracker().createLiveData(new String[] {"budgets"}, false, new Callable<List<Budget>>() {
      @Override
      @Nullable
      public List<Budget> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfCategorieId = CursorUtil.getColumnIndexOrThrow(_cursor, "categorie_id");
          final int _cursorIndexOfMontantPlafond = CursorUtil.getColumnIndexOrThrow(_cursor, "montant_plafond");
          final int _cursorIndexOfPeriode = CursorUtil.getColumnIndexOrThrow(_cursor, "periode");
          final int _cursorIndexOfMois = CursorUtil.getColumnIndexOrThrow(_cursor, "mois");
          final int _cursorIndexOfAnnee = CursorUtil.getColumnIndexOrThrow(_cursor, "annee");
          final List<Budget> _result = new ArrayList<Budget>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Budget _item;
            final Integer _tmpCategorie_id;
            if (_cursor.isNull(_cursorIndexOfCategorieId)) {
              _tmpCategorie_id = null;
            } else {
              _tmpCategorie_id = _cursor.getInt(_cursorIndexOfCategorieId);
            }
            final double _tmpMontant_plafond;
            _tmpMontant_plafond = _cursor.getDouble(_cursorIndexOfMontantPlafond);
            final String _tmpPeriode;
            if (_cursor.isNull(_cursorIndexOfPeriode)) {
              _tmpPeriode = null;
            } else {
              _tmpPeriode = _cursor.getString(_cursorIndexOfPeriode);
            }
            final int _tmpMois;
            _tmpMois = _cursor.getInt(_cursorIndexOfMois);
            final int _tmpAnnee;
            _tmpAnnee = _cursor.getInt(_cursorIndexOfAnnee);
            _item = new Budget(_tmpCategorie_id,_tmpMontant_plafond,_tmpPeriode,_tmpMois,_tmpAnnee);
            _item.id = _cursor.getInt(_cursorIndexOfId);
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
  public LiveData<Budget> getBudgetById(final int id) {
    final String _sql = "SELECT * FROM budgets WHERE id = ? LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    return __db.getInvalidationTracker().createLiveData(new String[] {"budgets"}, false, new Callable<Budget>() {
      @Override
      @Nullable
      public Budget call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfCategorieId = CursorUtil.getColumnIndexOrThrow(_cursor, "categorie_id");
          final int _cursorIndexOfMontantPlafond = CursorUtil.getColumnIndexOrThrow(_cursor, "montant_plafond");
          final int _cursorIndexOfPeriode = CursorUtil.getColumnIndexOrThrow(_cursor, "periode");
          final int _cursorIndexOfMois = CursorUtil.getColumnIndexOrThrow(_cursor, "mois");
          final int _cursorIndexOfAnnee = CursorUtil.getColumnIndexOrThrow(_cursor, "annee");
          final Budget _result;
          if (_cursor.moveToFirst()) {
            final Integer _tmpCategorie_id;
            if (_cursor.isNull(_cursorIndexOfCategorieId)) {
              _tmpCategorie_id = null;
            } else {
              _tmpCategorie_id = _cursor.getInt(_cursorIndexOfCategorieId);
            }
            final double _tmpMontant_plafond;
            _tmpMontant_plafond = _cursor.getDouble(_cursorIndexOfMontantPlafond);
            final String _tmpPeriode;
            if (_cursor.isNull(_cursorIndexOfPeriode)) {
              _tmpPeriode = null;
            } else {
              _tmpPeriode = _cursor.getString(_cursorIndexOfPeriode);
            }
            final int _tmpMois;
            _tmpMois = _cursor.getInt(_cursorIndexOfMois);
            final int _tmpAnnee;
            _tmpAnnee = _cursor.getInt(_cursorIndexOfAnnee);
            _result = new Budget(_tmpCategorie_id,_tmpMontant_plafond,_tmpPeriode,_tmpMois,_tmpAnnee);
            _result.id = _cursor.getInt(_cursorIndexOfId);
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
  public LiveData<Budget> getBudgetGlobal(final int mois, final int annee) {
    final String _sql = "SELECT * FROM budgets WHERE categorie_id IS NULL AND mois = ? AND annee = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, mois);
    _argIndex = 2;
    _statement.bindLong(_argIndex, annee);
    return __db.getInvalidationTracker().createLiveData(new String[] {"budgets"}, false, new Callable<Budget>() {
      @Override
      @Nullable
      public Budget call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfCategorieId = CursorUtil.getColumnIndexOrThrow(_cursor, "categorie_id");
          final int _cursorIndexOfMontantPlafond = CursorUtil.getColumnIndexOrThrow(_cursor, "montant_plafond");
          final int _cursorIndexOfPeriode = CursorUtil.getColumnIndexOrThrow(_cursor, "periode");
          final int _cursorIndexOfMois = CursorUtil.getColumnIndexOrThrow(_cursor, "mois");
          final int _cursorIndexOfAnnee = CursorUtil.getColumnIndexOrThrow(_cursor, "annee");
          final Budget _result;
          if (_cursor.moveToFirst()) {
            final Integer _tmpCategorie_id;
            if (_cursor.isNull(_cursorIndexOfCategorieId)) {
              _tmpCategorie_id = null;
            } else {
              _tmpCategorie_id = _cursor.getInt(_cursorIndexOfCategorieId);
            }
            final double _tmpMontant_plafond;
            _tmpMontant_plafond = _cursor.getDouble(_cursorIndexOfMontantPlafond);
            final String _tmpPeriode;
            if (_cursor.isNull(_cursorIndexOfPeriode)) {
              _tmpPeriode = null;
            } else {
              _tmpPeriode = _cursor.getString(_cursorIndexOfPeriode);
            }
            final int _tmpMois;
            _tmpMois = _cursor.getInt(_cursorIndexOfMois);
            final int _tmpAnnee;
            _tmpAnnee = _cursor.getInt(_cursorIndexOfAnnee);
            _result = new Budget(_tmpCategorie_id,_tmpMontant_plafond,_tmpPeriode,_tmpMois,_tmpAnnee);
            _result.id = _cursor.getInt(_cursorIndexOfId);
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
