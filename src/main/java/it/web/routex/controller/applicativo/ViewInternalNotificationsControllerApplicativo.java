package it.web.routex.controller.applicativo;

import it.web.routex.bean.MessageBean;
import it.web.routex.dao.LayerPersistenza;
import it.web.routex.exception.BrondiException;
import it.web.routex.exception.DAOExceptionRemoli;
import it.web.routex.model.Notification;
import it.web.routex.utility.factory.FactoryLayerPersistenza;

import java.util.ArrayList;
import java.util.List;

public class ViewInternalNotificationsControllerApplicativo {

    public List<MessageBean> messages(String codiceFiscale) throws BrondiException {
        List<MessageBean> result = new ArrayList<>();
        try {
            LayerPersistenza layer = FactoryLayerPersistenza.createLayerPersistenza();
            for (Notification notification : layer.getMessagesRAM()) {
                if (!"APPROVED".equalsIgnoreCase(notification.getStatus())) {
                    continue;
                }
                if (notification.getRecipientCf() == null || !notification.getRecipientCf().equalsIgnoreCase(codiceFiscale)) {
                    continue;
                }

                MessageBean bean = new MessageBean(notification.getMessage(), notification.getDate());
                bean.setLetto(notification.isLetto());
                bean.setStatus(notification.getStatus());
                bean.setSenderRole(notification.getSenderRole());
                bean.setSenderCf(notification.getSenderCf());
                bean.setRecipientCf(notification.getRecipientCf());
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
        } catch (DAOExceptionRemoli e) {
            throw new BrondiException(
                    "Error while loading internal notifications",
                    "BRONDI_021",
                    "ViewInternalNotificationsControllerApplicativo.messages",
                    e
            );
        }
    }

    public int unreadCount(String codiceFiscale) throws BrondiException {
        int unread = 0;
        try {
            LayerPersistenza layer = FactoryLayerPersistenza.createLayerPersistenza();
            for (Notification notification : layer.getMessagesRAM()) {
                if ("APPROVED".equalsIgnoreCase(notification.getStatus())
                        && notification.getRecipientCf() != null
                        && notification.getRecipientCf().equalsIgnoreCase(codiceFiscale)
                        && !notification.isLetto()) {
                    unread++;
                }
            }
            return unread;
        } catch (DAOExceptionRemoli e) {
            throw new BrondiException(
                    "Error while counting internal notifications",
                    "BRONDI_022",
                    "ViewInternalNotificationsControllerApplicativo.unreadCount",
                    e
            );
        }
    }

    public void markAllAsRead(String codiceFiscale) throws BrondiException {
        try {
            LayerPersistenza layer = FactoryLayerPersistenza.createLayerPersistenza();
            for (Notification notification : layer.getMessagesRAM()) {
                if ("APPROVED".equalsIgnoreCase(notification.getStatus())
                        && notification.getRecipientCf() != null
                        && notification.getRecipientCf().equalsIgnoreCase(codiceFiscale)
                        && !notification.isLetto()) {
                    notification.setLetto(true);
                    layer.markNotificationAsRead(notification);
                }
            }
        } catch (DAOExceptionRemoli e) {
            throw new BrondiException(
                    "Error while updating internal notifications",
                    "BRONDI_023",
                    "ViewInternalNotificationsControllerApplicativo.markAllAsRead",
                    e
            );
        }
    }
}
