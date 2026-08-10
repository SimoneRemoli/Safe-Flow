package it.web.safeflow.controller.applicativo;

import it.web.safeflow.exception.BrondiException;
import it.web.safeflow.exception.DAOExceptionRemoli;
import it.web.safeflow.dao.LayerPersistenza;
import it.web.safeflow.dao.SocialDataRepository;
import it.web.safeflow.model.Notification;
import it.web.safeflow.model.NotificationLikeState;
import it.web.safeflow.utility.factory.FactoryLayerPersistenza;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

public class NotificationLikeControllerApplicativo {

    private static final Object LOCK = new Object();
    private static final Path LIKE_DIR = Path.of(System.getProperty("user.home"), ".safe-flow");
    private static final Path LIKE_STORE = LIKE_DIR.resolve("notification-likes.properties");
    private static final String LIKE_PREFIX = "like.";
    private static final String NOTIFIED_PREFIX = "notified.";

    public NotificationLikeState toggleTravelerLike(String notificationKey, String codiceFiscale) throws BrondiException {
        String key = normalizeNotificationKey(notificationKey);
        String cf = normalizeCf(codiceFiscale);
        Notification likedNotification = findLikableTravelerNotification(key);

        try {
            SocialDataRepository repository = new SocialDataRepository();
            NotificationLikeState state = repository.toggleReportLike(key, cf);
            if (state.isLikedByCurrentUser()) {
                notifyReportOwnerIfNeeded(repository, key, cf, likedNotification);
            }
            return state;
        } catch (DAOExceptionRemoli ignored) {
            // Legacy fallback for local databases that have not imported the social tables yet.
        }

        synchronized (LOCK) {
            Properties properties = loadProperties();
            String propertyKey = propertyKey(key, cf);
            boolean liked = !properties.containsKey(propertyKey);

            if (liked) {
                properties.setProperty(propertyKey, "true");
                notifyReportOwnerIfNeeded(properties, key, cf, likedNotification);
            } else {
                properties.remove(propertyKey);
            }

            storeProperties(properties);
            return new NotificationLikeState(countLikes(properties, key), liked);
        }
    }

    public Map<String, NotificationLikeState> statesFor(Set<String> notificationKeys, String codiceFiscale) throws BrondiException {
        Map<String, NotificationLikeState> states = new HashMap<>();
        if (notificationKeys == null || notificationKeys.isEmpty()) {
            return states;
        }

        String cf = codiceFiscale == null || codiceFiscale.isBlank()
                ? ""
                : codiceFiscale.trim().toUpperCase(Locale.ROOT);

        synchronized (LOCK) {
            try {
                return new SocialDataRepository().reportLikeStates(notificationKeys, cf);
            } catch (DAOExceptionRemoli ignored) {
                // Legacy fallback for local databases that have not imported the social tables yet.
            }
            Properties properties = loadProperties();
            for (String rawKey : notificationKeys) {
                if (rawKey == null || rawKey.isBlank()) {
                    continue;
                }
                String key = rawKey.trim();
                states.put(key, new NotificationLikeState(
                        countLikes(properties, key),
                        !cf.isBlank() && properties.containsKey(propertyKey(key, cf))
                ));
            }
        }
        return states;
    }

    public static String keyFor(Notification notification) {
        return keyFor(
                notification.getDate(),
                notification.getSenderCf(),
                notification.getMessage()
        );
    }

    public static String keyFor(Timestamp date, String senderCf, String message) {
        long timestampMillis = date == null ? 0L : (date.getTime() / 1000L) * 1000L;
        String rawKey = timestampMillis
                + "|"
                + (senderCf == null ? "" : senderCf.trim().toUpperCase(Locale.ROOT))
                + "|"
                + (message == null ? "" : message);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(rawKey.getBytes(StandardCharsets.UTF_8));
    }

    private Notification findLikableTravelerNotification(String notificationKey) throws BrondiException {
        LayerPersistenza layer = FactoryLayerPersistenza.createLayerPersistenza();
        try {
            List<Notification> notifications = layer.getMessagesRAM();
            for (Notification notification : notifications) {
                boolean travelerReport = "TRAVELER".equalsIgnoreCase(notification.getSenderRole());
                boolean approved = "APPROVED".equalsIgnoreCase(notification.getStatus());
                if (travelerReport && approved && notificationKey.equals(keyFor(notification))) {
                    return notification;
                }
            }
        } catch (DAOExceptionRemoli e) {
            throw new BrondiException("Unable to verify the report.", "LIKE_NOTIFICATION_LOOKUP", "Notification lookup failed", e);
        }

        throw new BrondiException("This report cannot be liked.", "LIKE_NOTIFICATION_INVALID", notificationKey);
    }

