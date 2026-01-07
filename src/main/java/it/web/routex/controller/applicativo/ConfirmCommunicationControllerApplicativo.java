package it.web.routex.controller.applicativo;

import it.web.routex.bean.MessageBean;
import it.web.routex.dao.LayerPersistenza;
import it.web.routex.exception.DAOExceptionRemoli;
import it.web.routex.model.Notification;
import it.web.routex.utility.factory.FactoryLayerPersistenza;

public class ConfirmCommunicationControllerApplicativo {

    public void communication(MessageBean bean) throws DAOExceptionRemoli {

        // TRASFORMAZIONE boundary → dominio
        Notification notification = new Notification(
                bean.getMessage(),
                bean.getDate(),
                false   // nuova comunicazione = non risolta
        );

        /*SendCommunicationDAO dao = new SendCommunicationDAO();
        dao.sendMessage(notification);
         */

        LayerPersistenza layer = FactoryLayerPersistenza.createLayerPersistenza();
        layer.sendMessage(notification);
    }
}
