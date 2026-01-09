package it.web.routex.dao;

public class PathInit
{
    private String status;
    private String start;
    private String end;
    private String city;

    public String getCity() {
        return city;
    }

    public String getStart() {
        return start;
    }

    public String getStatus() {
        return status;
    }

    public String getEnd() {
        return end;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
