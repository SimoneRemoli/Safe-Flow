package Bean;

public class RoutingRequestBean {
    private String city;
    private int startId;
    private int endId;

    public int getEndId() {
        return endId;
    }
    public void setEndId(int endId) {
        this.endId = endId;
    }
    public String getCity() {
        return city;
    }
    public void setCity(String city) {
        this.city = city;
    }
    public int getStartId() {
        return startId;
    }
    public void setStartId(int startId) {
        this.startId = startId;
    }
}