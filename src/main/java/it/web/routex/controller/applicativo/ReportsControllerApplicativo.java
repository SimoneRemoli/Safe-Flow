package it.web.routex.controller.applicativo;

import it.web.routex.bean.PathInfoBean;
import it.web.routex.bean.ReportsStatsBean;
import it.web.routex.dao.PathInfoDAO;
import it.web.routex.exception.DAOExceptionRemoli;
import it.web.routex.model.Route;

import java.util.*;

public class ReportsControllerApplicativo {

    public ReportsStatsBean recuperaStatistiche() throws DAOExceptionRemoli {

        PathInfoDAO dao = new PathInfoDAO();
        List<Route> models = dao.getAllPathInfo();

        ReportsStatsBean stats = new ReportsStatsBean();

        List<PathInfoBean> beans = new ArrayList<>();
        Set<String> utenti = new HashSet<>();

        double totalDistance = 0;
        double totalTime = 0;

        for (Route r : models) {

            PathInfoBean b = new PathInfoBean();
            b.setStartStation(r.getPartenza());
            b.setEndStation(r.getArrivo());
            b.setCity(r.getCitta());
            b.setTipoViaggiatore(r.getTipoViaggiatore());
            b.setNCambi(r.getnCambi());
            b.setListaCambi(r.getListaCambi());
            b.setStazioneDiInterscambio(r.getStazInterscambio());
            b.setNStazioniAttraversate(r.getnStazAttraversate());
            b.setTempoDiArrivo(r.getTempoDiArrivo());
            b.setNStazioniCitta(r.getnStazioniCitta());
            b.setPercTerrenoUtilizzato(r.getPercTerrenoUtilizzato());
            b.setUtente(r.getUtente());

            beans.add(b);
            utenti.add(r.getUtente());

            totalDistance += r.getPercTerrenoUtilizzato();
            totalTime += r.getTempoDiArrivo();
        }

        stats.setPaths(beans);
        stats.setUtenti(utenti);
        stats.setTotalTrips(beans.size());
        stats.setTotalDistance(totalDistance);
        stats.setTotalTime(totalTime);

        return stats;
    }
}
