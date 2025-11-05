package Controller.Applicativo;

import Model.Domain.Credentials;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/logout")
public class LogoutController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false); // prendi la sessione esistente
        if (session != null) {
            Credentials.clearInstance(session);
            session.invalidate(); // invalida la sessione
        }
        response.sendRedirect("index.jsp"); // reindirizza alla homepage o al login
    }
}