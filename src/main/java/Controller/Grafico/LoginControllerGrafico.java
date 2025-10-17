package Controller.Grafico;



import Bean.AutenticazioneBean;
import Bean.UtenteBeanGenerico;
import Controller.Applicativo.LoginController;
import Factory.ConnectionFactory;
import Exception.DAOException;
import Model.Domain.Ruolo;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/login")
public class LoginControllerGrafico extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            HttpSession session = request.getSession();
            String email = request.getParameter("Email");
            String password = request.getParameter("Password");
            Class.forName("com.mysql.cj.jdbc.Driver");

            AutenticazioneBean aut = new AutenticazioneBean(email,password);
            LoginController login = new LoginController(aut);
            UtenteBeanGenerico utente = login.autenticaUtente();
            session.setAttribute("utenteLoggato", utente);
            utente.gestisciLogin(session, response);


        } catch (DAOException ex) {
           // throw new RuntimeException(ex); //entra qua se so sbajate le credenziali
            request.setAttribute("messaggioErrore", "Credenziali non valide. Riprova.");
            request.getRequestDispatcher("erroreLogin.jsp").forward(request, response);

        } catch (ClassNotFoundException ex) {
            throw new RuntimeException(ex);
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }

    }
}