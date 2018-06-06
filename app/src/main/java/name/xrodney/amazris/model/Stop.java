package name.xrodney.amazris.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Stop {
    //http://iris.bmhd.cz/api/stops.json

    @JsonProperty("Name")
    private String name;

    @JsonProperty("Zone")
    private String zone;

    @JsonProperty("Lat")
    private Double lat;

    @JsonProperty("Lng")
    private Double lng;

    @JsonProperty("Public")
    private boolean isPublic;
    private int id;

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
