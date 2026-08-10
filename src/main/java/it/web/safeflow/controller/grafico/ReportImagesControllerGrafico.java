package it.web.safeflow.controller.grafico;

import it.web.safeflow.controller.applicativo.ReportImageControllerApplicativo;
import it.web.safeflow.domain.LoggedHttpServlet;
import it.web.safeflow.domain.SessionAuthUtil;
import it.web.safeflow.exception.BrondiException;
import it.web.safeflow.model.ReportImageAttachment;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;

@WebServlet("/reportImages")
public class ReportImagesControllerGrafico extends LoggedHttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        if (!SessionAuthUtil.isLoggedIn(session)) {
            redirectToLogin(request, response);
            return;
        }

        String notificationKey = request.getParameter("notificationKey");
        String codiceFiscale = SessionAuthUtil.codiceFiscale(session).orElse(null);
        String ruolo = SessionAuthUtil.ruolo(session).orElse("");

        try {
            ReportImageControllerApplicativo controller = new ReportImageControllerApplicativo();
            List<ReportImageAttachment> images = controller.viewableImages(notificationKey, codiceFiscale, ruolo);
            request.setAttribute("notificationKey", notificationKey);
            request.setAttribute("reportMessage", controller.reportMessage(notificationKey, codiceFiscale, ruolo));
            request.setAttribute("images", images);
            forward(request, response, "/WEB-INF/views/reportImages.jsp");
        } catch (BrondiException e) {
            logger.warn("Report images not available: {}", e.getDetails());
            forwardError(request, response, e.getMessage(), "/errorLogged.jsp");
        } catch (Exception e) {
            logger.error("Unexpected error while loading report images", e);
            forwardError(request, response, "Unable to load report images.", "/errorLogged.jsp");
        }
    }
}
