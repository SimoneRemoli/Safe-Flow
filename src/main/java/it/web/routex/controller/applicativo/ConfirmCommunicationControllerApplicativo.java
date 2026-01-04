package it.web.routex.controller.applicativo;


import it.web.routex.bean.MessageBean;
import it.web.routex.exception.DAOExceptionRemoli;
import it.web.routex.dao.SendCommunicationDAO;

import java.sql.SQLException;

public class ConfirmCommunicationControllerApplicativo {

    public boolean communication(MessageBean message) throws DAOExceptionRemoli, SQLException {
        try {
            SendCommunicationDAO sendCommunication = new SendCommunicationDAO();
            sendCommunication.sendMessage(message);
            System.out.println("\nMessaggio inviato.\n");
            return true;

        } catch (Exception e) {
            // catch generico per sicurezza
            System.err.println("Errore nell'invio del messaggio: " + e.getMessage());
            throw new DAOExceptionRemoli("Errore nella communication()", e);
        }
    }

}