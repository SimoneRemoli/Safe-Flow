package it.web.routex.bean;

import java.sql.Timestamp;

public class MessageBean
{
    private String message;
    private Timestamp date;
    private Boolean risolto;
    private Boolean approvato;
    private Boolean letto;
    private String status;
    private String senderRole;
    private String senderCf;
    private String recipientCf;
    private String city;
    private Boolean pickpocketAlert;
    private Boolean fightAlert;
    private Boolean crowdAlert;
    private Boolean generalAlert;
    private String stationName;
    private String suspectClothing;

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

    public Boolean getApprovato() {
        return approvato;
    }

    public void setApprovato(Boolean approvato) {
        this.approvato = approvato;
    }

    public Boolean getLetto() {
        return letto;
    }

    public void setLetto(Boolean letto) {
        this.letto = letto;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSenderRole() {
        return senderRole;
    }

    public void setSenderRole(String senderRole) {
        this.senderRole = senderRole;
    }

    public String getSenderCf() {
        return senderCf;
    }

    public void setSenderCf(String senderCf) {
        this.senderCf = senderCf;
    }

    public String getRecipientCf() {
        return recipientCf;
    }

    public void setRecipientCf(String recipientCf) {
        this.recipientCf = recipientCf;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Boolean getPickpocketAlert() {
        return pickpocketAlert;
    }

    public void setPickpocketAlert(Boolean pickpocketAlert) {
        this.pickpocketAlert = pickpocketAlert;
    }

    public Boolean getFightAlert() {
        return fightAlert;
    }

    public void setFightAlert(Boolean fightAlert) {
        this.fightAlert = fightAlert;
    }

    public Boolean getCrowdAlert() {
        return crowdAlert;
    }

    public void setCrowdAlert(Boolean crowdAlert) {
        this.crowdAlert = crowdAlert;
    }

    public Boolean getGeneralAlert() {
        return generalAlert;
    }

    public void setGeneralAlert(Boolean generalAlert) {
        this.generalAlert = generalAlert;
    }

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    public String getSuspectClothing() {
        return suspectClothing;
    }

    public void setSuspectClothing(String suspectClothing) {
        this.suspectClothing = suspectClothing;
    }

}
