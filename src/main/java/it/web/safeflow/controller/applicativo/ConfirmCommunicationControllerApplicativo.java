package it.web.safeflow.controller.applicativo;

import it.web.safeflow.bean.MessageBean;
import it.web.safeflow.dao.LayerPersistenza;
import it.web.safeflow.exception.DAOExceptionRemoli;
import it.web.safeflow.model.Notification;
import it.web.safeflow.utility.factory.FactoryLayerPersistenza;
import it.web.safeflow.utility.observer.Notifier;

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
