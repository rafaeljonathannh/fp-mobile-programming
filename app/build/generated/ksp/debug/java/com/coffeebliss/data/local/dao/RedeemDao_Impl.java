package com.coffeebliss.data.local.dao;

import android.database.Cursor;
import androidx.annotation.NonNull;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.coffeebliss.data.local.entity.Redeem;
import java.lang.Class;
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
public final class RedeemDao_Impl implements RedeemDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<Redeem> __insertionAdapterOfRedeem;

  public RedeemDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfRedeem = new EntityInsertionAdapter<Redeem>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `redeems` (`id`,`memberId`,`rewardName`,`pointsUsed`,`redeemedAt`) VALUES (nullif(?, 0),?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Redeem entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getMemberId());
        statement.bindString(3, entity.getRewardName());
        statement.bindLong(4, entity.getPointsUsed());
        statement.bindLong(5, entity.getRedeemedAt());
      }
    };
  }

  @Override
  public Object insert(final Redeem redeem, final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfRedeem.insertAndReturnId(redeem);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<Redeem>> getRedeemsByMember(final int memberId) {
    final String _sql = "SELECT * FROM redeems WHERE memberId = ? ORDER BY redeemedAt DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, memberId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"redeems"}, new Callable<List<Redeem>>() {
      @Override
      @NonNull
      public List<Redeem> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfMemberId = CursorUtil.getColumnIndexOrThrow(_cursor, "memberId");
          final int _cursorIndexOfRewardName = CursorUtil.getColumnIndexOrThrow(_cursor, "rewardName");
          final int _cursorIndexOfPointsUsed = CursorUtil.getColumnIndexOrThrow(_cursor, "pointsUsed");
          final int _cursorIndexOfRedeemedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "redeemedAt");
          final List<Redeem> _result = new ArrayList<Redeem>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Redeem _item;
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final int _tmpMemberId;
            _tmpMemberId = _cursor.getInt(_cursorIndexOfMemberId);
            final String _tmpRewardName;
            _tmpRewardName = _cursor.getString(_cursorIndexOfRewardName);
            final int _tmpPointsUsed;
            _tmpPointsUsed = _cursor.getInt(_cursorIndexOfPointsUsed);
            final long _tmpRedeemedAt;
            _tmpRedeemedAt = _cursor.getLong(_cursorIndexOfRedeemedAt);
            _item = new Redeem(_tmpId,_tmpMemberId,_tmpRewardName,_tmpPointsUsed,_tmpRedeemedAt);
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
