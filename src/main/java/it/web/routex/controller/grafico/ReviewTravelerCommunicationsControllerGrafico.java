package it.web.routex.controller.grafico;

import it.web.routex.bean.MessageBean;
import it.web.routex.controller.applicativo.ReviewTravelerCommunicationsControllerApplicativo;
import it.web.routex.domain.LoggedHttpServlet;
import it.web.routex.domain.SessionAuthUtil;
import it.web.routex.exception.DAOExceptionRemoli;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.sql.Timestamp;

@WebServlet("/reviewTravelerCommunications")
public class ReviewTravelerCommunicationsControllerGrafico extends LoggedHttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        if (!isAdmin(request.getSession(false))) {
            redirectToLogin(response);
            return;
        }
        loadPage(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        if (!isAdmin(request.getSession(false))) {
            redirectToLogin(response);
            return;
        }

        ReviewTravelerCommunicationsControllerApplicativo controller =
                new ReviewTravelerCommunicationsControllerApplicativo();
        String[] selectedMessages = request.getParameterValues("selectedMessages");
        String reviewAction = request.getParameter("reviewAction");
        HttpSession session = request.getSession(false);
        String adminCf = session != null && session.getAttribute("codiceFiscale") != null
                ? session.getAttribute("codiceFiscale").toString()
                : null;

        try {
            if (selectedMessages == null || selectedMessages.length == 0) {
                request.setAttribute("errore", "Select at least one traveler report.");
                loadPage(request, response);
                return;
            }

            for (String raw : selectedMessages) {
                String[] parts = raw.split("\\|", 3);
                MessageBean message = new MessageBean(parts[2], new Timestamp(Long.parseLong(parts[0])));
                message.setSenderCf(parts[1] == null || parts[1].isBlank() ? null : parts[1]);

                if ("reject".equalsIgnoreCase(reviewAction)) {
                    String reason = normalize(request.getParameter("rejectReason_" + parts[0]));
                    if (reason == null || reason.isBlank()) {
                        request.setAttribute("errore", "Provide a rejection reason for every selected traveler report.");
                        loadPage(request, response);
                        return;
                    }
                    controller.reject(message, adminCf, reason);
                } else {
                    controller.approve(message, adminCf);
                }
            }

            String success = "reject".equalsIgnoreCase(reviewAction)
                    ? "Selected traveler reports rejected successfully."
                    : "Selected traveler reports approved successfully.";
            request.getSession().setAttribute("alertMessage", success);
            response.sendRedirect("reviewTravelerCommunications");
        } catch (DAOExceptionRemoli e) {
            if ("This traveler report has already been handled by another admin.".equals(e.getMessage())) {
                try {
                    request.getSession().setAttribute("alertMessage", e.getMessage());
                    response.sendRedirect("reviewTravelerCommunications");
                    return;
                } catch (Exception ex) {
                    logger.error("Traveler review redirect error", ex);
                    return;
                }
            }
            request.setAttribute("errore", e.getMessage());
            loadPage(request, response);
        } catch (Exception e) {
            request.setAttribute("errore", "Unexpected error while reviewing traveler reports.");
            try {
                request.getRequestDispatcher("/adminError.jsp").forward(request, response);
            } catch (Exception ex) {
                logger.error("Traveler review forward error", ex);
            }
        }
    }

    private String normalize(String value) {
        return value == null ? null : value.trim();
    }

    private void loadPage(HttpServletRequest request, HttpServletResponse response) {
        ReviewTravelerCommunicationsControllerApplicativo controller =
                new ReviewTravelerCommunicationsControllerApplicativo();
        try {
            request.setAttribute("pendingMessages", controller.pendingMessages());
            request.getRequestDispatcher("/WEB-INF/views/reviewTravelerCommunications.jsp").forward(request, response);
        } catch (Exception e) {
            request.setAttribute("errore", "Error while loading pending traveler reports.");
            try {
                request.getRequestDispatcher("/adminError.jsp").forward(request, response);
            } catch (Exception ex) {
                logger.error("Traveler review load forward error", ex);
            }
        }
    }

    private boolean isAdmin(HttpSession session) {
        return SessionAuthUtil.isLoggedIn(session)
                && session.getAttribute("ruolo") != null
                && "ADMIN".equalsIgnoreCase(session.getAttribute("ruolo").toString());
    }

    private void redirectToLogin(HttpServletResponse response) {
        try {
            response.sendRedirect("login.jsp");
        } catch (Exception e) {
            logger.error("Traveler review redirect to login error", e);
        }
    }
}
