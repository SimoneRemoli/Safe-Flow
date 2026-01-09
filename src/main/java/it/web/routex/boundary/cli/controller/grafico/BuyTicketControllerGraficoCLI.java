package it.web.routex.boundary.cli.controller.grafico;

import it.web.routex.bean.CityBean;
import it.web.routex.bean.PrezzoTotaleBean;
import it.web.routex.boundary.cli.LoggedCLI;
import it.web.routex.boundary.cli.extractor.BuyTicketExtractorCLI;
import it.web.routex.boundary.cli.view.BuyTicketCLI;
import it.web.routex.boundary.cli.view.ConfermaPagamentoCLI;
import it.web.routex.boundary.cli.view.GenericErrorCLI;
import it.web.routex.controller.applicativo.CityController;
import it.web.routex.exception.DAOExceptionRemoli;
import it.web.routex.exception.InvalidBuyTicketInputExceptionRemoli;
import it.web.routex.exception.InvalidCityDataExceptionRemoli;
import it.web.routex.exception.InvalidPriceCalculationExceptionRemoli;
import it.web.routex.record.BuyTicketRecord;

import java.util.List;

public class BuyTicketControllerGraficoCLI extends LoggedCLI
{
    public void doGet()
    {

        try {
            // Recupero delle città tramite il controller applicativo
            CityController cityController = new CityController();
            List<CityBean> cities = cityController.getAllCities();

            // Passo la lista alla view

            forwardToBuyTicket(cities);

        } catch (DAOExceptionRemoli e) {
            GenericErrorCLI.mostraErrore(e.getMessage());
        } catch (InvalidCityDataExceptionRemoli e) {
            GenericErrorCLI.mostraErrore(e.getUserMessage());
            logger.error("Errore nei dati delle città: {}", e.toString());
        }

    }

    /**
     * Gestisce la richiesta di acquisto di uno o più biglietti.
     * Calcola il prezzo totale e inoltra alla pagina di conferma pagamento.
     */
    public void doPost(String city, String quantity) {

        BuyTicketRecord buyTicket;

        buyTicket = estraiBuyTicket(city,quantity);

        logger.info("Elaborazione richiesta acquisto biglietti per città='{}', quantità={}", buyTicket.city(), buyTicket.quantity());

        try {
            // Delego al controller applicativo il calcolo del prezzo totale
            CityController cityController = new CityController();
            PrezzoTotaleBean prezzo = cityController.ottieniPrezzoTotale(buyTicket.city(), buyTicket.quantity());
            logger.info("Elaborazione prezzo: prezzo={}",prezzo.getPrezzoTotale());


            new ConfermaPagamentoCLI(buyTicket.city(),String.valueOf(buyTicket.quantity()),prezzo.getPrezzoTotale() );
            forwardingConferma();

        } catch (DAOExceptionRemoli e) {
            GenericErrorCLI.mostraErrore("Errore durante l'elaborazione dell'acquisto: " + e.getMessage());
            logger.error("Errore nella DAO {}. ", e.getMessage());
        } catch (InvalidPriceCalculationExceptionRemoli e) {
            GenericErrorCLI.mostraErrore("Errore nei dati inseriti " + e.getUserMessage());
        }
    }

    private BuyTicketRecord estraiBuyTicket(String city, String quantity)
    {
        try {
            return BuyTicketExtractorCLI.from(city,quantity);
        } catch (InvalidBuyTicketInputExceptionRemoli e) {
            logger.error("Errore di validazione input nell'acquisto biglietti", e);
            GenericErrorCLI.mostraErrore("Errore di validazione input nell'acquisto biglietti" + e.getUserMessage());
            return null;
        }
    }
    private void forwardingConferma()
    {
        ConfermaPagamentoCLI.stampa();
    }

    private void forwardToBuyTicket(List<CityBean> cities) {
        try {
            BuyTicketCLI.mostraAcquisto(cities);
            logger.info("[CLI]Visualizzata la pagina di acquisto biglietti con size={} città disponibili.", cities.size());
        } catch (Exception e) {
            logger.error("[CLI]Errore nella visualizzazione della pagina di acquisto biglietti.", e);
        }
    }
}
