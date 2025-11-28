package utility.Facade;
import Bean.CityLifeBean;
import Bean.InformazioniPercorsoBean;
import Bean.RoutingRequestBean;
import Controller.Applicativo.CityLifeController;
import utility.Factory.CityLifeFactory;
import java.util.ArrayList;

public class Facade
{
    public InformazioniPercorsoBean compute(RoutingRequestBean route) throws Exception {
        CityLifeBean cityLife = RoutingProcess(route);
        return SettingProcess(cityLife);
    }

    private CityLifeBean RoutingProcess (RoutingRequestBean route) throws Exception {
        CityLifeController controller = CityLifeFactory.createCity(route.getCity());

        ArrayList<Integer> path = controller.Dijkstra(
                route.getStartId(),
                route.getEndId(),
                route.getCity()
        );
        return controller.calcolaPercorso(path, route.getCity());
    }
    private InformazioniPercorsoBean SettingProcess(CityLifeBean cityLife)
    {
        int numero_stazioni_usate = 0;
        Double minutaggio = 0.0, percentuale_stazioni_usate = 0.0, app = 0.0;

        InformazioniPercorsoBean trasferimento = new InformazioniPercorsoBean();
        numero_stazioni_usate = cityLife.getPercorsi_Con_Nomi().size();
        minutaggio = numero_stazioni_usate * 2.5;
        app = (double) numero_stazioni_usate / cityLife.getNumero_stazioni_totali();
        percentuale_stazioni_usate = (double) app * 100.0;
        trasferimento.setCityLife(cityLife);
        trasferimento.setMinutaggio(minutaggio);
        trasferimento.setNumero_stazioni_usate(numero_stazioni_usate);
        trasferimento.setPercentuale_stazioni_usate(percentuale_stazioni_usate);
        trasferimento.setPercentuale_stazioni_usate(percentuale_stazioni_usate);

        return trasferimento;

    }
}
