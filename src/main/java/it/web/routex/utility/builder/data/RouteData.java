package it.web.routex.utility.builder.data;

public class RouteData {

    private String startStation;
    private String endStation;
    private String city;
    private String tipoViaggiatore;
    private int nCambi;
    private String listaCambi;
    private String stazioneDiInterscambio;
    private int nStazioniAttraversate;
    private double tempoDiArrivo;
    private int nStazioniCitta;
    private double percTerrenoUtilizzato;
    private String utente;

    public String getCity() {
        return city;
    }

    public String getListaCambi() {
        return listaCambi;
    }

    public int getnStazioniCitta() {
        return nStazioniCitta;
    }

    public int getnCambi() {
        return nCambi;
    }

    public double getPercTerrenoUtilizzato() {
        return percTerrenoUtilizzato;
    }

    public double getTempoDiArrivo() {
        return tempoDiArrivo;
    }

    public int getnStazioniAttraversate() {
        return nStazioniAttraversate;
    }

    public String getEndStation() {
        return endStation;
    }

    public String getStartStation() {
        return startStation;
    }

    public String getStazioneDiInterscambio() {
        return stazioneDiInterscambio;
    }

    public String getTipoViaggiatore() {
        return tipoViaggiatore;
    }

    public String getUtente() {
        return utente;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setTempoDiArrivo(double tempoDiArrivo) {
        this.tempoDiArrivo = tempoDiArrivo;
    }

    public void setnStazioniCitta(int nStazioniCitta) {
        this.nStazioniCitta = nStazioniCitta;
    }

    public void setPercTerrenoUtilizzato(double percTerrenoUtilizzato) {
        this.percTerrenoUtilizzato = percTerrenoUtilizzato;
    }

    public void setnCambi(int nCambi) {
        this.nCambi = nCambi;
    }

    public void setListaCambi(String listaCambi) {
        this.listaCambi = listaCambi;
    }

    public void setEndStation(String endStation) {
        this.endStation = endStation;
    }

    public void setnStazioniAttraversate(int nStazioniAttraversate) {
        this.nStazioniAttraversate = nStazioniAttraversate;
    }

    public void setStartStation(String startStation) {
        this.startStation = startStation;
    }

    public void setStazioneDiInterscambio(String stazioneDiInterscambio) {
        this.stazioneDiInterscambio = stazioneDiInterscambio;
    }

    public void setTipoViaggiatore(String tipoViaggiatore) {
        this.tipoViaggiatore = tipoViaggiatore;
    }

    public void setUtente(String utente) {
        this.utente = utente;
    }
}

