package it.web.routex.controller.applicativo;

import it.web.routex.bean.MessageBean;
import it.web.routex.dao.LayerPersistenza;
import it.web.routex.exception.BrondiException;
import it.web.routex.exception.DAOExceptionRemoli;
import it.web.routex.model.Notification;
import it.web.routex.model.UserProfileSummary;
import it.web.routex.utility.factory.FactoryLayerPersistenza;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

public class ViewInternalNotificationsControllerApplicativo {

    private static final Object DISMISS_LOCK = new Object();
    private static final Path DISMISS_STORE = Path.of(System.getProperty("user.home"), ".safe-flow", "internal-notification-dismissals.properties");
    private static final String DISMISSED_PREFIX = "dismissed.";

    public List<MessageBean> messages(String codiceFiscale) throws BrondiException {
        List<MessageBean> result = new ArrayList<>();
        try {
            LayerPersistenza layer = FactoryLayerPersistenza.createLayerPersistenza();
            NotificationCommentControllerApplicativo commentTargets = new NotificationCommentControllerApplicativo();
            Set<String> senderCodiciFiscali = new HashSet<>();
            Set<String> dismissedKeys = dismissedKeysFor(codiceFiscale);
            for (Notification notification : layer.getMessagesRAM()) {
                if (!"APPROVED".equalsIgnoreCase(notification.getStatus())) {
                    continue;
                }
                if (notification.getRecipientCf() == null || !notification.getRecipientCf().equalsIgnoreCase(codiceFiscale)) {
                    continue;
                }

                String notificationKey = NotificationLikeControllerApplicativo.keyFor(notification);
                if (dismissedKeys.contains(notificationKey)) {
                    continue;
                }

                MessageBean bean = new MessageBean(notification.getMessage(), notification.getDate());
                bean.setNotificationKey(notificationKey);
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
            Set<String> dismissedKeys = dismissedKeysFor(codiceFiscale);
            for (Notification notification : layer.getMessagesRAM()) {
                if ("APPROVED".equalsIgnoreCase(notification.getStatus())
                        && notification.getRecipientCf() != null
                        && notification.getRecipientCf().equalsIgnoreCase(codiceFiscale)
                        && !dismissedKeys.contains(NotificationLikeControllerApplicativo.keyFor(notification))
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
            Set<String> dismissedKeys = dismissedKeysFor(codiceFiscale);
            for (Notification notification : layer.getMessagesRAM()) {
                if ("APPROVED".equalsIgnoreCase(notification.getStatus())
                        && notification.getRecipientCf() != null
                        && notification.getRecipientCf().equalsIgnoreCase(codiceFiscale)
                        && !dismissedKeys.contains(NotificationLikeControllerApplicativo.keyFor(notification))
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

    public void dismissNotification(String codiceFiscale, String notificationKey) throws BrondiException {
        String cf = normalizeCf(codiceFiscale);
        String key = normalizeNotificationKey(notificationKey);
        ensureNotificationBelongsToTraveler(cf, key);

        synchronized (DISMISS_LOCK) {
            Properties properties = loadDismissProperties();
            properties.setProperty(dismissPropertyKey(cf, key), "true");
            storeDismissProperties(properties);
        }
    }

    private void ensureNotificationBelongsToTraveler(String codiceFiscale, String notificationKey) throws BrondiException {
        try {
            LayerPersistenza layer = FactoryLayerPersistenza.createLayerPersistenza();
            for (Notification notification : layer.getMessagesRAM()) {
                if ("APPROVED".equalsIgnoreCase(notification.getStatus())
                        && notification.getRecipientCf() != null
                        && notification.getRecipientCf().equalsIgnoreCase(codiceFiscale)
                        && notificationKey.equals(NotificationLikeControllerApplicativo.keyFor(notification))) {
                    return;
                }
            }
        } catch (DAOExceptionRemoli e) {
            throw new BrondiException(
                    "Error while removing the notification",
                    "BRONDI_024",
                    "ViewInternalNotificationsControllerApplicativo.dismissNotification",
                    e
            );
        }

        throw new BrondiException("This notification cannot be removed.", "INTERNAL_NOTIFICATION_NOT_FOUND", notificationKey);
    }

    private Set<String> dismissedKeysFor(String codiceFiscale) throws BrondiException {
        String cf = normalizeCf(codiceFiscale);
        String prefix = DISMISSED_PREFIX + cf + ".";
        Set<String> keys = new HashSet<>();

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
            throw new BrondiException("Invalid user session.", "INTERNAL_NOTIFICATION_SESSION", "Missing codice fiscale");
        }
        return codiceFiscale.trim().toUpperCase(Locale.ROOT);
    }

    private String normalizeNotificationKey(String notificationKey) throws BrondiException {
        if (notificationKey == null || notificationKey.trim().isEmpty()) {
            throw new BrondiException("Invalid notification.", "INTERNAL_NOTIFICATION_KEY", "Missing notification key");
        }

        String key = notificationKey.trim();
        if (!key.matches("[A-Za-z0-9_-]+")) {
            throw new BrondiException("Invalid notification.", "INTERNAL_NOTIFICATION_KEY", "Invalid notification key");
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
            throw new BrondiException("Unable to read removed notifications.", "INTERNAL_NOTIFICATION_DISMISS_IO", "Dismiss store read error", e);
        }
    }

    private void storeDismissProperties(Properties properties) throws BrondiException {
        try {
            Files.createDirectories(DISMISS_STORE.getParent());
            try (OutputStream output = Files.newOutputStream(DISMISS_STORE)) {
                properties.store(output, "Safe Flow internal notification dismissals");
            }
        } catch (IOException e) {
            throw new BrondiException("Unable to remove the notification.", "INTERNAL_NOTIFICATION_DISMISS_IO", "Dismiss store write error", e);
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
