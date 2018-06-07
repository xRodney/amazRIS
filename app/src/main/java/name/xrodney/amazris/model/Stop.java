package name.xrodney.amazris.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
public class Stop {
    //http://iris.bmhd.cz/api/stops.json

    @PrimaryKey
    private int id;

    @JsonProperty("Name")
    @ColumnInfo
    private String name;

    @JsonProperty("Zone")
    @ColumnInfo
    private String zone;

    @JsonProperty("Lat")
    @ColumnInfo
    private Double lat;

    @JsonProperty("Lng")
    @ColumnInfo
    private Double lng;

    @JsonProperty("Public")
    @ColumnInfo
    private boolean isPublic;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getZone() {
        return zone;
    }

    public void setZone(String zone) {
        this.zone = zone;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public void setPublic(boolean aPublic) {
        isPublic = aPublic;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