    private void notifyReportOwnerIfNeeded(Properties properties,
                                           String notificationKey,
                                           String likerCf,
                                           Notification likedNotification) throws BrondiException {
        String ownerCf = likedNotification.getSenderCf();
        if (ownerCf == null || ownerCf.isBlank() || ownerCf.equalsIgnoreCase(likerCf)) {
            return;
        }

        String notifiedKey = NOTIFIED_PREFIX + notificationKey + "." + likerCf;
        if (properties.containsKey(notifiedKey)) {
            return;
        }

        Notification ownerNotification = new Notification(
                "Someone liked your traveler report: " + summarize(likedNotification.getMessage()),
                new Timestamp(System.currentTimeMillis()),
                false,
                true,
                false,
                "APPROVED",
                "TRAVELER",
                likerCf,
                ownerCf,
                likedNotification.getCity(),
                likedNotification.isPickpocketAlert(),
                likedNotification.isFightAlert(),
                likedNotification.isCrowdAlert(),
                likedNotification.isGeneralAlert(),
                likedNotification.getStationName(),
                likedNotification.getSuspectClothing()
        );

        try {
            FactoryLayerPersistenza.createLayerPersistenza().sendMessage(ownerNotification);
            properties.setProperty(notifiedKey, "true");
        } catch (DAOExceptionRemoli e) {
            throw new BrondiException("Unable to notify the report owner.", "LIKE_OWNER_NOTIFICATION", "Owner notification failed", e);
        }
    }

    private void notifyReportOwnerIfNeeded(SocialDataRepository repository,
                                           String notificationKey,
                                           String likerCf,
                                           Notification likedNotification) throws BrondiException {
        String ownerCf = likedNotification.getSenderCf();
        if (ownerCf == null || ownerCf.isBlank() || ownerCf.equalsIgnoreCase(likerCf)) {
            return;
        }

        try {
            if (repository.isReportLikeNotificationSent(notificationKey, likerCf)) {
                return;
            }
        } catch (DAOExceptionRemoli e) {
            throw new BrondiException("Unable to notify the report owner.", "LIKE_OWNER_NOTIFICATION", "Owner notification marker failed", e);
        }

        Notification ownerNotification = new Notification(
                "Someone liked your traveler report: " + summarize(likedNotification.getMessage()),
                new Timestamp(System.currentTimeMillis()),
                false,
                true,
                false,
                "APPROVED",
                "TRAVELER",
                likerCf,
                ownerCf,
                likedNotification.getCity(),
                likedNotification.isPickpocketAlert(),
                likedNotification.isFightAlert(),
                likedNotification.isCrowdAlert(),
                likedNotification.isGeneralAlert(),
                likedNotification.getStationName(),
                likedNotification.getSuspectClothing()
        );

        try {
            FactoryLayerPersistenza.createLayerPersistenza().sendMessage(ownerNotification);
            repository.markReportLikeNotificationSent(notificationKey, likerCf);
        } catch (DAOExceptionRemoli e) {
            throw new BrondiException("Unable to notify the report owner.", "LIKE_OWNER_NOTIFICATION", "Owner notification failed", e);
        }
    }

    private String summarize(String message) {
        if (message == null || message.isBlank()) {
            return "your report";
        }

        String normalized = message.trim().replaceAll("\\s+", " ");
        if (normalized.length() <= 90) {
            return "\"" + normalized + "\"";
        }
        return "\"" + normalized.substring(0, 87) + "...\"";
    }

    private int countLikes(Properties properties, String notificationKey) {
        String prefix = LIKE_PREFIX + notificationKey + ".";
        int count = 0;
        for (Object key : properties.keySet()) {
            if (key instanceof String propertyKey && propertyKey.startsWith(prefix)) {
                count++;
            }
        }
        return count;
    }

    private String propertyKey(String notificationKey, String codiceFiscale) {
        return LIKE_PREFIX + notificationKey + "." + codiceFiscale;
    }

    private String normalizeNotificationKey(String notificationKey) throws BrondiException {
        if (notificationKey == null || notificationKey.trim().isEmpty()) {
            throw new BrondiException("Invalid report.", "LIKE_NOTIFICATION_KEY", "Missing notification key");
        }
        return notificationKey.trim();
    }

    private String normalizeCf(String codiceFiscale) throws BrondiException {
        if (codiceFiscale == null || codiceFiscale.trim().isEmpty()) {
            throw new BrondiException("Invalid user session.", "LIKE_SESSION", "Missing codice fiscale");
        }
        return codiceFiscale.trim().toUpperCase(Locale.ROOT);
    }

    private Properties loadProperties() throws BrondiException {
        Properties properties = new Properties();
        if (!Files.exists(LIKE_STORE)) {
            return properties;
        }

        try (InputStream input = Files.newInputStream(LIKE_STORE)) {
            properties.load(input);
            return properties;
        } catch (IOException e) {
            throw new BrondiException("Unable to read report likes.", "LIKE_IO", "Like store read error", e);
        }
    }

    private void storeProperties(Properties properties) throws BrondiException {
        try {
            Files.createDirectories(LIKE_DIR);
            try (OutputStream output = Files.newOutputStream(LIKE_STORE)) {
                properties.store(output, "Safe Flow notification likes");
            }
        } catch (IOException e) {
            throw new BrondiException("Unable to save report likes.", "LIKE_IO", "Like store write error", e);
        }
    }
}
