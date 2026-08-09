package it.web.routex.controller.applicativo;

import it.web.routex.bean.MessageBean;
import it.web.routex.dao.LayerPersistenza;
import it.web.routex.exception.DAOExceptionRemoli;
import it.web.routex.model.Notification;
import it.web.routex.utility.factory.FactoryLayerPersistenza;
import it.web.routex.utility.observer.Notifier;

public class ConfirmCommunicationControllerApplicativo {

    public void communication(MessageBean bean, String senderCf) throws DAOExceptionRemoli {

        // TRASFORMAZIONE boundary → dominio
        Notification notification = new Notification(
                bean.getMessage(),
                bean.getDate(),
                false,
                true,
                true,
                "APPROVED",
                "ADMIN",
                senderCf,
                null,
                bean.getCity(),
                Boolean.TRUE.equals(bean.getPickpocketAlert()),
                Boolean.TRUE.equals(bean.getFightAlert()),
                Boolean.TRUE.equals(bean.getCrowdAlert()),
                Boolean.TRUE.equals(bean.getGeneralAlert()),
                bean.getStationName(),
                bean.getSuspectClothing()
        );

        LayerPersistenza layer = FactoryLayerPersistenza.createLayerPersistenza();
        layer.sendMessage(notification);
        Notifier.getInstanceSingleton().comunicazioneInviata();
    }
}
