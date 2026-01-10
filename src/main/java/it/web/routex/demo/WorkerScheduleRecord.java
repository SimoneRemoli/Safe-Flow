package it.web.routex.demo;

public class WorkerScheduleRecord {

    private final String codiceFiscale;
    private final int oraInizio;
    private final int oraFine;
    private final String luogoDiLavoro;

    public WorkerScheduleRecord(String codiceFiscale, int oraInizio, int oraFine, String luogoDiLavoro) {
        this.codiceFiscale = codiceFiscale;
        this.oraInizio = oraInizio;
        this.oraFine = oraFine;
        this.luogoDiLavoro = luogoDiLavoro;
    }

    public String getCodiceFiscale() {
        return codiceFiscale;
    }

    public int getOraInizio() {
        return oraInizio;
    }

    public int getOraFine() {
        return oraFine;
    }

    public String getLuogoDiLavoro() {
        return luogoDiLavoro;
    }
}
