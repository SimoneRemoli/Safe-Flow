package it.web.routex.bean;

import java.io.Serializable;

public class WorkerScheduleBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private String codiceFiscale;
    private Integer oraInizio;
    private Integer oraFine;
    private String luogoDiLavoro;

    // Costruttore vuoto
    public WorkerScheduleBean(Integer oraInizio, Integer oraFine, String luogoDiLavoro) {
        this.oraInizio = oraInizio;
        this.oraFine = oraFine;
        this.luogoDiLavoro = luogoDiLavoro;
    }

    // Getter e Setter
    public String getCodiceFiscale() {
        return codiceFiscale;
    }

    public void setCodiceFiscale(String codiceFiscale) {
        this.codiceFiscale = codiceFiscale;
    }

    public Integer getOraInizio() {
        return oraInizio;
    }

    public void setOraInizio(Integer oraInizio) {
        this.oraInizio = oraInizio;
    }

    public Integer getOraFine() {
        return oraFine;
    }

    public void setOraFine(Integer oraFine) {
        this.oraFine = oraFine;
    }

    public String getLuogoDiLavoro() {
        return luogoDiLavoro;
    }

    public void setLuogoDiLavoro(String luogoDiLavoro) {
        this.luogoDiLavoro = luogoDiLavoro;
    }

}