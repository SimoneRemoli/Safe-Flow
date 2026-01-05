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
            return true;

        } catch (Exception e) {
            throw new DAOExceptionRemoli("Errore nella communication()", e);
        }
    }
}