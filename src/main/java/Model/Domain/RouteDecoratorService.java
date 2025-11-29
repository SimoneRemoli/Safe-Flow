package Model.Domain;

import Bean.InformazioniPercorsoBean;
import utility.Decorator.DecoratorChange.BaseComponent;
import utility.Decorator.DecoratorChange.CheckCambiamentiDecorator;
import utility.Decorator.DecoratorChange.Component;

import javax.servlet.http.HttpServletRequest;

public class RouteDecoratorService {

    public static void decorate(InformazioniPercorsoBean dto, HttpServletRequest request) {
        Component c = new CheckCambiamentiDecorator(new BaseComponent());

        request.setAttribute("percorsi", dto.getCityLife().getPercorsi_Con_Nomi());
        request.setAttribute("numero_cambi", dto.getCityLife().getNumero_cambi());
        request.setAttribute("linee", dto.getCityLife().getLinee());
        request.setAttribute("numero", dto.getNumero_stazioni_usate());
        request.setAttribute("minutaggio", dto.getMinutaggio());
        request.setAttribute("stazionitotali", dto.getCityLife().getNumero_stazioni_totali());
        request.setAttribute("suolometropolitano", dto.getPercentuale_stazioni_usate());

        request.setAttribute("listacambi",
                c.getChanges(dto.getCityLife().getSequenze_di_cambiamento()));

        request.setAttribute("nodicruciali",
                c.getChanges(dto.getCityLife().getSequenze_nodi_cruciali()));
    }
}
