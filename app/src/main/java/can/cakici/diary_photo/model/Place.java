package can.cakici.diary_photo.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity
public class Place implements Serializable {

    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(typeAffinity = ColumnInfo.BLOB)
    public byte[] image;

    @ColumnInfo(name = "baslik")
    public String baslik;

    @ColumnInfo(name = "day")
    public int day;

    @ColumnInfo(name = "mounth")
    public int mounth;

    @ColumnInfo(name = "year")
    public int year;

    @ColumnInfo(name = "note")
    public String note;


    @ColumnInfo(name = "latitude")
    public double latitude;

    @ColumnInfo(name = "longitude")
    public double longitude;

    public Place(byte[] image, String baslik, int day, int mounth, int year, String note, double latitude, double longitude) {
        this.image = image;
        this.baslik = baslik;
        this.day = day;
        this.mounth = mounth;
        this.year = year;
        this.note = note;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
