package it.web.routex.boundary.cli.domain;

import it.web.routex.bean.InformazioniPercorsoBean;
import it.web.routex.boundary.cli.view.PathNOREGCLI;
import it.web.routex.utility.decorator.decoratorchange.BaseComponent;
import it.web.routex.utility.decorator.decoratorchange.CheckCambiamentiDecorator;
import it.web.routex.utility.decorator.decoratorchange.Component;

public class RouteDecoratorServiceCLI {

    public static void decorate(InformazioniPercorsoBean dto)
    {
        Component c = new CheckCambiamentiDecorator(new BaseComponent());
        PathNOREGCLI path = new PathNOREGCLI(dto.getCityLife().getPercorsiConNomi(),dto.getCityLife().getNumeroCambi(),
                dto.getCityLife().getLinee(),dto.getNumeroStazioniUsate(),dto.getMinutaggio(),dto.getCityLife().getNumeroStazioniTotali(),
                dto.getPercentualeStazioniUsate(),c.getChanges(dto.getCityLife().getSequenzeDiCambiamento()),c.getChanges(dto.getCityLife().getSequenzeNodiCruciali()));

    }
}
