package name.xrodney.amazris.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class StopDepartures {
    @JsonProperty("StopID")
    private int id;

    @JsonProperty("Message")
    private String message;

    @JsonProperty("PostList")
    private List<SignDepartures> departures;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<SignDepartures> getDepartures() {
        return departures;
    }

    public void setDepartures(List<SignDepartures> departures) {
        this.departures = departures;
    }
}
