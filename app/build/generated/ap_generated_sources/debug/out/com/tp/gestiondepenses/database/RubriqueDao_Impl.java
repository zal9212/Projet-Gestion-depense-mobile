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
import com.tp.gestiondepenses.model.Rubrique;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;

@SuppressWarnings({"unchecked", "deprecation"})
public final class RubriqueDao_Impl implements RubriqueDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<Rubrique> __insertionAdapterOfRubrique;

  private final EntityDeletionOrUpdateAdapter<Rubrique> __deletionAdapterOfRubrique;

  private final EntityDeletionOrUpdateAdapter<Rubrique> __updateAdapterOfRubrique;

  private final SharedSQLiteStatement __preparedStmtOfDeleteAll;

  public RubriqueDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfRubrique = new EntityInsertionAdapter<Rubrique>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `rubriques` (`id`,`categorie_id`,`nom`) VALUES (nullif(?, 0),?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement, final Rubrique entity) {
        statement.bindLong(1, entity.id);
        statement.bindLong(2, entity.categorie_id);
        if (entity.nom == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.nom);
        }
      }
    };
    this.__deletionAdapterOfRubrique = new EntityDeletionOrUpdateAdapter<Rubrique>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `rubriques` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement, final Rubrique entity) {
        statement.bindLong(1, entity.id);
      }
    };
    this.__updateAdapterOfRubrique = new EntityDeletionOrUpdateAdapter<Rubrique>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `rubriques` SET `id` = ?,`categorie_id` = ?,`nom` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement, final Rubrique entity) {
        statement.bindLong(1, entity.id);
        statement.bindLong(2, entity.categorie_id);
        if (entity.nom == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.nom);
        }
        statement.bindLong(4, entity.id);
      }
    };
    this.__preparedStmtOfDeleteAll = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM rubriques";
        return _query;
      }
    };
  }

  @Override
  public void insertRubrique(final Rubrique rubrique) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfRubrique.insert(rubrique);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void delete(final Rubrique rubrique) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __deletionAdapterOfRubrique.handle(rubrique);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void update(final Rubrique rubrique) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __updateAdapterOfRubrique.handle(rubrique);
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
  public LiveData<List<Rubrique>> getRubriquesByCategorie(final int categorieId) {
    final String _sql = "SELECT * FROM rubriques WHERE categorie_id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, categorieId);
    return __db.getInvalidationTracker().createLiveData(new String[] {"rubriques"}, false, new Callable<List<Rubrique>>() {
      @Override
      @Nullable
      public List<Rubrique> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfCategorieId = CursorUtil.getColumnIndexOrThrow(_cursor, "categorie_id");
          final int _cursorIndexOfNom = CursorUtil.getColumnIndexOrThrow(_cursor, "nom");
          final List<Rubrique> _result = new ArrayList<Rubrique>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Rubrique _item;
            final int _tmpCategorie_id;
            _tmpCategorie_id = _cursor.getInt(_cursorIndexOfCategorieId);
            final String _tmpNom;
            if (_cursor.isNull(_cursorIndexOfNom)) {
              _tmpNom = null;
            } else {
              _tmpNom = _cursor.getString(_cursorIndexOfNom);
            }
            _item = new Rubrique(_tmpCategorie_id,_tmpNom);
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
  public LiveData<List<Rubrique>> getAllRubriques() {
    final String _sql = "SELECT * FROM rubriques";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return __db.getInvalidationTracker().createLiveData(new String[] {"rubriques"}, false, new Callable<List<Rubrique>>() {
      @Override
      @Nullable
      public List<Rubrique> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfCategorieId = CursorUtil.getColumnIndexOrThrow(_cursor, "categorie_id");
          final int _cursorIndexOfNom = CursorUtil.getColumnIndexOrThrow(_cursor, "nom");
          final List<Rubrique> _result = new ArrayList<Rubrique>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Rubrique _item;
            final int _tmpCategorie_id;
            _tmpCategorie_id = _cursor.getInt(_cursorIndexOfCategorieId);
            final String _tmpNom;
            if (_cursor.isNull(_cursorIndexOfNom)) {
              _tmpNom = null;
            } else {
              _tmpNom = _cursor.getString(_cursorIndexOfNom);
            }
            _item = new Rubrique(_tmpCategorie_id,_tmpNom);
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

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
