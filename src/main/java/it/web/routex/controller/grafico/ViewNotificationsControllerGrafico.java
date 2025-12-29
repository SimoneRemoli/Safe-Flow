package it.web.routex.controller.grafico;

import it.web.routex.controller.applicativo.ViewNotificationsControllerApplicativo;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/viewNotifications")
public class ViewNotificationsControllerGrafico extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        try {

            ViewNotificationsControllerApplicativo notifications = new ViewNotificationsControllerApplicativo();
            request.getSession().setAttribute("notifiche", notifications.messages());
            // Se non sei loggato o cf è null, reindirizza a login
            response.sendRedirect(request.getContextPath() + "/viewNotifications.jsp");

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}