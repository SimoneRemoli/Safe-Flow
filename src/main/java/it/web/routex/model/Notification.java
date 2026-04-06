package it.web.routex.model;

import java.sql.Timestamp;

/**
 * Model di dominio che rappresenta una notifica del sistema.
 * Incapsula i dati persistenti associati a una comunicazione
 * (contenuto del messaggio, data di creazione e stato di risoluzione),
 * ed è utilizzato dallo strato applicativo per applicare la logica di business.
 * @author Lorenzo Brondi
 */


public class Notification {

    private final String message;
    private final Timestamp date;
    private boolean risolto;
    private boolean approvato;
    private boolean letto;
    private String status;
    private String senderRole;
    private String senderCf;
    private String recipientCf;
    private String city;
    private boolean pickpocketAlert;
    private boolean fightAlert;
    private boolean crowdAlert;
    private boolean generalAlert;
    private String stationName;
    private String suspectClothing;

    public Notification(String message, Timestamp date, boolean risolto) {
        this(message, date, risolto, true, true, "APPROVED", "ADMIN", null, null, null, false, false, false, false, null, null);
    }

    public Notification(String message, Timestamp date, boolean risolto, boolean approvato, String senderRole) {
        this(message, date, risolto, approvato, true, approvato ? "APPROVED" : "PENDING", senderRole, null, null, null, false, false, false, false, null, null);
    }

    public Notification(String message, Timestamp date, boolean risolto, boolean approvato, boolean letto, String senderRole, String senderCf, String recipientCf) {
        this(message, date, risolto, approvato, letto, approvato ? "APPROVED" : "PENDING", senderRole, senderCf, recipientCf, null, false, false, false, false, null, null);
    }

    public Notification(String message, Timestamp date, boolean risolto, boolean approvato, boolean letto, String status, String senderRole, String senderCf, String recipientCf, String city) {
        this(message, date, risolto, approvato, letto, status, senderRole, senderCf, recipientCf, city, false, false, false, false, null, null);
    }

    public Notification(String message, Timestamp date, boolean risolto, boolean approvato, boolean letto, String status, String senderRole, String senderCf, String recipientCf, String city, boolean pickpocketAlert, boolean fightAlert, boolean crowdAlert, boolean generalAlert, String stationName, String suspectClothing) {
        this.message = message;
        this.date = date;
        this.risolto = risolto;
        this.approvato = approvato;
        this.letto = letto;
        this.status = status;
        this.senderRole = senderRole;
        this.senderCf = senderCf;
        this.recipientCf = recipientCf;
        this.city = city;
        this.pickpocketAlert = pickpocketAlert;
        this.fightAlert = fightAlert;
        this.crowdAlert = crowdAlert;
        this.generalAlert = generalAlert;
        this.stationName = stationName;
        this.suspectClothing = suspectClothing;
    }

    public String getMessage() {
        return message;
    }

    public Timestamp getDate() {
        return date;
    }

    public boolean isRisolto() {
        return risolto;
    }
    public void setRisolto(boolean ris)
    {
        this.risolto = ris;
    }

    public boolean isApprovato() {
        return approvato;
    }

    public void setApprovato(boolean approvato) {
        this.approvato = approvato;
    }

    public boolean isLetto() {
        return letto;
    }

    public void setLetto(boolean letto) {
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

    public boolean isPickpocketAlert() {
        return pickpocketAlert;
    }

    public void setPickpocketAlert(boolean pickpocketAlert) {
        this.pickpocketAlert = pickpocketAlert;
    }

    public boolean isFightAlert() {
        return fightAlert;
    }

    public void setFightAlert(boolean fightAlert) {
        this.fightAlert = fightAlert;
    }

    public boolean isCrowdAlert() {
        return crowdAlert;
    }

    public void setCrowdAlert(boolean crowdAlert) {
        this.crowdAlert = crowdAlert;
    }

    public boolean isGeneralAlert() {
        return generalAlert;
    }

    public void setGeneralAlert(boolean generalAlert) {
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
