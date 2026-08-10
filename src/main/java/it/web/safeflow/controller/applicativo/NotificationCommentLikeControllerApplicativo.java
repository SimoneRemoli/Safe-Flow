package it.web.safeflow.controller.applicativo;

import it.web.safeflow.dao.LayerPersistenza;
import it.web.safeflow.dao.SocialDataRepository;
import it.web.safeflow.exception.BrondiException;
import it.web.safeflow.exception.DAOExceptionRemoli;
import it.web.safeflow.model.Notification;
import it.web.safeflow.model.NotificationComment;
import it.web.safeflow.model.NotificationLikeState;
import it.web.safeflow.utility.factory.FactoryLayerPersistenza;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Timestamp;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

public class NotificationCommentLikeControllerApplicativo {

    private static final Object LOCK = new Object();
    private static final Path LIKE_DIR = Path.of(System.getProperty("user.home"), ".safe-flow");
    private static final Path LIKE_STORE = LIKE_DIR.resolve("comment-likes.properties");
    private static final String LIKE_PREFIX = "like.";
    private static final String NOTIFIED_PREFIX = "notified.";

    public NotificationLikeState toggleTravelerCommentLike(String notificationKey,
                                                           String commentId,
                                                           String codiceFiscale) throws BrondiException {
        String key = normalizeNotificationKey(notificationKey);
        String id = normalizeCommentId(commentId);
        String cf = normalizeCf(codiceFiscale);

        NotificationCommentControllerApplicativo comments = new NotificationCommentControllerApplicativo();
        Notification report = comments.findCommentableTravelerNotificationByKey(key);
        NotificationComment likedComment = findComment(comments, key, id, cf);

        try {
            SocialDataRepository repository = new SocialDataRepository();
            NotificationLikeState state = repository.toggleCommentLike(id, cf);
            if (state.isLikedByCurrentUser()) {
                notifyCommentOwnerIfNeeded(repository, key, id, cf, report, likedComment);
            }
            return state;
        } catch (DAOExceptionRemoli ignored) {
            // Legacy fallback for local databases that have not imported the social tables yet.
        }

        synchronized (LOCK) {
            Properties properties = loadProperties();
            String propertyKey = propertyKey(id, cf);
            boolean liked = !properties.containsKey(propertyKey);

            if (liked) {
                properties.setProperty(propertyKey, "true");
                notifyCommentOwnerIfNeeded(properties, key, id, cf, report, likedComment);
            } else {
                properties.remove(propertyKey);
            }

            storeProperties(properties);
            return new NotificationLikeState(countLikes(properties, id), liked);
        }
    }

    public Map<String, NotificationLikeState> statesFor(Set<String> commentIds, String codiceFiscale)
            throws BrondiException {
        Map<String, NotificationLikeState> states = new HashMap<>();
        if (commentIds == null || commentIds.isEmpty()) {
            return states;
        }

        String cf = codiceFiscale == null || codiceFiscale.isBlank()
                ? ""
                : codiceFiscale.trim().toUpperCase(Locale.ROOT);

        synchronized (LOCK) {
            try {
                return new SocialDataRepository().commentLikeStates(commentIds, cf);
            } catch (DAOExceptionRemoli ignored) {
                // Legacy fallback for local databases that have not imported the social tables yet.
            }
            Properties properties = loadProperties();
            for (String rawId : commentIds) {
                if (rawId == null || rawId.isBlank()) {
                    continue;
                }
                String id = rawId.trim();
                states.put(id, new NotificationLikeState(
                        countLikes(properties, id),
                        !cf.isBlank() && properties.containsKey(propertyKey(id, cf))
                ));
            }
        }
        return states;
    }

    private NotificationComment findComment(NotificationCommentControllerApplicativo comments,
                                            String notificationKey,
                                            String commentId,
                                            String currentUserCf) throws BrondiException {
        List<NotificationComment> reportComments = comments
                .commentsFor(Collections.singleton(notificationKey), currentUserCf)
                .getOrDefault(notificationKey, Collections.emptyList());

        return reportComments.stream()
                .filter(comment -> commentId.equals(comment.getId()))
                .findFirst()
                .orElseThrow(() -> new BrondiException(
                        "This comment cannot be liked.",
                        "COMMENT_LIKE_INVALID",
                        commentId
                ));
    }

    private void notifyCommentOwnerIfNeeded(Properties properties,
                                            String notificationKey,
                                            String commentId,
                                            String likerCf,
                                            Notification report,
                                            NotificationComment likedComment) throws BrondiException {
        String ownerCf = likedComment.getAuthorCf();
        if (ownerCf == null || ownerCf.isBlank() || ownerCf.equalsIgnoreCase(likerCf)) {
            return;
        }

        String notifiedKey = NOTIFIED_PREFIX + commentId + "." + likerCf;
        if (properties.containsKey(notifiedKey)) {
            return;
        }

        Notification ownerNotification = new Notification(
                "Someone liked your comment: " + summarize(likedComment.getText()),
                new Timestamp(System.currentTimeMillis()),
                false,
                true,
                false,
                "APPROVED",
                "TRAVELER",
                likerCf,
                ownerCf,
                report.getCity(),
                report.isPickpocketAlert(),
                report.isFightAlert(),
                report.isCrowdAlert(),
                report.isGeneralAlert(),
                report.getStationName(),
                report.getSuspectClothing()
        );

        try {
            LayerPersistenza layer = FactoryLayerPersistenza.createLayerPersistenza();
            layer.sendMessage(ownerNotification);
            new NotificationCommentControllerApplicativo()
                    .storeTargetForInternalNotification(ownerNotification, notificationKey, commentId);
            properties.setProperty(notifiedKey, "true");
        } catch (DAOExceptionRemoli e) {
            throw new BrondiException("Unable to notify the comment owner.", "COMMENT_LIKE_OWNER_NOTIFICATION", "Owner notification failed", e);
        }
    }

