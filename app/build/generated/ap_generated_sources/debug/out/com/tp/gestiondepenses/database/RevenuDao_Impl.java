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
import com.tp.gestiondepenses.model.Revenu;
import java.lang.Class;
import java.lang.Double;
import java.lang.Exception;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;

@SuppressWarnings({"unchecked", "deprecation"})
public final class RevenuDao_Impl implements RevenuDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<Revenu> __insertionAdapterOfRevenu;

  private final EntityDeletionOrUpdateAdapter<Revenu> __deletionAdapterOfRevenu;

  private final EntityDeletionOrUpdateAdapter<Revenu> __updateAdapterOfRevenu;

  private final SharedSQLiteStatement __preparedStmtOfDeleteAll;

  public RevenuDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfRevenu = new EntityInsertionAdapter<Revenu>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `revenus` (`id`,`source`,`montant`,`date`,`description`,`created_at`) VALUES (nullif(?, 0),?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement, final Revenu entity) {
        statement.bindLong(1, entity.id);
        if (entity.source == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.source);
        }
        statement.bindDouble(3, entity.montant);
        statement.bindLong(4, entity.date);
        if (entity.description == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.description);
        }
        statement.bindLong(6, entity.created_at);
      }
    };
    this.__deletionAdapterOfRevenu = new EntityDeletionOrUpdateAdapter<Revenu>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `revenus` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement, final Revenu entity) {
        statement.bindLong(1, entity.id);
      }
    };
    this.__updateAdapterOfRevenu = new EntityDeletionOrUpdateAdapter<Revenu>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `revenus` SET `id` = ?,`source` = ?,`montant` = ?,`date` = ?,`description` = ?,`created_at` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement, final Revenu entity) {
        statement.bindLong(1, entity.id);
        if (entity.source == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.source);
        }
        statement.bindDouble(3, entity.montant);
        statement.bindLong(4, entity.date);
        if (entity.description == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.description);
        }
        statement.bindLong(6, entity.created_at);
        statement.bindLong(7, entity.id);
      }
    };
    this.__preparedStmtOfDeleteAll = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM revenus";
        return _query;
      }
    };
  }

  @Override
  public void insertRevenu(final Revenu revenu) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfRevenu.insert(revenu);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void deleteRevenu(final Revenu revenu) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __deletionAdapterOfRevenu.handle(revenu);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void updateRevenu(final Revenu revenu) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __updateAdapterOfRevenu.handle(revenu);
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
  public LiveData<List<Revenu>> getAllRevenus() {
    final String _sql = "SELECT * FROM revenus ORDER BY date DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return __db.getInvalidationTracker().createLiveData(new String[] {"revenus"}, false, new Callable<List<Revenu>>() {
      @Override
      @Nullable
      public List<Revenu> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfSource = CursorUtil.getColumnIndexOrThrow(_cursor, "source");
          final int _cursorIndexOfMontant = CursorUtil.getColumnIndexOrThrow(_cursor, "montant");
          final int _cursorIndexOfDate = CursorUtil.getColumnIndexOrThrow(_cursor, "date");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "created_at");
          final List<Revenu> _result = new ArrayList<Revenu>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Revenu _item;
            final String _tmpSource;
            if (_cursor.isNull(_cursorIndexOfSource)) {
              _tmpSource = null;
            } else {
              _tmpSource = _cursor.getString(_cursorIndexOfSource);
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
            _item = new Revenu(_tmpSource,_tmpMontant,_tmpDate,_tmpDescription);
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
  public LiveData<Double> getTotalRevenusParMois(final String mois, final String annee) {
    final String _sql = "SELECT SUM(montant) FROM revenus WHERE strftime('%m', datetime(date/1000, 'unixepoch')) = ? AND strftime('%Y', datetime(date/1000, 'unixepoch')) = ?";
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
    return __db.getInvalidationTracker().createLiveData(new String[] {"revenus"}, false, new Callable<Double>() {
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
  public LiveData<Revenu> getRevenuById(final int id) {
    final String _sql = "SELECT * FROM revenus WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    return __db.getInvalidationTracker().createLiveData(new String[] {"revenus"}, false, new Callable<Revenu>() {
      @Override
      @Nullable
      public Revenu call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfSource = CursorUtil.getColumnIndexOrThrow(_cursor, "source");
          final int _cursorIndexOfMontant = CursorUtil.getColumnIndexOrThrow(_cursor, "montant");
          final int _cursorIndexOfDate = CursorUtil.getColumnIndexOrThrow(_cursor, "date");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "created_at");
          final Revenu _result;
          if (_cursor.moveToFirst()) {
            final String _tmpSource;
            if (_cursor.isNull(_cursorIndexOfSource)) {
              _tmpSource = null;
            } else {
              _tmpSource = _cursor.getString(_cursorIndexOfSource);
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
            _result = new Revenu(_tmpSource,_tmpMontant,_tmpDate,_tmpDescription);
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
  public LiveData<Double> getTotalRevenus() {
    final String _sql = "SELECT SUM(montant) FROM revenus";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return __db.getInvalidationTracker().createLiveData(new String[] {"revenus"}, false, new Callable<Double>() {
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
