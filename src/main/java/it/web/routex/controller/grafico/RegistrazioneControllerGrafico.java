package it.web.routex.controller.grafico;


import it.web.routex.bean.UtenteBeanGenerico;
import it.web.routex.controller.applicativo.RegistrazioneControllerApplicativo;
import it.web.routex.domain.CredentialsDTO;
import it.web.routex.enumerator.Ruolo;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Date;

@WebServlet("/register")
public class RegistrazioneControllerGrafico extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            // --- Lettura parametri form ---
            String nome = request.getParameter("firstName");
            String cognome = request.getParameter("lastName");
            String codiceFiscale = request.getParameter("codicefiscale");
            String password = request.getParameter("password");
            String email = request.getParameter("email");
            String birthParam = request.getParameter("birthdate");
            Date dataDiNascita = (birthParam == null || birthParam.isEmpty()) ? null : Date.valueOf(birthParam);
            //    Boolean disabile = Boolean.parseBoolean(request.getParameter("disabled"));
            String checkDisabile = request.getParameter("disabled");
            Boolean disabile = (checkDisabile == null || checkDisabile.isEmpty()) ? null : Boolean.valueOf(checkDisabile);
            Integer oraInizio = null;
            Integer oraFine = null;

            String oraInizioParam = request.getParameter("oraInizio");
            String oraFineParam   = request.getParameter("oraFine");
            String luogoDiLavoro   = request.getParameter("luogoDiLavoro");

            if (oraInizioParam != null && !oraInizioParam.isEmpty()) {
                oraInizio = Integer.valueOf(oraInizioParam);
            }

            if (oraFineParam != null && !oraFineParam.isEmpty()) {
                oraFine = Integer.valueOf(oraFineParam);
            }


            // --- Determino il ruolo ---
            Ruolo ruolo = null;

            /*if (disabile != null)
                ruolo = disabile ? Ruolo.DISABLED_TRAVELER : Ruolo.TRAVELER;
            else
                ruolo = Ruolo.WORKER;

               l'ho tolto io momentaneamente
             */

            // 🔒 Sicurezza lato server
            if (ruolo != Ruolo.WORKER) {
                oraInizio = null;
                oraFine = null;
            }
            else if (oraInizio != null && oraFine != null) {
                if (oraInizio < 0 || oraInizio > 23 || oraFine < 0 || oraFine > 23 || oraInizio >= oraFine) {
                    request.setAttribute("errore", "Orari di lavoro non validi");
                    request.getRequestDispatcher("register.jsp").forward(request, response);
                    return;
                }
            }



            // --- Creo il DTO ---
            CredentialsDTO cred = new CredentialsDTO(
                    nome,
                    cognome,
                    codiceFiscale,
                    password,
                    email,
                    dataDiNascita,
                    disabile,
                    ruolo,
                    oraInizio,
                    oraFine,
                    luogoDiLavoro
            );
            cred.setRuolo(ruolo);

            // --- Registrazione tramite controller applicativo ---
            RegistrazioneControllerApplicativo service = new RegistrazioneControllerApplicativo();
            boolean ok = service.register(cred);

            if (!ok) {
                request.setAttribute("errore", "Registrazione non riuscita.");
                request.getRequestDispatcher("register.jsp").forward(request, response);
                return;
            }

            // --- Creazione sessione e login automatico ---
            HttpSession session = request.getSession();
            /*if(ruolo == Ruolo.TRAVELER || ruolo == Ruolo.DISABLED_TRAVELER) {
                LoginController loginController = new LoginController(email, password);
                UtenteBeanGenerico utente = loginController.autenticaUtente();
                session.setAttribute("utente", utente);

                // --- Redirect in base al ruolo ---
                gestisciReindirizzamento(utente, response);
            }

             */

            request.getSession().setAttribute("successRegister", "Lavoratore registrato con successo.");

            response.sendRedirect(request.getContextPath() + "/indexAdmin.jsp");

        } catch (Exception e) {
            throw new ServletException("Errore registrazione", e);
        }
    }

    private void gestisciReindirizzamento(UtenteBeanGenerico utente, HttpServletResponse response)
            throws IOException {

        switch (utente.getRuolo()) {
            case WORKER -> response.sendRedirect("registerWorkerDone.jsp");
            case /*DISABLED_TRAVELER*/ TRAVELER -> response.sendRedirect("loginDone.jsp");
            //case DISABLED_TRAVELER, TRAVELER -> response.sendRedirect("index.jsp");
            default -> response.sendRedirect("index.jsp");
        }
    }
}