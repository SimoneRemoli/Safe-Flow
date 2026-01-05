package it.web.routex.controller.grafico;

import it.web.routex.bean.MessageBean;
import it.web.routex.controller.applicativo.ConfirmCommunicationControllerApplicativo;
import it.web.routex.exception.BrondiInvalidCommunicationInputException;
import it.web.routex.extractor.CommunicationInputExtractor;
import it.web.routex.record.CommunicationInput;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/confirmCommunication")
public class ConfirmCommunicationControllerGrafico extends HttpServlet {

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

            // Chiamata applicativa
            ConfirmCommunicationControllerApplicativo service = new ConfirmCommunicationControllerApplicativo();

            service.communication(mess);

            // Successo
            request.setAttribute("successTitle", "Comunicazione inviata");
            request.setAttribute(
                    "successMessage",
                    "La comunicazione è stata correttamente inviata a tutti i lavoratori del sistema."
            );

            request.getRequestDispatcher("/successCommunication.jsp").forward(request, response);

        } catch (BrondiInvalidCommunicationInputException e) {

            // ERRORE DI INPUT (BOUNDARY)
            request.setAttribute("errore", e.getMessage());
            try {
                request.getRequestDispatcher("/adminError.jsp").forward(request, response);
            }catch(Exception d){
                throw new RuntimeException(d.getMessage());
            }

        } catch (Exception e) {
            // ERRORE TECNICO / APPLICATIVO
            request.setAttribute(
                    "errore",
                    "Errore durante l'invio della comunicazione."
            );
            try {
            request.getRequestDispatcher("/adminError.jsp").forward(request, response);
            }catch(Exception d){
                throw new RuntimeException(d.getMessage());
            }
        }
    }
}
