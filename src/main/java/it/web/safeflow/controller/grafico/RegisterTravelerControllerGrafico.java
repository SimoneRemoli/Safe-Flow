package it.web.safeflow.controller.grafico;

import it.web.safeflow.controller.applicativo.RegisterTravelerControllerApplicativo;
import it.web.safeflow.domain.LoggedHttpServlet;
import it.web.safeflow.exception.BrondiException;
import it.web.safeflow.exception.DAOExceptionRemoli;
import it.web.safeflow.extractor.RequestSanitizer;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/registerTraveler")
public class RegisterTravelerControllerGrafico extends LoggedHttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        forward(request, response, "/registerTraveler.jsp");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        RegisterTravelerControllerApplicativo controller = new RegisterTravelerControllerApplicativo();

        try {
            controller.register(
                    RequestSanitizer.sanitize(request.getParameter("firstName")),
                    RequestSanitizer.sanitize(request.getParameter("lastName")),
                    RequestSanitizer.sanitize(request.getParameter("taxCode")),
                    RequestSanitizer.sanitize(request.getParameter("email")),
                    RequestSanitizer.sanitize(request.getParameter("password")),
                    RequestSanitizer.sanitize(request.getParameter("birthDate")),
                    request.getParameter("disabled") != null
            );

            request.setAttribute("successTitle", "Account created");
            request.setAttribute("successMessage", "Your traveler account has been created. You can now access the reserved area.");
            request.setAttribute("successHomeTarget", "login.jsp");
            request.setAttribute("successHomeLabel", "Go to login");
            forward(request, response, "/successCommunication.jsp");
        } catch (BrondiException e) {
            logger.warn("Invalid traveler registration input: {}", e.getDetails());
            forwardError(request, response, e.getMessage(), "/errorNotLogged.jsp");
        } catch (DAOExceptionRemoli e) {
            logger.error("Traveler registration persistence error", e);
            forwardError(request, response, "Error while creating the traveler account.", "/errorNotLogged.jsp");
        } catch (Exception e) {
            logger.error("Unexpected traveler registration error", e);
            forwardError(request, response, "Unexpected error while creating the traveler account.", "/errorNotLogged.jsp");
        }
    }
}
