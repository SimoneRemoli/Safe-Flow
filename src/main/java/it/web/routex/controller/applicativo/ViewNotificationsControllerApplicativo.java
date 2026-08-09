package it.web.routex.controller.applicativo;
import it.web.routex.bean.MessageBean;
import it.web.routex.dao.LayerPersistenza;
import it.web.routex.exception.BrondiException;
import it.web.routex.exception.DAOExceptionRemoli;
import it.web.routex.model.Notification;
import it.web.routex.model.UserProfileSummary;
import it.web.routex.utility.factory.FactoryLayerPersistenza;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Collections;

public class ViewNotificationsControllerApplicativo {

    public List<MessageBean> messages(String ruolo, String codiceFiscale) throws BrondiException {

        List<MessageBean> result = new ArrayList<>();

        LayerPersistenza layer = FactoryLayerPersistenza.createLayerPersistenza();
        try {
            List<Notification> notifications = layer.getMessagesRAM();
            Set<String> senderCodiciFiscali = new HashSet<>();
            for (Notification n : notifications) {
                boolean include = false;

                if ("TRAVELER".equalsIgnoreCase(ruolo)) {
                    include = "APPROVED".equalsIgnoreCase(n.getStatus()) && n.getRecipientCf() == null;
                }

                if (include) {
                    MessageBean bean = new MessageBean(n.getMessage(), n.getDate());
                    bean.setRisolto(n.isRisolto());
                    bean.setApprovato(n.isApprovato());
                    bean.setLetto(n.isLetto());
                    bean.setStatus(n.getStatus());
                    bean.setSenderRole(n.getSenderRole());
                    bean.setSenderCf(n.getSenderCf());
                    bean.setRecipientCf(n.getRecipientCf());
                    bean.setCity(n.getCity());
                    bean.setPickpocketAlert(n.isPickpocketAlert());
                    bean.setFightAlert(n.isFightAlert());
                    bean.setCrowdAlert(n.isCrowdAlert());
                    bean.setGeneralAlert(n.isGeneralAlert());
                    bean.setStationName(n.getStationName());
                    bean.setSuspectClothing(n.getSuspectClothing());
                    if (n.getSenderCf() != null && !n.getSenderCf().isBlank()) {
                        senderCodiciFiscali.add(n.getSenderCf());
                    }
                    result.add(bean);
                }
            }

            enrichSenderProfiles(result, codiceFiscale, senderCodiciFiscali);
            return result;

        } catch (DAOExceptionRemoli e) {
            throw new BrondiException(
                    "Errore nel recupero delle notifiche",
                    "BRONDI_020",
                    "ViewNotificationsControllerApplicativo.messages",
                    e
            );
        }
    }

    private void enrichSenderProfiles(List<MessageBean> messages,
                                      String currentUserCf,
                                      Set<String> senderCodiciFiscali) {
        Map<String, UserProfileSummary> profiles;
        try {
            UserProfileControllerApplicativo profileController = new UserProfileControllerApplicativo();
            profiles = profileController.getProfilesByCodiceFiscale(senderCodiciFiscali);
        } catch (BrondiException e) {
            profiles = Collections.emptyMap();
        }

        for (MessageBean message : messages) {
            String senderCf = message.getSenderCf();
            boolean currentUserSender = senderCf != null
                    && currentUserCf != null
                    && senderCf.equalsIgnoreCase(currentUserCf);

            UserProfileSummary profile = senderCf == null ? null : profiles.get(senderCf.trim().toUpperCase());
            message.setCurrentUserSender(currentUserSender);

            if (profile != null) {
                message.setSenderName(profile.getNome());
                message.setSenderSurname(profile.getCognome());
                message.setSenderDisplayName(currentUserSender ? "me" : profile.getDisplayName());
                message.setSenderInitials(profile.getInitials());
                message.setSenderAvatarPresent(profile.isAvatarPresent());
                message.setSenderProfileAvailable(true);
            } else {
                boolean missingSender = senderCf == null || senderCf.isBlank();
                message.setSenderDisplayName(currentUserSender ? "me" : missingSender ? "Safe Flow Team" : "Unknown user");
                message.setSenderInitials(currentUserSender ? "ME" : missingSender ? "SF" : "U");
                message.setSenderAvatarPresent(false);
                message.setSenderProfileAvailable(false);
            }
        }
    }
}
