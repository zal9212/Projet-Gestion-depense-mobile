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
import com.tp.gestiondepenses.model.Categorie;
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
public final class CategorieDao_Impl implements CategorieDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<Categorie> __insertionAdapterOfCategorie;

  private final EntityDeletionOrUpdateAdapter<Categorie> __deletionAdapterOfCategorie;

  private final EntityDeletionOrUpdateAdapter<Categorie> __updateAdapterOfCategorie;

  private final SharedSQLiteStatement __preparedStmtOfDeleteAll;

  public CategorieDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfCategorie = new EntityInsertionAdapter<Categorie>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `categories` (`id`,`nom`,`icone`,`couleur`,`est_defaut`) VALUES (nullif(?, 0),?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement, final Categorie entity) {
        statement.bindLong(1, entity.id);
        if (entity.nom == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.nom);
        }
        if (entity.icone == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.icone);
        }
        if (entity.couleur == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.couleur);
        }
        final int _tmp = entity.est_defaut ? 1 : 0;
        statement.bindLong(5, _tmp);
      }
    };
    this.__deletionAdapterOfCategorie = new EntityDeletionOrUpdateAdapter<Categorie>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `categories` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement, final Categorie entity) {
        statement.bindLong(1, entity.id);
      }
    };
    this.__updateAdapterOfCategorie = new EntityDeletionOrUpdateAdapter<Categorie>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `categories` SET `id` = ?,`nom` = ?,`icone` = ?,`couleur` = ?,`est_defaut` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement, final Categorie entity) {
        statement.bindLong(1, entity.id);
        if (entity.nom == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.nom);
        }
        if (entity.icone == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.icone);
        }
        if (entity.couleur == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.couleur);
        }
        final int _tmp = entity.est_defaut ? 1 : 0;
        statement.bindLong(5, _tmp);
        statement.bindLong(6, entity.id);
      }
    };
    this.__preparedStmtOfDeleteAll = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM categories";
        return _query;
      }
    };
  }

  @Override
  public void insertCategorie(final Categorie categorie) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfCategorie.insert(categorie);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public long insertCategorieWithId(final Categorie categorie) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      final long _result = __insertionAdapterOfCategorie.insertAndReturnId(categorie);
      __db.setTransactionSuccessful();
      return _result;
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void deleteCategorie(final Categorie categorie) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __deletionAdapterOfCategorie.handle(categorie);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void updateCategorie(final Categorie categorie) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __updateAdapterOfCategorie.handle(categorie);
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
  public LiveData<List<Categorie>> getAllCategories() {
    final String _sql = "SELECT * FROM categories";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return __db.getInvalidationTracker().createLiveData(new String[] {"categories"}, false, new Callable<List<Categorie>>() {
      @Override
      @Nullable
      public List<Categorie> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfNom = CursorUtil.getColumnIndexOrThrow(_cursor, "nom");
          final int _cursorIndexOfIcone = CursorUtil.getColumnIndexOrThrow(_cursor, "icone");
          final int _cursorIndexOfCouleur = CursorUtil.getColumnIndexOrThrow(_cursor, "couleur");
          final int _cursorIndexOfEstDefaut = CursorUtil.getColumnIndexOrThrow(_cursor, "est_defaut");
          final List<Categorie> _result = new ArrayList<Categorie>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Categorie _item;
            final String _tmpNom;
            if (_cursor.isNull(_cursorIndexOfNom)) {
              _tmpNom = null;
            } else {
              _tmpNom = _cursor.getString(_cursorIndexOfNom);
            }
            final String _tmpIcone;
            if (_cursor.isNull(_cursorIndexOfIcone)) {
              _tmpIcone = null;
            } else {
              _tmpIcone = _cursor.getString(_cursorIndexOfIcone);
            }
            final String _tmpCouleur;
            if (_cursor.isNull(_cursorIndexOfCouleur)) {
              _tmpCouleur = null;
            } else {
              _tmpCouleur = _cursor.getString(_cursorIndexOfCouleur);
            }
            final boolean _tmpEst_defaut;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfEstDefaut);
            _tmpEst_defaut = _tmp != 0;
            _item = new Categorie(_tmpNom,_tmpIcone,_tmpCouleur,_tmpEst_defaut);
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
  public Categorie getCategorieById(final int id) {
    final String _sql = "SELECT * FROM categories WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfNom = CursorUtil.getColumnIndexOrThrow(_cursor, "nom");
      final int _cursorIndexOfIcone = CursorUtil.getColumnIndexOrThrow(_cursor, "icone");
      final int _cursorIndexOfCouleur = CursorUtil.getColumnIndexOrThrow(_cursor, "couleur");
      final int _cursorIndexOfEstDefaut = CursorUtil.getColumnIndexOrThrow(_cursor, "est_defaut");
      final Categorie _result;
      if (_cursor.moveToFirst()) {
        final String _tmpNom;
        if (_cursor.isNull(_cursorIndexOfNom)) {
          _tmpNom = null;
        } else {
          _tmpNom = _cursor.getString(_cursorIndexOfNom);
        }
        final String _tmpIcone;
        if (_cursor.isNull(_cursorIndexOfIcone)) {
          _tmpIcone = null;
        } else {
          _tmpIcone = _cursor.getString(_cursorIndexOfIcone);
        }
        final String _tmpCouleur;
        if (_cursor.isNull(_cursorIndexOfCouleur)) {
          _tmpCouleur = null;
        } else {
          _tmpCouleur = _cursor.getString(_cursorIndexOfCouleur);
        }
        final boolean _tmpEst_defaut;
        final int _tmp;
        _tmp = _cursor.getInt(_cursorIndexOfEstDefaut);
        _tmpEst_defaut = _tmp != 0;
        _result = new Categorie(_tmpNom,_tmpIcone,_tmpCouleur,_tmpEst_defaut);
        _result.id = _cursor.getInt(_cursorIndexOfId);
      } else {
        _result = null;
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
