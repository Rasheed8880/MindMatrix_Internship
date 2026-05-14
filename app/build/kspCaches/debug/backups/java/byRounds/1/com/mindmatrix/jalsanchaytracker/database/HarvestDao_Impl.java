package com.mindmatrix.jalsanchaytracker.database;

import android.database.Cursor;
import androidx.annotation.NonNull;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import java.lang.Class;
import java.lang.Double;
import java.lang.Exception;
import java.lang.Long;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class HarvestDao_Impl implements HarvestDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<HarvestEntryEntity> __insertionAdapterOfHarvestEntryEntity;

  public HarvestDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfHarvestEntryEntity = new EntityInsertionAdapter<HarvestEntryEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `harvest_entries` (`id`,`roofAreaSqm`,`rainfallMm`,`runoffCoefficient`,`tankCapacityLiters`,`collectedLiters`,`createdAt`) VALUES (nullif(?, 0),?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final HarvestEntryEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindDouble(2, entity.getRoofAreaSqm());
        statement.bindDouble(3, entity.getRainfallMm());
        statement.bindDouble(4, entity.getRunoffCoefficient());
        statement.bindDouble(5, entity.getTankCapacityLiters());
        statement.bindDouble(6, entity.getCollectedLiters());
        statement.bindLong(7, entity.getCreatedAt());
      }
    };
  }

  @Override
  public Object insert(final HarvestEntryEntity entry,
      final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfHarvestEntryEntity.insertAndReturnId(entry);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<HarvestEntryEntity>> observeEntries() {
    final String _sql = "SELECT * FROM harvest_entries ORDER BY createdAt DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"harvest_entries"}, new Callable<List<HarvestEntryEntity>>() {
      @Override
      @NonNull
      public List<HarvestEntryEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfRoofAreaSqm = CursorUtil.getColumnIndexOrThrow(_cursor, "roofAreaSqm");
          final int _cursorIndexOfRainfallMm = CursorUtil.getColumnIndexOrThrow(_cursor, "rainfallMm");
          final int _cursorIndexOfRunoffCoefficient = CursorUtil.getColumnIndexOrThrow(_cursor, "runoffCoefficient");
          final int _cursorIndexOfTankCapacityLiters = CursorUtil.getColumnIndexOrThrow(_cursor, "tankCapacityLiters");
          final int _cursorIndexOfCollectedLiters = CursorUtil.getColumnIndexOrThrow(_cursor, "collectedLiters");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final List<HarvestEntryEntity> _result = new ArrayList<HarvestEntryEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final HarvestEntryEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final double _tmpRoofAreaSqm;
            _tmpRoofAreaSqm = _cursor.getDouble(_cursorIndexOfRoofAreaSqm);
            final double _tmpRainfallMm;
            _tmpRainfallMm = _cursor.getDouble(_cursorIndexOfRainfallMm);
            final double _tmpRunoffCoefficient;
            _tmpRunoffCoefficient = _cursor.getDouble(_cursorIndexOfRunoffCoefficient);
            final double _tmpTankCapacityLiters;
            _tmpTankCapacityLiters = _cursor.getDouble(_cursorIndexOfTankCapacityLiters);
            final double _tmpCollectedLiters;
            _tmpCollectedLiters = _cursor.getDouble(_cursorIndexOfCollectedLiters);
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            _item = new HarvestEntryEntity(_tmpId,_tmpRoofAreaSqm,_tmpRainfallMm,_tmpRunoffCoefficient,_tmpTankCapacityLiters,_tmpCollectedLiters,_tmpCreatedAt);
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
  public Flow<Double> observeTotalSaved() {
    final String _sql = "SELECT COALESCE(SUM(collectedLiters), 0) FROM harvest_entries";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"harvest_entries"}, new Callable<Double>() {
      @Override
      @NonNull
      public Double call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Double _result;
          if (_cursor.moveToFirst()) {
            final double _tmp;
            _tmp = _cursor.getDouble(0);
            _result = _tmp;
          } else {
            _result = 0.0;
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
  public Flow<Double> observeSavedBetween(final long start, final long end) {
    final String _sql = "SELECT COALESCE(SUM(collectedLiters), 0) FROM harvest_entries WHERE createdAt >= ? AND createdAt < ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, start);
    _argIndex = 2;
    _statement.bindLong(_argIndex, end);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"harvest_entries"}, new Callable<Double>() {
      @Override
      @NonNull
      public Double call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Double _result;
          if (_cursor.moveToFirst()) {
            final double _tmp;
            _tmp = _cursor.getDouble(0);
            _result = _tmp;
          } else {
            _result = 0.0;
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
