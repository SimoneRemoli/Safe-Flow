package Bean;

import Controller.Applicativo.RegistrazionePagamentoController;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;

public abstract class UtenteBeanGenerico
{
    String nome, cognome;

    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCognome() {
        return cognome;
    }

    public String getNome() {
        return nome;
    }
    public abstract void gestisciLogin(HttpSession session, HttpServletResponse response) throws IOException;
    //public abstract void gestisciPagamento(RegistrazionePagamentoController reg, double totale) throws SQLException;

}
