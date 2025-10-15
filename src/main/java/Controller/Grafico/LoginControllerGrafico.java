package Controller.Grafico;



import Bean.AutenticazioneBean;
import Controller.Applicativo.LoginController;
import Factory.ConnectionFactory;
import Model.DAO.LoginProcedureDAO;
import Model.Domain.Credentials;
import Exception.DAOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Connection;
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
            login.autenticaUtente();


            if(login.qualeTabellA()) { //utente normale

                session.setAttribute("nome", login.autenticaUtente().getNome());
                session.setAttribute("cognome", login.autenticaUtente().getCognome());
                session.setAttribute("cf", login.autenticaUtente().getCodiceFiscale());
                session.setAttribute("disabile", login.autenticaUtente().getDisabile() ? "yes" : null);

                // Reindirizza a una pagina di conferma (o login.jsp)
                response.sendRedirect("index.jsp");
                System.out.println("Nome: " + login.autenticaUtente().getNome() + " Cognome: "+ login.autenticaUtente().getCognome()
                + "\n CF: "+ login.autenticaUtente().getCodiceFiscale()+" IsDisabile: "+login.autenticaUtente().getDisabile());
            }
            else
           //worker o admin
            {
                session.setAttribute("nome", login.autenticaUtente().getNome());
                session.setAttribute("cognome", login.autenticaUtente().getCognome());
                session.setAttribute("ruolo", login.autenticaUtente().getRuolo());
                System.out.println("Nome: " + login.autenticaUtente().getNome() + " Cognome: " + login.autenticaUtente().getCognome()
                        + "\n ruolo: " + login.autenticaUtente().getRuolo());

               /* switch(cred.getRuolo())
                {
                    case WORKER -> new Worker_Controller().start(response);//1 worker


                    case ADMIN -> new Admin_Controller().start(response);//2 admin
                }
                */


            }
        } catch (DAOException ex) {
            throw new RuntimeException(ex);
        } catch (ClassNotFoundException ex) {
            throw new RuntimeException(ex);
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }

    }
}