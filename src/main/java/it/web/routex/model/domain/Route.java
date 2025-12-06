package it.web.routex.model.domain;
import it.web.routex.exception.InvalidRouteException;
import it.web.routex.utility.singleton.Credentials;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

public class Route {

    private String partenza;
    private String arrivo;
    private String citta;
    private String tipoViaggiatore;
    private int nCambi;
    private String listaCambi;
    private String stazInterscambio;
    private int nStazAttraversate;
    private Double tempoDiArrivo;
    private int nStazioniCitta;
    private Double percTerrenoUtilizzato;
    private String utente;


    @SuppressWarnings("unchecked")
    public Route(HttpServletRequest request) throws InvalidRouteException {
        Credentials cred = Credentials.getInstanceSingleton();

        try {
            // Conversione sicura di listacambi
            Object objListaCambi = request.getAttribute("listacambi");
            List<String> listaCambiList = new ArrayList<>();

            if (objListaCambi instanceof List) {
                listaCambiList = (List<String>) objListaCambi;
            } else if (objListaCambi instanceof String string) {
                listaCambiList.add(string);
            }
            this.listaCambi = String.join(", ", listaCambiList);

            // Conversione sicura di nodicruciali
            Object objStazInterscambio = request.getAttribute("nodicruciali");
            List<String> stazInterList = new ArrayList<>();
            if (objStazInterscambio instanceof List) {
                stazInterList = (List<String>) objStazInterscambio;
            } else if (objStazInterscambio instanceof String string) {
                stazInterList.add(string);
            }

            this.stazInterscambio = String.join(", ", stazInterList);

            this.partenza = (String) request.getAttribute("inizio");
            this.arrivo = (String) request.getAttribute("fine");
            this.citta = (String) request.getAttribute("city");
            this.tipoViaggiatore = (String) request.getAttribute("status");
            this.nCambi = (int) request.getAttribute("numero_cambi");
            this.nStazAttraversate = (int) request.getAttribute("numero");
            this.tempoDiArrivo = (Double) request.getAttribute("minutaggio");
            this.nStazioniCitta = (int) request.getAttribute("stazionitotali");
            this.percTerrenoUtilizzato = (Double) request.getAttribute("suolometropolitano");
            this.utente = cred.getCodiceFiscale();

        } catch (NullPointerException | ClassCastException e) {
            throw new InvalidRouteException(
                    "Errore nei dati della richiesta per la creazione della Route", e
            );
        }
    }


    public Route() {

    }

    @SuppressWarnings("unchecked")
    public static String convertListObjectToString(Object obj) {
        if (obj instanceof List) {
            List<?> tempList = (List<?>) obj;
            if (!tempList.isEmpty() && tempList.get(0) instanceof String) {
                List<String> lista = (List<String>) tempList;
                return String.join(", ", lista);
            }
        } else if (obj instanceof String string) {
            return string;
        }
        return "";
    }

    public String getPartenza() {
        return partenza;
    }

    public void setPartenza(String partenza) {
        this.partenza = partenza;
    }

    public String getArrivo() {
        return arrivo;
    }

    public void setArrivo(String arrivo) {
        this.arrivo = arrivo;
    }

    public String getCitta() {
        return citta;
    }

    public void setCitta(String citta) {
        this.citta = citta;
    }

    public String getTipoViaggiatore() {
        return tipoViaggiatore;
    }

    public void setTipoViaggiatore(String tipoViaggiatore) {
        this.tipoViaggiatore = tipoViaggiatore;
    }

    public int getnCambi() {
        return nCambi;
    }

    public void setnCambi(int nCambi) {
        this.nCambi = nCambi;
    }

    public String getListaCambi() {
        return listaCambi;
    }

    public void setListaCambi(String listaCambi) {
        this.listaCambi = listaCambi;
    }

    public String getStazInterscambio() {
        return stazInterscambio;
    }

    public void setStazInterscambio(String stazInterscambio) {
        this.stazInterscambio = stazInterscambio;
    }

    public int getnStazAttraversate() {
        return nStazAttraversate;
    }

    public void setnStazAttraversate(int nStazAttraversate) {
        this.nStazAttraversate = nStazAttraversate;
    }

    public Double getTempoDiArrivo() {
        return tempoDiArrivo;
    }

    public void setTempoDiArrivo(Double tempoDiArrivo) {
        this.tempoDiArrivo = tempoDiArrivo;
    }

    public int getnStazioniCitta() {
        return nStazioniCitta;
    }

    public void setnStazioniCitta(int nStazioniCitta) {
        this.nStazioniCitta = nStazioniCitta;
    }

    public Double getPercTerrenoUtilizzato() {
        return percTerrenoUtilizzato;
    }

    public void setPercTerrenoUtilizzato(Double percTerrenoUtilizzato) {
        this.percTerrenoUtilizzato = percTerrenoUtilizzato;
    }

    public String getUtente() {
        return utente;
    }

    public void setUtente(String utente) {
        this.utente = utente;
    }
}
