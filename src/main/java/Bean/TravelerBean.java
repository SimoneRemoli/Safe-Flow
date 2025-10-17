package Bean;


import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class TravelerBean extends UtenteBeanGenerico {


    private String codiceFiscale;
    private boolean isDisable;

    public String getCodiceFiscale() {
        return codiceFiscale;
    }
    public boolean getDisabile() {
        return isDisable;
    }

    public void setCodiceFiscale(String codiceFiscale) {
        this.codiceFiscale = codiceFiscale;
    }


    public void setDisable(boolean disable) {
        isDisable = disable;
    }

    @Override
    public void gestisciLogin(HttpSession session, HttpServletResponse response) throws IOException {
        session.setAttribute("nome", getNome());
        session.setAttribute("cognome", getCognome());
        session.setAttribute("cf", getCodiceFiscale());
        session.setAttribute("disabile", getDisabile() ? "yes" : null);
        System.out.println("Traveler loggato: " + getNome() + " " + getCognome());
        response.sendRedirect("index.jsp");
    }

   /* @Override
    public void gestisciPagamento(RegistrazionePagamentoController reg, double totale) throws SQLException {
        System.out.println("Traveler " + getNome() + " paga con CF " + getCodiceFiscale() + " è disabile: " + getDisabile()
        + " cognome: "+ getCognome());
        //SalvaPagamentoDAO dao = new SalvaPagamentoDAO();
        //dao.salvaPagamento("Traveler", getCodiceFiscale(), totale);
    }

    */





}
