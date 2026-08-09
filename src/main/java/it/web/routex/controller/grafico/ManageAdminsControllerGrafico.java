package it.web.routex.controller.grafico;

import it.web.routex.controller.applicativo.ManageAdminsControllerApplicativo;
import it.web.routex.domain.LoggedHttpServlet;
import it.web.routex.domain.SessionAuthUtil;
import it.web.routex.exception.DAOExceptionRemoli;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@WebServlet("/manageAdmins")
public class ManageAdminsControllerGrafico extends LoggedHttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        if (!hasRole(request, "ADMIN")) {
            redirectToLogin(request, response);
            return;
        }
        loadPage(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        if (!SessionAuthUtil.hasRole(session, "ADMIN")) {
            redirectToLogin(request, response);
            return;
        }

        ManageAdminsControllerApplicativo controller = new ManageAdminsControllerApplicativo();
        String action = request.getParameter("action");

        try {
            if ("createAdmin".equals(action)) {
                controller.createAdmin(
                        request.getParameter("nome"),
                        request.getParameter("cognome"),
                        request.getParameter("email"),
                        request.getParameter("password"),
                        request.getParameter("codiceFiscale")
                );
                request.getSession().setAttribute("alertMessage", "Admin account created successfully.");
                response.sendRedirect("manageAdmins");
                return;
            }

            if ("deleteAdmins".equals(action)) {
                String[] selectedAdmins = request.getParameterValues("selectedAdmins");
                String currentAdminCf = SessionAuthUtil.codiceFiscale(session).orElse(null);
                int deleted = controller.deleteAdmins(
                        selectedAdmins != null ? Arrays.asList(selectedAdmins) : Collections.emptyList(),
                        currentAdminCf
                );
                request.getSession().setAttribute("alertMessage", deleted + " admin account(s) deleted.");
                response.sendRedirect("manageAdmins");
                return;
            }

            if ("deleteTravelers".equals(action)) {
                String[] selectedTravelers = request.getParameterValues("selectedTravelers");
                int deleted = controller.deleteTravelers(
                        selectedTravelers != null ? Arrays.asList(selectedTravelers) : Collections.emptyList()
                );
                request.getSession().setAttribute("alertMessage", deleted + " traveler account(s) deleted.");
                response.sendRedirect("manageAdmins");
                return;
            }

            request.setAttribute("errore", "Unsupported admin action.");
            request.getRequestDispatcher("/adminError.jsp").forward(request, response);
        } catch (DAOExceptionRemoli e) {
            logger.error("Admin management error", e);
            request.setAttribute("inlineError", e.getMessage());
            loadPage(request, response);
        } catch (Exception e) {
            logger.error("Unexpected admin management error", e);
            request.setAttribute("errore", "Unexpected error while managing admin accounts.");
            try {
                request.getRequestDispatcher("/adminError.jsp").forward(request, response);
            } catch (Exception ex) {
                logger.error("Forward error on admin management failure", ex);
            }
        }
    }

    private void loadPage(HttpServletRequest request, HttpServletResponse response) {
        ManageAdminsControllerApplicativo controller = new ManageAdminsControllerApplicativo();
        try {
            request.setAttribute("admins", controller.listAdmins());
            request.setAttribute("travelers", controller.listTravelers());
            request.getRequestDispatcher("/WEB-INF/views/manageAdmins.jsp").forward(request, response);
        } catch (DAOExceptionRemoli e) {
            logger.error("Error loading admin management page", e);
            request.setAttribute("errore", e.getMessage());
            try {
                request.getRequestDispatcher("/adminError.jsp").forward(request, response);
            } catch (Exception ex) {
                logger.error("Forward error while loading admin management page", ex);
            }
        } catch (Exception e) {
            logger.error("Unexpected error loading admin management page", e);
            request.setAttribute("errore", "Unexpected error while loading admin management.");
            try {
                request.getRequestDispatcher("/adminError.jsp").forward(request, response);
            } catch (Exception ex) {
                logger.error("Forward error while handling unexpected admin management error", ex);
            }
        }
    }
}
