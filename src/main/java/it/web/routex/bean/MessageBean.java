package it.web.routex.bean;

import java.sql.Timestamp;

public class MessageBean
{
    private String message;
    private Timestamp date;
    private Boolean risolto;

    public MessageBean(){}

    public MessageBean(String m, Timestamp d)
    {
        this.message = m;
        this.date = d;
    }

    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }

    public Timestamp getDate() {
        return date;
    }
    public void setDate(Timestamp date) {
        this.date = date;
    }

    public Boolean getRisolto() {
        return risolto;
    }
    public void setRisolto(Boolean risolto) {
        this.risolto = risolto;
    }

}