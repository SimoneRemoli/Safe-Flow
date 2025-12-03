package it.web.routex.utility.Facade;
import it.web.routex.bean.CityLifeBean;
import it.web.routex.bean.InformazioniPercorsoBean;
import it.web.routex.bean.RoutingRequestBean;
import it.web.routex.controller.applicativo.CityLifeController;
import it.web.routex.exception.FuoriRangeExceptionRemoli;
import it.web.routex.exception.UnreacheableNodeExceptionRemoli;
import it.web.routex.utility.Factory.CityLifeFactory;

import java.sql.SQLException;
import java.util.ArrayList;

public class FacadePath
{
    public InformazioniPercorsoBean compute(RoutingRequestBean route) throws IllegalArgumentException, FuoriRangeExceptionRemoli, UnreacheableNodeExceptionRemoli, SQLException {
        CityLifeBean cityLife = RoutingProcess(route);
        return SettingProcess(cityLife);
    }
    private CityLifeBean RoutingProcess (RoutingRequestBean route) throws IllegalArgumentException, FuoriRangeExceptionRemoli, UnreacheableNodeExceptionRemoli, SQLException {
        CityLifeController controller = CityLifeFactory.createCity(route.getCity());

        ArrayList<Integer> path = controller.Dijkstra(
                route.getStartId(),
                route.getEndId()
        );
        return controller.calcolaPercorso(path, route.getCity());
    }
    private InformazioniPercorsoBean SettingProcess(CityLifeBean cityLife)
    {
        int numero_stazioni_usate = 0;
        Double minutaggio = 0.0, percentuale_stazioni_usate = 0.0, app = 0.0;
        InformazioniPercorsoBean trasferimento = new InformazioniPercorsoBean();
        numero_stazioni_usate = cityLife.getPercorsiConNomi().size();
        minutaggio = numero_stazioni_usate * 2.5;
        app = (double) numero_stazioni_usate / cityLife.getNumeroStazioniTotali();
        percentuale_stazioni_usate = (double) app * 100.0;
        trasferimento.setCityLife(cityLife);
        trasferimento.setMinutaggio(minutaggio);
        trasferimento.setNumeroStazioniUsate(numero_stazioni_usate);
        trasferimento.setPercentualeStazioniUsate(percentuale_stazioni_usate);
        return trasferimento;
    }
}
