package it.web.routex.controller.grafico;

import it.web.routex.bean.MessageBean;
import it.web.routex.controller.applicativo.SubmitTravelerCommunicationControllerApplicativo;
import it.web.routex.domain.LoggedHttpServlet;
import it.web.routex.exception.BrondiInvalidCommunicationInputException;
import it.web.routex.extractor.CommunicationInputExtractor;
import it.web.routex.record.CommunicationInput;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/submitTravelerCommunication")
public class SubmitTravelerCommunicationControllerGrafico extends LoggedHttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        try {
            CommunicationInput input = CommunicationInputExtractor.extract(request);

            MessageBean message = new MessageBean();
            message.setMessage(input.message());
            message.setDate(input.date());
            message.setCity(input.city());
            message.setPickpocketAlert(input.pickpocketAlert());
            message.setFightAlert(input.fightAlert());
            message.setCrowdAlert(input.crowdAlert());
            message.setGeneralAlert(input.generalAlert());
            message.setStationName(input.stationName());
            message.setSuspectClothing(input.suspectClothing());

            HttpSession session = request.getSession(false);
            if (session != null && session.getAttribute("codiceFiscale") != null) {
                message.setSenderCf(session.getAttribute("codiceFiscale").toString());
            }

            SubmitTravelerCommunicationControllerApplicativo controller =
                    new SubmitTravelerCommunicationControllerApplicativo();
            controller.submit(message);

            request.setAttribute("successTitle", "Report submitted");
            request.setAttribute("successMessage",
                    "Your message has been submitted and is now waiting for admin approval.");
            request.getRequestDispatcher("/successCommunication.jsp").forward(request, response);
        } catch (BrondiInvalidCommunicationInputException e) {
            request.setAttribute("errore", e.getMessage());
            try {
                request.getRequestDispatcher("/errorLogged.jsp").forward(request, response);
            } catch (Exception ex) {
                logger.error("Traveler communication forward error", ex);
            }
        } catch (Exception e) {
            request.setAttribute("errore", "Error while submitting the traveler report.");
            try {
                request.getRequestDispatcher("/errorLogged.jsp").forward(request, response);
            } catch (Exception ex) {
                logger.error("Traveler communication generic forward error", ex);
            }
        }
    }
}
