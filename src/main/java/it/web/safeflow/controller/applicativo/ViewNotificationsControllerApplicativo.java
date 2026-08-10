package it.web.safeflow.controller.applicativo;
import it.web.safeflow.bean.MessageBean;
import it.web.safeflow.dao.LayerPersistenza;
import it.web.safeflow.dao.SocialDataRepository;
import it.web.safeflow.exception.BrondiException;
import it.web.safeflow.exception.DAOExceptionRemoli;
import it.web.safeflow.model.Notification;
import it.web.safeflow.model.NotificationComment;
import it.web.safeflow.model.NotificationLikeState;
import it.web.safeflow.model.UserProfileSummary;
import it.web.safeflow.utility.factory.FactoryLayerPersistenza;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.Collections;

public class ViewNotificationsControllerApplicativo {

    private static final Object DISMISS_LOCK = new Object();
    private static final Path DISMISS_STORE = Path.of(System.getProperty("user.home"), ".safe-flow", "public-notification-dismissals.properties");
    private static final String DISMISSED_PREFIX = "dismissed.";

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
                    String notificationKey = NotificationLikeControllerApplicativo.keyFor(n);
                    MessageBean bean = new MessageBean(n.getMessage(), n.getDate());
                    bean.setNotificationKey(notificationKey);
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
                message.setSenderCommunityRank(profile.getStats().getCommunityRank());
                message.setSenderTrustLevel(profile.getStats().getTrustLevel());
            } else {
                boolean missingSender = senderCf == null || senderCf.isBlank();
                message.setSenderDisplayName(currentUserSender ? "me" : missingSender ? "Safe Flow Team" : "Unknown user");
                message.setSenderInitials(currentUserSender ? "ME" : missingSender ? "SF" : "U");
                message.setSenderAvatarPresent(false);
                message.setSenderProfileAvailable(false);
                message.setSenderCommunityRank(0);
                message.setSenderTrustLevel(null);
            }
        }
    }

    private void enrichLikes(List<MessageBean> messages,
                             String currentUserCf,
                             Set<String> notificationLikeKeys) throws BrondiException {
        Map<String, it.web.safeflow.model.NotificationLikeState> states =
                new NotificationLikeControllerApplicativo().statesFor(notificationLikeKeys, currentUserCf);

        for (MessageBean message : messages) {
            String notificationKey = message.getNotificationKey();
            it.web.safeflow.model.NotificationLikeState state = notificationKey == null ? null : states.get(notificationKey);
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
        Map<String, List<it.web.safeflow.model.NotificationComment>> commentsByKey =
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

    public void dismissNotification(String ruolo, String codiceFiscale, String notificationKey) throws BrondiException {
        if (!"TRAVELER".equalsIgnoreCase(ruolo)) {
            throw new BrondiException("Unauthorized request.", "PUBLIC_NOTIFICATION_ROLE", "Only travelers can remove public notifications");
        }

        String cf = normalizeCf(codiceFiscale);
        String key = normalizeNotificationKey(notificationKey);
        ensurePublicNotificationExists(key);

        try {
            new SocialDataRepository().dismissNotification("PUBLIC", cf, key);
            return;
        } catch (DAOExceptionRemoli ignored) {
            // Keep the legacy file store as a compatibility fallback until every environment has the DB tables.
        }

        synchronized (DISMISS_LOCK) {
            Properties properties = loadDismissProperties();
            properties.setProperty(dismissPropertyKey(cf, key), "true");
            storeDismissProperties(properties);
        }
    }

    private void ensurePublicNotificationExists(String notificationKey) throws BrondiException {
        try {
            LayerPersistenza layer = FactoryLayerPersistenza.createLayerPersistenza();
            for (Notification notification : layer.getMessagesRAM()) {
                if ("APPROVED".equalsIgnoreCase(notification.getStatus())
                        && (notification.getRecipientCf() == null || notification.getRecipientCf().isBlank())
                        && notificationKey.equals(NotificationLikeControllerApplicativo.keyFor(notification))) {
                    return;
                }
            }
        } catch (DAOExceptionRemoli e) {
            throw new BrondiException("Unable to remove notification.", "PUBLIC_NOTIFICATION_LOOKUP", "Notification lookup failed", e);
        }

        throw new BrondiException("This notification cannot be removed.", "PUBLIC_NOTIFICATION_NOT_FOUND", notificationKey);
    }

    private Set<String> dismissedKeysFor(String codiceFiscale) throws BrondiException {
        String cf = normalizeCf(codiceFiscale);
        Set<String> keys = new HashSet<>();

        try {
            keys.addAll(new SocialDataRepository().dismissedNotificationKeys("PUBLIC", cf));
        } catch (DAOExceptionRemoli ignored) {
            // Fall back to the legacy file store when the social tables are not installed yet.
        }

        String prefix = DISMISSED_PREFIX + cf + ".";

        synchronized (DISMISS_LOCK) {
            Properties properties = loadDismissProperties();
            for (Object rawKey : properties.keySet()) {
                if (rawKey instanceof String propertyKey && propertyKey.startsWith(prefix)) {
                    keys.add(propertyKey.substring(prefix.length()));
                }
            }
        }

        return keys;
    }

    private String dismissPropertyKey(String codiceFiscale, String notificationKey) {
        return DISMISSED_PREFIX + codiceFiscale + "." + notificationKey;
    }

    private String normalizeCf(String codiceFiscale) throws BrondiException {
        if (codiceFiscale == null || codiceFiscale.trim().isEmpty()) {
            throw new BrondiException("Invalid user session.", "PUBLIC_NOTIFICATION_SESSION", "Missing codice fiscale");
        }
        return codiceFiscale.trim().toUpperCase(Locale.ROOT);
    }

    private String normalizeNotificationKey(String notificationKey) throws BrondiException {
        if (notificationKey == null || notificationKey.trim().isEmpty()) {
            throw new BrondiException("Invalid notification.", "PUBLIC_NOTIFICATION_KEY", "Missing notification key");
        }

        String key = notificationKey.trim();
        if (!key.matches("[A-Za-z0-9_-]+")) {
            throw new BrondiException("Invalid notification.", "PUBLIC_NOTIFICATION_KEY", "Invalid notification key");
        }
        return key;
    }

    private Properties loadDismissProperties() throws BrondiException {
        Properties properties = new Properties();
        if (!Files.exists(DISMISS_STORE)) {
            return properties;
        }

        try (InputStream input = Files.newInputStream(DISMISS_STORE)) {
            properties.load(input);
            return properties;
        } catch (IOException e) {
            throw new BrondiException("Unable to read removed notifications.", "PUBLIC_NOTIFICATION_DISMISS_IO", "Dismiss store read error", e);
        }
    }

    private void storeDismissProperties(Properties properties) throws BrondiException {
        try {
            Files.createDirectories(DISMISS_STORE.getParent());
            try (OutputStream output = Files.newOutputStream(DISMISS_STORE)) {
                properties.store(output, "Safe Flow public notification dismissals");
            }
        } catch (IOException e) {
            throw new BrondiException("Unable to remove notification.", "PUBLIC_NOTIFICATION_DISMISS_IO", "Dismiss store write error", e);
        }
    }
}
