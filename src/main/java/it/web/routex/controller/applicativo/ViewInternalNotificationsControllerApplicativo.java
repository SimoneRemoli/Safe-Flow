package it.web.routex.controller.applicativo;

import it.web.routex.bean.MessageBean;
import it.web.routex.dao.LayerPersistenza;
import it.web.routex.exception.BrondiException;
import it.web.routex.exception.DAOExceptionRemoli;
import it.web.routex.model.Notification;
import it.web.routex.model.UserProfileSummary;
import it.web.routex.utility.factory.FactoryLayerPersistenza;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ViewInternalNotificationsControllerApplicativo {

    public List<MessageBean> messages(String codiceFiscale) throws BrondiException {
        List<MessageBean> result = new ArrayList<>();
        try {
            LayerPersistenza layer = FactoryLayerPersistenza.createLayerPersistenza();
            NotificationCommentControllerApplicativo commentTargets = new NotificationCommentControllerApplicativo();
            Set<String> senderCodiciFiscali = new HashSet<>();
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
                bean.setActionUrl(commentTargets.targetUrlForInternalNotification(notification));
                if (notification.getSenderCf() != null && !notification.getSenderCf().isBlank()) {
                    senderCodiciFiscali.add(notification.getSenderCf());
                }
                result.add(bean);
            }
            enrichSenderProfiles(result, senderCodiciFiscali);
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

    private void enrichSenderProfiles(List<MessageBean> messages, Set<String> senderCodiciFiscali) {
        Map<String, UserProfileSummary> profiles;
        try {
            profiles = new UserProfileControllerApplicativo().getProfilesByCodiceFiscale(senderCodiciFiscali);
        } catch (BrondiException e) {
            profiles = Collections.emptyMap();
        }

        for (MessageBean message : messages) {
            if ("ADMIN".equalsIgnoreCase(message.getSenderRole())) {
                message.setSenderDisplayName("Safe Flow Admin Team");
                message.setSenderInitials("SF");
                message.setSenderAvatarPresent(false);
                message.setSenderProfileAvailable(false);
                continue;
            }

            String senderCf = message.getSenderCf();
            UserProfileSummary profile = senderCf == null ? null : profiles.get(senderCf.trim().toUpperCase());
            if (profile != null) {
                message.setSenderDisplayName(profile.getDisplayName());
                message.setSenderInitials(profile.getInitials());
                message.setSenderAvatarPresent(profile.isAvatarPresent());
                message.setSenderProfileAvailable(true);
            } else {
                message.setSenderDisplayName("Traveler");
                message.setSenderInitials("T");
                message.setSenderAvatarPresent(false);
                message.setSenderProfileAvailable(false);
            }
        }
    }
}
