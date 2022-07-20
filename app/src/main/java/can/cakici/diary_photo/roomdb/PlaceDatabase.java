package can.cakici.diary_photo.roomdb;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import can.cakici.diary_photo.model.Place;

@Database(entities = {Place.class},version = 1)
public abstract class PlaceDatabase extends RoomDatabase {
    public abstract PlaceDao placeDao();
}
