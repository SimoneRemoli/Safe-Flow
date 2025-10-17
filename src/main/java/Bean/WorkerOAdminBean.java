package Bean;

import Model.Domain.Ruolo;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class WorkerOAdminBean extends UtenteBeanGenerico
{
    private Ruolo ruolo;

    public Ruolo getRuolo() {
        return ruolo;
    }

    public void setRuolo(Ruolo ruolo) {
        this.ruolo = ruolo;
    }

    public void gestisciLogin(HttpSession session, HttpServletResponse response) throws IOException {
        session.setAttribute("nome", getNome());
        session.setAttribute("cognome", getCognome());
        session.setAttribute("ruolo", ruolo);
        System.out.println("Worker o Admin loggato: " + getNome() + " (" + getRuolo() + ")");
        response.sendRedirect("dashboardWorker.jsp");
    }
    /*@Override
    public void gestisciPagamento(RegistrazionePagamentoController reg, double totale) throws SQLException {
        System.out.println("Worker/Admin " + getNome() + " (" + getRuolo() + ") effettua pagamento");
        if (getRuolo() == Ruolo.WORKER) {
            System.out.println("⚠️ Worker " + getNome() + " non può eseguire pagamenti.");
            throw new SecurityException("Il worker non può eseguire pagamenti.");
        }
        else if (getRuolo() == Ruolo.ADMIN) {
            System.out.println("⚠️ ADMIN " + getNome() + " non può eseguire pagamenti.");
            throw new SecurityException("L’amministratore non può eseguire pagamenti.");
        }
    }*/





}
