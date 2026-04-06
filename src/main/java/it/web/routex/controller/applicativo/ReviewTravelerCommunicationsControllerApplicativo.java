package it.web.routex.controller.applicativo;

import it.web.routex.bean.MessageBean;
import it.web.routex.dao.LayerPersistenza;
import it.web.routex.exception.DAOExceptionRemoli;
import it.web.routex.model.Notification;
import it.web.routex.utility.factory.FactoryLayerPersistenza;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class ReviewTravelerCommunicationsControllerApplicativo {

    public List<MessageBean> pendingMessages() throws DAOExceptionRemoli {
        LayerPersistenza layer = FactoryLayerPersistenza.createLayerPersistenza();
        List<MessageBean> result = new ArrayList<>();

        for (Notification notification : layer.getMessagesRAM()) {
            if (!"PENDING".equalsIgnoreCase(notification.getStatus())) {
                continue;
            }
            if (!"TRAVELER".equalsIgnoreCase(notification.getSenderRole())) {
                continue;
            }

            MessageBean bean = new MessageBean(notification.getMessage(), notification.getDate());
            bean.setApprovato(false);
            bean.setStatus(notification.getStatus());
            bean.setSenderRole(notification.getSenderRole());
            bean.setSenderCf(notification.getSenderCf());
            bean.setCity(notification.getCity());
            bean.setPickpocketAlert(notification.isPickpocketAlert());
            bean.setFightAlert(notification.isFightAlert());
            bean.setCrowdAlert(notification.isCrowdAlert());
            bean.setGeneralAlert(notification.isGeneralAlert());
            bean.setStationName(notification.getStationName());
            bean.setSuspectClothing(notification.getSuspectClothing());
            result.add(bean);
        }

        return result;
    }

    public void approve(MessageBean message, String adminCf) throws DAOExceptionRemoli {
        LayerPersistenza layer = FactoryLayerPersistenza.createLayerPersistenza();

        for (Notification notification : layer.getMessagesRAM()) {
            if (!notification.getMessage().equals(message.getMessage())) {
                continue;
            }
            if (!notification.getDate().equals(message.getDate())) {
                continue;
            }
            if (!sameSender(notification, message)) {
                continue;
            }

            if (!layer.approvePendingTravelerNotification(notification)) {
                throw new DAOExceptionRemoli("This traveler report has already been handled by another admin.");
            }

            Notification adminNotification = new Notification(
                    "Your traveler report has been approved by the RouteX admin team.",
                    new Timestamp(System.currentTimeMillis()),
                    false,
                    true,
                    false,
                    "APPROVED",
                    "ADMIN",
                    adminCf,
                    notification.getSenderCf(),
                    notification.getCity(),
                    notification.isPickpocketAlert(),
                    notification.isFightAlert(),
                    notification.isCrowdAlert(),
                    notification.isGeneralAlert(),
                    notification.getStationName(),
                    notification.getSuspectClothing()
            );
            layer.sendMessage(adminNotification);
            return;
        }

        throw new DAOExceptionRemoli("Pending traveler notification not found.");
    }

    public void reject(MessageBean message, String adminCf, String reason) throws DAOExceptionRemoli {
        LayerPersistenza layer = FactoryLayerPersistenza.createLayerPersistenza();

        for (Notification notification : layer.getMessagesRAM()) {
            if (!notification.getMessage().equals(message.getMessage())) {
                continue;
            }
            if (!notification.getDate().equals(message.getDate())) {
                continue;
            }
            if (!sameSender(notification, message)) {
                continue;
            }

            if (!layer.rejectPendingTravelerNotification(notification)) {
                throw new DAOExceptionRemoli("This traveler report has already been handled by another admin.");
            }

            Notification adminNotification = new Notification(
                    "Your traveler report has been rejected: " + reason,
                    new Timestamp(System.currentTimeMillis()),
                    false,
                    true,
                    false,
                    "APPROVED",
                    "ADMIN",
                    adminCf,
                    notification.getSenderCf(),
                    notification.getCity(),
                    notification.isPickpocketAlert(),
                    notification.isFightAlert(),
                    notification.isCrowdAlert(),
                    notification.isGeneralAlert(),
                    notification.getStationName(),
                    notification.getSuspectClothing()
            );
            layer.sendMessage(adminNotification);
            return;
        }

        throw new DAOExceptionRemoli("Pending traveler notification not found.");
    }

    private boolean sameSender(Notification notification, MessageBean message) {
        if (message.getSenderCf() == null || message.getSenderCf().isBlank()) {
            return notification.getSenderCf() == null || notification.getSenderCf().isBlank();
        }
        return message.getSenderCf().equals(notification.getSenderCf());
    }
}