    private void notifyCommentOwnerIfNeeded(SocialDataRepository repository,
                                            String notificationKey,
                                            String commentId,
                                            String likerCf,
                                            Notification report,
                                            NotificationComment likedComment) throws BrondiException {
        String ownerCf = likedComment.getAuthorCf();
        if (ownerCf == null || ownerCf.isBlank() || ownerCf.equalsIgnoreCase(likerCf)) {
            return;
        }

        try {
            if (repository.isCommentLikeNotificationSent(commentId, likerCf)) {
                return;
            }
        } catch (DAOExceptionRemoli e) {
            throw new BrondiException("Unable to notify the comment owner.", "COMMENT_LIKE_OWNER_NOTIFICATION", "Comment like marker failed", e);
        }

        Notification ownerNotification = new Notification(
                "Someone liked your comment: " + summarize(likedComment.getText()),
                new Timestamp(System.currentTimeMillis()),
                false,
                true,
                false,
                "APPROVED",
                "TRAVELER",
                likerCf,
                ownerCf,
                report.getCity(),
                report.isPickpocketAlert(),
                report.isFightAlert(),
                report.isCrowdAlert(),
                report.isGeneralAlert(),
                report.getStationName(),
                report.getSuspectClothing()
        );

        try {
            LayerPersistenza layer = FactoryLayerPersistenza.createLayerPersistenza();
            layer.sendMessage(ownerNotification);
            new NotificationCommentControllerApplicativo()
                    .storeTargetForInternalNotification(ownerNotification, notificationKey, commentId);
            repository.markCommentLikeNotificationSent(commentId, likerCf);
        } catch (DAOExceptionRemoli e) {
            throw new BrondiException("Unable to notify the comment owner.", "COMMENT_LIKE_OWNER_NOTIFICATION", "Owner notification failed", e);
        }
    }

    private String summarize(String message) {
        if (message == null || message.isBlank()) {
            return "your comment";
        }

        String normalized = message.trim().replaceAll("\\s+", " ");
        if (normalized.length() <= 90) {
            return "\"" + normalized + "\"";
        }
        return "\"" + normalized.substring(0, 87) + "...\"";
    }

    private int countLikes(Properties properties, String commentId) {
        String prefix = LIKE_PREFIX + commentId + ".";
        int count = 0;
        for (Object key : properties.keySet()) {
            if (key instanceof String propertyKey && propertyKey.startsWith(prefix)) {
                count++;
            }
        }
        return count;
    }

    private String propertyKey(String commentId, String codiceFiscale) {
        return LIKE_PREFIX + commentId + "." + codiceFiscale;
    }

    private String normalizeNotificationKey(String notificationKey) throws BrondiException {
        if (notificationKey == null || notificationKey.trim().isEmpty()) {
            throw new BrondiException("Invalid report.", "COMMENT_LIKE_NOTIFICATION_KEY", "Missing notification key");
        }

        String key = notificationKey.trim();
        if (!key.matches("[A-Za-z0-9_-]+")) {
            throw new BrondiException("Invalid report.", "COMMENT_LIKE_NOTIFICATION_KEY", "Invalid notification key");
        }
        return key;
    }

    private String normalizeCommentId(String commentId) throws BrondiException {
        if (commentId == null || commentId.trim().isEmpty()) {
            throw new BrondiException("Invalid comment.", "COMMENT_LIKE_COMMENT_ID", "Missing comment id");
        }

        String id = commentId.trim();
        if (!id.matches("[A-Za-z0-9_-]+")) {
            throw new BrondiException("Invalid comment.", "COMMENT_LIKE_COMMENT_ID", "Invalid comment id");
        }
        return id;
    }

    private String normalizeCf(String codiceFiscale) throws BrondiException {
        if (codiceFiscale == null || codiceFiscale.trim().isEmpty()) {
            throw new BrondiException("Invalid user session.", "COMMENT_LIKE_SESSION", "Missing codice fiscale");
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
            throw new BrondiException("Unable to read comment likes.", "COMMENT_LIKE_IO", "Like store read error", e);
        }
    }

    private void storeProperties(Properties properties) throws BrondiException {
        try {
            Files.createDirectories(LIKE_DIR);
            try (OutputStream output = Files.newOutputStream(LIKE_STORE)) {
                properties.store(output, "Safe Flow comment likes");
            }
        } catch (IOException e) {
            throw new BrondiException("Unable to save comment likes.", "COMMENT_LIKE_IO", "Like store write error", e);
        }
    }
}
