package it.web.routex.utility.builder;

import it.web.routex.utility.builder.data.RouteData;
import it.web.routex.model.Route;

public class RouteBuilder {

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

    public RouteBuilder(String startStation) {
        this.startStation = startStation;
    }

    public RouteBuilder endStation(String endStation) {
        this.endStation = endStation;
        return this;
    }

    public RouteBuilder city(String city) {
        this.city = city;
        return this;
    }

    public RouteBuilder tipoViaggiatore(String tipoViaggiatore) {
        this.tipoViaggiatore = tipoViaggiatore;
        return this;
    }

    public RouteBuilder nCambi(int nCambi) {
        this.nCambi = nCambi;
        return this;
    }

    public RouteBuilder listaCambi(String listaCambi) {
        this.listaCambi = listaCambi;
        return this;
    }

    public RouteBuilder stazioneDiInterscambio(String stazioneDiInterscambio) {
        this.stazioneDiInterscambio = stazioneDiInterscambio;
        return this;
    }

    public RouteBuilder nStazioniAttraversate(int nStazioniAttraversate) {
        this.nStazioniAttraversate = nStazioniAttraversate;
        return this;
    }

    public RouteBuilder tempoDiArrivo(double tempoDiArrivo) {
        this.tempoDiArrivo = tempoDiArrivo;
        return this;
    }

    public RouteBuilder nStazioniCitta(int nStazioniCitta) {
        this.nStazioniCitta = nStazioniCitta;
        return this;
    }

    public RouteBuilder percTerrenoUtilizzato(double percTerrenoUtilizzato) {
        this.percTerrenoUtilizzato = percTerrenoUtilizzato;
        return this;
    }

    public RouteBuilder utente(String utente) {
        this.utente = utente;
        return this;
    }

    public Route build()
    {
        RouteData data = new RouteData();
        data.setCity(city);
        data.setEndStation(endStation);
        data.setListaCambi(listaCambi);
        data.setnCambi(nCambi);
        data.setnStazioniCitta(nStazioniCitta);
        data.setnStazioniAttraversate(nStazioniAttraversate);
        data.setPercTerrenoUtilizzato(percTerrenoUtilizzato);
        data.setTempoDiArrivo(tempoDiArrivo);
        data.setTipoViaggiatore(tipoViaggiatore);
        data.setUtente(utente);
        data.setStartStation(startStation);
        data.setStazioneDiInterscambio(stazioneDiInterscambio);
        return new Route(data);
    }
}
