package it.web.routex.controller.applicativo;
import it.web.routex.bean.MessageBean;
import it.web.routex.dao.LayerPersistenza;
import it.web.routex.exception.BrondiException;
import it.web.routex.exception.DAOExceptionRemoli;
import it.web.routex.model.Notification;
import it.web.routex.model.NotificationComment;
import it.web.routex.model.NotificationLikeState;
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
            Set<String> notificationLikeKeys = new HashSet<>();
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
	                    if ("TRAVELER".equalsIgnoreCase(n.getSenderRole())) {
	                        String notificationKey = NotificationLikeControllerApplicativo.keyFor(n);
	                        bean.setNotificationKey(notificationKey);
	                        notificationLikeKeys.add(notificationKey);
	                    }
	                    if (n.getSenderCf() != null && !n.getSenderCf().isBlank()) {
	                        senderCodiciFiscali.add(n.getSenderCf());
	                    }
                    result.add(bean);
                }
            }

            enrichSenderProfiles(result, codiceFiscale, senderCodiciFiscali);
            enrichLikes(result, codiceFiscale, notificationLikeKeys);
            enrichImageCounts(result);
            enrichComments(result, codiceFiscale, notificationLikeKeys);
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

    private void enrichLikes(List<MessageBean> messages,
                             String currentUserCf,
                             Set<String> notificationLikeKeys) throws BrondiException {
        Map<String, it.web.routex.model.NotificationLikeState> states =
                new NotificationLikeControllerApplicativo().statesFor(notificationLikeKeys, currentUserCf);

        for (MessageBean message : messages) {
            String notificationKey = message.getNotificationKey();
            it.web.routex.model.NotificationLikeState state = notificationKey == null ? null : states.get(notificationKey);
            message.setLikeCount(state == null ? 0 : state.getLikeCount());
            message.setLikedByCurrentUser(state != null && state.isLikedByCurrentUser());
        }
    }

    private void enrichImageCounts(List<MessageBean> messages) {
        ReportImageControllerApplicativo reportImages = new ReportImageControllerApplicativo();
        for (MessageBean message : messages) {
            String notificationKey = message.getNotificationKey();
            message.setImageCount(notificationKey == null ? 0 : reportImages.imageCount(notificationKey));
        }
    }

    private void enrichComments(List<MessageBean> messages,
                                String currentUserCf,
                                Set<String> notificationKeys) throws BrondiException {
        Map<String, List<it.web.routex.model.NotificationComment>> commentsByKey =
                new NotificationCommentControllerApplicativo().commentsFor(notificationKeys, currentUserCf);
        Set<String> commentIds = new HashSet<>();

        for (MessageBean message : messages) {
            String notificationKey = message.getNotificationKey();
            List<NotificationComment> comments = notificationKey == null
                    ? Collections.emptyList()
                    : commentsByKey.getOrDefault(notificationKey, Collections.emptyList());
            message.setComments(comments);
            message.setCommentCount(comments.size());
            for (NotificationComment comment : comments) {
                if (comment.getId() != null && !comment.getId().isBlank()) {
                    commentIds.add(comment.getId());
                }
            }
        }

        Map<String, NotificationLikeState> commentLikeStates =
                new NotificationCommentLikeControllerApplicativo().statesFor(commentIds, currentUserCf);
        for (MessageBean message : messages) {
            for (NotificationComment comment : message.getComments()) {
                NotificationLikeState state = commentLikeStates.get(comment.getId());
                comment.setLikeCount(state == null ? 0 : state.getLikeCount());
                comment.setLikedByCurrentUser(state != null && state.isLikedByCurrentUser());
            }
        }
    }
}
