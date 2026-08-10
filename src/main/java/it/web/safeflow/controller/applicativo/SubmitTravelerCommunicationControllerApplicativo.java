package it.web.safeflow.controller.applicativo;

import it.web.safeflow.bean.MessageBean;
import it.web.safeflow.dao.LayerPersistenza;
import it.web.safeflow.exception.DAOExceptionRemoli;
import it.web.safeflow.model.Notification;
import it.web.safeflow.utility.factory.FactoryLayerPersistenza;

public class SubmitTravelerCommunicationControllerApplicativo {

    public void submit(MessageBean message) throws DAOExceptionRemoli {
        Notification notification = new Notification(
                message.getMessage(),
                message.getDate(),
                false,
                false,
                true,
                "PENDING",
                "TRAVELER",
                message.getSenderCf(),
                null,
                message.getCity(),
                Boolean.TRUE.equals(message.getPickpocketAlert()),
                Boolean.TRUE.equals(message.getFightAlert()),
                Boolean.TRUE.equals(message.getCrowdAlert()),
                Boolean.TRUE.equals(message.getGeneralAlert()),
                message.getStationName(),
                message.getSuspectClothing()
        );

        LayerPersistenza layer = FactoryLayerPersistenza.createLayerPersistenza();
        layer.sendMessage(notification);
    }
}
