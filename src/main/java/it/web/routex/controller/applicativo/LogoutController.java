package it.web.routex.controller.applicativo;

import it.web.routex.utility.Singleton.Credentials;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/logout")
public class LogoutController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    {

        try {
            // 1. Svuota il singleton delle credenziali
            Credentials.getInstanceSingleton().clear();

            // 2. Invalida anche la sessione (per sicurezza)
            HttpSession session = request.getSession(false);
            if (session != null) {
                session.invalidate();
            }

            // 3. Torna alla home / login
            response.sendRedirect("index.jsp");
        }catch(Exception e)
        {
            throw new RuntimeException(e.getMessage());
        }
    }
}
