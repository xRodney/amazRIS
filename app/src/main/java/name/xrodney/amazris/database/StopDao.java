package name.xrodney.amazris.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import name.xrodney.amazris.model.Stop;

@Dao
public interface StopDao {
    @Query("SELECT * FROM stop")
    List<Stop> getAll();

    @Query("SELECT * FROM stop WHERE abs(lat - :lat) < :radius AND abs(lng - :lng) < :radius")
    List<Stop> getNearby(double lat, double lng, double radius);

    @Insert
    void insertAll(List<Stop> stops);

    @Delete
    void deleteAll(List<Stop> stops);

    @Query("SELECT count(*) FROM stop")
    int count();
}
