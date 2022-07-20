package can.cakici.diary_photo.roomdb;

import android.database.Cursor;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.rxjava3.RxRoom;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import can.cakici.diary_photo.model.Place;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.lang.Void;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;

@SuppressWarnings({"unchecked", "deprecation"})
public final class PlaceDao_Impl implements PlaceDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<Place> __insertionAdapterOfPlace;

  private final EntityDeletionOrUpdateAdapter<Place> __deletionAdapterOfPlace;

  public PlaceDao_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfPlace = new EntityInsertionAdapter<Place>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR ABORT INTO `Place` (`id`,`image`,`baslik`,`day`,`mounth`,`year`,`note`,`latitude`,`longitude`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, Place value) {
        stmt.bindLong(1, value.id);
        if (value.image == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindBlob(2, value.image);
        }
        if (value.baslik == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, value.baslik);
        }
        stmt.bindLong(4, value.day);
        stmt.bindLong(5, value.mounth);
        stmt.bindLong(6, value.year);
        if (value.note == null) {
          stmt.bindNull(7);
        } else {
          stmt.bindString(7, value.note);
        }
        stmt.bindDouble(8, value.latitude);
        stmt.bindDouble(9, value.longitude);
      }
    };
    this.__deletionAdapterOfPlace = new EntityDeletionOrUpdateAdapter<Place>(__db) {
      @Override
      public String createQuery() {
        return "DELETE FROM `Place` WHERE `id` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, Place value) {
        stmt.bindLong(1, value.id);
      }
    };
  }

  @Override
  public Completable insert(final Place place) {
    return Completable.fromCallable(new Callable<Void>() {
      @Override
      public Void call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfPlace.insert(place);
          __db.setTransactionSuccessful();
          return null;
        } finally {
          __db.endTransaction();
        }
      }
    });
  }

  @Override
  public Completable delete(final Place place) {
    return Completable.fromCallable(new Callable<Void>() {
      @Override
      public Void call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfPlace.handle(place);
          __db.setTransactionSuccessful();
          return null;
        } finally {
          __db.endTransaction();
        }
      }
    });
  }

  @Override
  public Flowable<List<Place>> getAll() {
    final String _sql = "SELECT * FROM Place";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return RxRoom.createFlowable(__db, false, new String[]{"Place"}, new Callable<List<Place>>() {
      @Override
      public List<Place> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfImage = CursorUtil.getColumnIndexOrThrow(_cursor, "image");
          final int _cursorIndexOfBaslik = CursorUtil.getColumnIndexOrThrow(_cursor, "baslik");
          final int _cursorIndexOfDay = CursorUtil.getColumnIndexOrThrow(_cursor, "day");
          final int _cursorIndexOfMounth = CursorUtil.getColumnIndexOrThrow(_cursor, "mounth");
          final int _cursorIndexOfYear = CursorUtil.getColumnIndexOrThrow(_cursor, "year");
          final int _cursorIndexOfNote = CursorUtil.getColumnIndexOrThrow(_cursor, "note");
          final int _cursorIndexOfLatitude = CursorUtil.getColumnIndexOrThrow(_cursor, "latitude");
          final int _cursorIndexOfLongitude = CursorUtil.getColumnIndexOrThrow(_cursor, "longitude");
          final List<Place> _result = new ArrayList<Place>(_cursor.getCount());
          while(_cursor.moveToNext()) {
            final Place _item;
            final byte[] _tmpImage;
            if (_cursor.isNull(_cursorIndexOfImage)) {
              _tmpImage = null;
            } else {
              _tmpImage = _cursor.getBlob(_cursorIndexOfImage);
            }
            final String _tmpBaslik;
            if (_cursor.isNull(_cursorIndexOfBaslik)) {
              _tmpBaslik = null;
            } else {
              _tmpBaslik = _cursor.getString(_cursorIndexOfBaslik);
            }
            final int _tmpDay;
            _tmpDay = _cursor.getInt(_cursorIndexOfDay);
            final int _tmpMounth;
            _tmpMounth = _cursor.getInt(_cursorIndexOfMounth);
            final int _tmpYear;
            _tmpYear = _cursor.getInt(_cursorIndexOfYear);
            final String _tmpNote;
            if (_cursor.isNull(_cursorIndexOfNote)) {
              _tmpNote = null;
            } else {
              _tmpNote = _cursor.getString(_cursorIndexOfNote);
            }
            final double _tmpLatitude;
            _tmpLatitude = _cursor.getDouble(_cursorIndexOfLatitude);
            final double _tmpLongitude;
            _tmpLongitude = _cursor.getDouble(_cursorIndexOfLongitude);
            _item = new Place(_tmpImage,_tmpBaslik,_tmpDay,_tmpMounth,_tmpYear,_tmpNote,_tmpLatitude,_tmpLongitude);
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

  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
