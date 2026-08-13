package it.web.safeflow.controller.applicativo;

import it.web.safeflow.bean.MessageBean;
import it.web.safeflow.dao.LayerPersistenza;
import it.web.safeflow.exception.BrondiException;
import it.web.safeflow.exception.DAOExceptionRemoli;
import it.web.safeflow.model.Credentials;
import it.web.safeflow.model.Notification;
import it.web.safeflow.utility.factory.FactoryLayerPersistenza;
import it.web.safeflow.utility.observer.Notifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Timestamp;
import java.util.List;

public class ConfirmCommunicationControllerApplicativo {

    private static final Logger logger = LoggerFactory.getLogger(ConfirmCommunicationControllerApplicativo.class);

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
        try {
            notifyTravelersAboutAdminAlert(layer, notification, senderCf);
        } catch (DAOExceptionRemoli e) {
            logger.warn("Admin alert published, but traveler internal notifications could not be created.", e);
        }
        Notifier.getInstanceSingleton().comunicazioneInviata();
    }

    private void notifyTravelersAboutAdminAlert(LayerPersistenza layer,
                                                Notification publicNotification,
                                                String senderCf) throws DAOExceptionRemoli {
        List<Credentials> travelers = layer.listTravelers();
        if (travelers.isEmpty()) {
            return;
        }

        String publicNotificationKey = NotificationLikeControllerApplicativo.keyFor(publicNotification);
        long baseMillis = System.currentTimeMillis();
        int offset = 0;

        for (Credentials traveler : travelers) {
            String recipientCf = traveler.getCodiceFiscale();
            if (recipientCf == null || recipientCf.isBlank()) {
                continue;
            }

            Notification internalNotification = new Notification(
                    "New Safe Flow admin alert: " + summarize(publicNotification.getMessage()),
                    new Timestamp(baseMillis + (offset++ * 1000L)),
                    false,
                    true,
                    false,
                    "APPROVED",
                    "ADMIN",
                    senderCf,
                    recipientCf,
                    publicNotification.getCity(),
                    publicNotification.isPickpocketAlert(),
                    publicNotification.isFightAlert(),
                    publicNotification.isCrowdAlert(),
                    publicNotification.isGeneralAlert(),
                    publicNotification.getStationName(),
                    publicNotification.getSuspectClothing()
            );

            layer.sendMessage(internalNotification);
            storeInternalTarget(internalNotification, publicNotificationKey);
        }
    }

    private void storeInternalTarget(Notification internalNotification,
                                     String publicNotificationKey) throws DAOExceptionRemoli {
        try {
            new NotificationCommentControllerApplicativo()
                    .storeTargetForInternalNotification(internalNotification, publicNotificationKey, null);
        } catch (BrondiException e) {
            throw new DAOExceptionRemoli("Unable to link the admin notification to the published message.", e);
        }
    }

    private String summarize(String message) {
        if (message == null || message.isBlank()) {
            return "new public transport information";
        }

        String normalized = message.trim().replaceAll("\\s+", " ");
        if (normalized.length() <= 90) {
            return "\"" + normalized + "\"";
        }
        return "\"" + normalized.substring(0, 87) + "...\"";
    }
}
