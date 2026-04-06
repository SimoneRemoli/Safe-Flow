package it.web.routex.controller.grafico;

import it.web.routex.bean.MessageBean;
import it.web.routex.controller.applicativo.ConfirmCommunicationControllerApplicativo;
import it.web.routex.domain.LoggedHttpServlet;
import it.web.routex.exception.BrondiInvalidCommunicationInputException;
import it.web.routex.extractor.CommunicationInputExtractor;
import it.web.routex.record.CommunicationInput;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/confirmCommunication")
public class ConfirmCommunicationControllerGrafico extends LoggedHttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    {
        try {
            //Estrazione + validazione input
            CommunicationInput input = CommunicationInputExtractor.extract(request);

            // Bean di trasporto
            MessageBean mess = new MessageBean();
            mess.setMessage(input.message());
            mess.setDate(input.date());
            mess.setCity(input.city());
            mess.setPickpocketAlert(input.pickpocketAlert());
            mess.setFightAlert(input.fightAlert());
            mess.setCrowdAlert(input.crowdAlert());
            mess.setGeneralAlert(input.generalAlert());
            mess.setStationName(input.stationName());
            mess.setSuspectClothing(input.suspectClothing());

            // Chiamata applicativa
            ConfirmCommunicationControllerApplicativo service = new ConfirmCommunicationControllerApplicativo();

            service.communication(mess);

            // Successo
            request.setAttribute("successTitle", "Report published");
            request.setAttribute(
                    "successMessage",
                    "Your report has been published successfully and is now visible to all users on the platform."
            );
            request.setAttribute("successHomeLabel", "Back to Admin Hub");
            request.setAttribute("successHomeTarget", "adminHub");

            request.getRequestDispatcher("/successCommunication.jsp").forward(request, response);

        } catch (BrondiInvalidCommunicationInputException e) {

            // ERRORE DI INPUT (BOUNDARY)
            request.setAttribute("errore", e.getMessage());
            try {
                request.getRequestDispatcher("/adminError.jsp").forward(request, response);
            }catch(Exception d){
                logger.error("Errore specifico nel forwarding verso adminError.jsp : {} ", e.getMessage());
            }

        } catch (Exception e) {
            // ERRORE TECNICO / APPLICATIVO
            request.setAttribute(
                    "errore",
                    "Error while publishing the report."
            );
            try {
            request.getRequestDispatcher("/adminError.jsp").forward(request, response);
            }catch(Exception d){
                logger.error("Errore generico nel forwarding verso adminError.jsp : {} ", e.getMessage());

            }
        }
    }
}
