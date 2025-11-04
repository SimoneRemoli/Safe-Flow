package Model.DAO;

import Factory.ConnectionFactory;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import Exception.DAOException;
import Model.Domain.Mastercard;
import Model.Domain.Ruolo;


public class MastercardDAO
{

    private final String nC, sc, cvv;

    public MastercardDAO(Mastercard m)
    {
        this.nC = m.getNumero_carta();
        this.sc = m.getData_scadenza();
        this.cvv = m.getCvv();
    }

    public int GetPaymentMastercard() throws DAOException, SQLException
    {
        int esito=-1;
        //ConnectionFactory.Cambio_Di_Ruolo(Ruolo.TRAVELER);
        try {

            Connection conn = ConnectionFactory.getConnection();
            CallableStatement cs = conn.prepareCall("{ CALL RouteX_Update.getMastercardPayment(?,?,?) }");
            cs.setString(1,this.nC);
            cs.setString(2,this.sc);
            cs.setString(3, this.cvv);

            ResultSet rs =  cs.executeQuery();

            while(rs.next()) esito = rs.getInt("esito");
            System.out.println("Pagamento effettuato");

        }catch (Exception e) {
            throw new DAOException("Errore. " + e.getMessage());
        }
        return esito;

    }
}
