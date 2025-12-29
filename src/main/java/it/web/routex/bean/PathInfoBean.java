package it.web.routex.bean;

public class PathInfoBean {
    private String startStation, endStation, city, tipoViaggiatore, listaCambi, stazioneDiInterscambio, utente;
    private int nCambi, nStazioniAttraversate, nStazioniCitta;
    private double tempoDiArrivo, percTerrenoUtilizzato;

    // --- getter e setter ---
    public String getStartStation() { return startStation; }
    public void setStartStation(String startStation) { this.startStation = startStation; }

    public String getEndStation() { return endStation; }
    public void setEndStation(String endStation) { this.endStation = endStation; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public String getTipoViaggiatore() { return tipoViaggiatore; }
    public void setTipoViaggiatore(String tipoViaggiatore) { this.tipoViaggiatore = tipoViaggiatore; }

    public int getNCambi() { return nCambi; }
    public void setNCambi(int nCambi) { this.nCambi = nCambi; }

    public String getListaCambi() { return listaCambi; }
    public void setListaCambi(String listaCambi) { this.listaCambi = listaCambi; }

    public String getStazioneDiInterscambio() { return stazioneDiInterscambio; }
    public void setStazioneDiInterscambio(String stazioneDiInterscambio) { this.stazioneDiInterscambio = stazioneDiInterscambio; }

    public int getNStazioniAttraversate() { return nStazioniAttraversate; }
    public void setNStazioniAttraversate(int nStazioniAttraversate) { this.nStazioniAttraversate = nStazioniAttraversate; }

    public double getTempoDiArrivo() { return tempoDiArrivo; }
    public void setTempoDiArrivo(double tempoDiArrivo) { this.tempoDiArrivo = tempoDiArrivo; }

    public int getNStazioniCitta() { return nStazioniCitta; }
    public void setNStazioniCitta(int nStazioniCitta) { this.nStazioniCitta = nStazioniCitta; }

    public double getPercTerrenoUtilizzato() { return percTerrenoUtilizzato; }
    public void setPercTerrenoUtilizzato(double percTerrenoUtilizzato) { this.percTerrenoUtilizzato = percTerrenoUtilizzato; }

    public String getUtente() { return utente; }
    public void setUtente(String utente) { this.utente = utente; }
}