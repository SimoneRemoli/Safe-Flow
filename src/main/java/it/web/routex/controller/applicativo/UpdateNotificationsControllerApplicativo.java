package it.web.routex.controller.applicativo;


import it.web.routex.bean.MessageBean;
import it.web.routex.exception.DAOExceptionRemoli;
import it.web.routex.dao.SendCommunicationDAO;

import java.sql.Timestamp;

public class UpdateNotificationsControllerApplicativo {

    public void aggiornaStatoNotifiche(String[] risolte) throws DAOExceptionRemoli {

        SendCommunicationDAO dao = new SendCommunicationDAO();

        if (risolte == null) {
            return;
        }

        for (String r : risolte) {

            // formato: timestamp|messaggio
            String[] parts = r.split("\\|", 2);

            long time = Long.parseLong(parts[0]);
            String message = parts[1];

            MessageBean m = new MessageBean();
            m.setDate(new Timestamp(time));
            m.setMessage(message);
            m.setRisolto(true);

            dao.solvedMessage(m);
        }
    }
}