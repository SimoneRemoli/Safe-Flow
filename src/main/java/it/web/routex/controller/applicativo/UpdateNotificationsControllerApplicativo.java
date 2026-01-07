package it.web.routex.controller.applicativo;

import it.web.routex.bean.MessageBean;
import it.web.routex.dao.LayerPersistenza;
import it.web.routex.exception.DAOExceptionRemoli;
import it.web.routex.model.Notification;
import it.web.routex.utility.factory.FactoryLayerPersistenza;

public class UpdateNotificationsControllerApplicativo {

    public void aggiornaStatoNotifica(MessageBean bean) throws DAOExceptionRemoli {

        // LOGICA DI BUSINESS MODEL
        Notification notification = new Notification(
                bean.getMessage(),
                bean.getDate(),
                true
        );

        /*SendCommunicationDAO dao = new SendCommunicationDAO();
        dao.solvedNotification(notification);
         */

        LayerPersistenza layer = FactoryLayerPersistenza.createLayerPersistenza();
        layer.solvedNotification(notification);
    }
}
