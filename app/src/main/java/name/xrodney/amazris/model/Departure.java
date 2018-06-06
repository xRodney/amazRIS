package name.xrodney.amazris.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Departure {

    @JsonProperty("EndStop")
    private String endStop;

    @JsonProperty("LF")
    private boolean lowFloor;

    @JsonProperty("Line")
    private String line;

    @JsonProperty("Time")
    private String time;

    public String getEndStop() {
        return endStop;
    }

    public void setEndStop(String endStop) {
        this.endStop = endStop;
    }

    public boolean isLowFloor() {
        return lowFloor;
    }

    public void setLowFloor(boolean lowFloor) {
        this.lowFloor = lowFloor;
    }

    public String getLine() {
        return line;
    }

    public void setLine(String line) {
        this.line = line;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
