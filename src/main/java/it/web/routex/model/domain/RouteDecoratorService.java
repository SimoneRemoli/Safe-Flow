package it.web.routex.model.domain;

import it.web.routex.bean.InformazioniPercorsoBean;
import it.web.routex.utility.Decorator.DecoratorChange.BaseComponent;
import it.web.routex.utility.Decorator.DecoratorChange.CheckCambiamentiDecorator;
import it.web.routex.utility.Decorator.DecoratorChange.Component;

import javax.servlet.http.HttpServletRequest;

public class RouteDecoratorService {

    public static void decorate(InformazioniPercorsoBean dto, HttpServletRequest request) {
        Component c = new CheckCambiamentiDecorator(new BaseComponent());

        request.setAttribute("percorsi", dto.getCityLife().getPercorsiConNomi());
        request.setAttribute("numero_cambi", dto.getCityLife().getNumeroCambi());
        request.setAttribute("linee", dto.getCityLife().getLinee());
        request.setAttribute("numero", dto.getNumeroStazioniUsate());
        request.setAttribute("minutaggio", dto.getMinutaggio());
        request.setAttribute("stazionitotali", dto.getCityLife().getNumeroStazioniTotali());
        request.setAttribute("suolometropolitano", dto.getPercentualeStazioniUsate());

        request.setAttribute("listacambi",
                c.getChanges(dto.getCityLife().getSequenzeDiCambiamento()));

        request.setAttribute("nodicruciali",
                c.getChanges(dto.getCityLife().getSequenzeNodiCruciali()));
    }
}
