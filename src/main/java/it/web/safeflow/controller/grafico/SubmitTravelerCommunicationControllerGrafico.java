package it.web.safeflow.controller.grafico;

import it.web.safeflow.bean.MessageBean;
import it.web.safeflow.controller.applicativo.ReportImageControllerApplicativo;
import it.web.safeflow.controller.applicativo.SubmitTravelerCommunicationControllerApplicativo;
import it.web.safeflow.domain.LoggedHttpServlet;
import it.web.safeflow.domain.SessionAuthUtil;
import it.web.safeflow.exception.BrondiException;
import it.web.safeflow.exception.BrondiInvalidCommunicationInputException;
import it.web.safeflow.extractor.CommunicationInputExtractor;
import it.web.safeflow.record.CommunicationInput;

import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;
import java.util.Collection;

@WebServlet("/submitTravelerCommunication")
@MultipartConfig(maxFileSize = 5 * 1024 * 1024, maxRequestSize = 28 * 1024 * 1024)
public class SubmitTravelerCommunicationControllerGrafico extends LoggedHttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        try {
            HttpSession session = request.getSession(false);
            if (!SessionAuthUtil.hasRole(session, "TRAVELER")) {
                redirectToLogin(request, response);
                return;
            }

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

	            message.setSenderCf(SessionAuthUtil.codiceFiscale(session).orElse(null));
	            Collection<Part> parts = request.getParts();
	            ReportImageControllerApplicativo reportImages = new ReportImageControllerApplicativo();
	            reportImages.validateImages(parts);

	            SubmitTravelerCommunicationControllerApplicativo controller =
	                    new SubmitTravelerCommunicationControllerApplicativo();
	            controller.submit(message);
	            reportImages.saveImages(message, parts);

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
	        } catch (BrondiException e) {
	            request.setAttribute("errore", e.getMessage());
	            try {
	                request.getRequestDispatcher("/errorLogged.jsp").forward(request, response);
	            } catch (Exception ex) {
	                logger.error("Traveler report image forward error", ex);
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
