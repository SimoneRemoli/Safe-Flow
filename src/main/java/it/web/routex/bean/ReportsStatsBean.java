package it.web.routex.bean;

import java.util.List;
import java.util.Set;

public class ReportsStatsBean {

    private int totalTrips;
    private double totalDistance;
    private double totalTime;
    private Set<String> utenti;
    private List<PathInfoBean> paths;

    public int getTotalTrips() {
        return totalTrips;
    }

    public void setTotalTrips(int totalTrips) {
        this.totalTrips = totalTrips;
    }

    public double getTotalDistance() {
        return totalDistance;
    }

    public void setTotalDistance(double totalDistance) {
        this.totalDistance = totalDistance;
    }

    public double getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(double totalTime) {
        this.totalTime = totalTime;
    }

    public Set<String> getUtenti() {
        return utenti;
    }

    public void setUtenti(Set<String> utenti) {
        this.utenti = utenti;
    }

    public List<PathInfoBean> getPaths() {
        return paths;
    }

    public void setPaths(List<PathInfoBean> paths) {
        this.paths = paths;
    }
}
