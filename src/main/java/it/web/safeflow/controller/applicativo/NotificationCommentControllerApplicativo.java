package it.web.safeflow.controller.applicativo;

import it.web.safeflow.dao.LayerPersistenza;
import it.web.safeflow.dao.SocialDataRepository;
import it.web.safeflow.exception.BrondiException;
import it.web.safeflow.exception.DAOExceptionRemoli;
import it.web.safeflow.model.Notification;
import it.web.safeflow.model.NotificationComment;
import it.web.safeflow.model.UserProfileSummary;
import it.web.safeflow.utility.factory.FactoryLayerPersistenza;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.UUID;

public class NotificationCommentControllerApplicativo {

    private static final Object LOCK = new Object();
    private static final Path COMMENT_DIR = Path.of(System.getProperty("user.home"), ".safe-flow", "report-comments");
    private static final Path COMMENT_NOTIFICATION_TARGET_STORE = Path.of(System.getProperty("user.home"), ".safe-flow", "comment-notification-targets.properties");
    private static final int MAX_COMMENT_LENGTH = 600;
    private static final String TRAVELER_ROLE = "TRAVELER";

    public NotificationComment addTravelerComment(String notificationKey, String authorCf, String text) throws BrondiException {
        return addTravelerComment(notificationKey, authorCf, text, null);
    }

    public NotificationComment addTravelerComment(String notificationKey,
                                                  String authorCf,
                                                  String text,
                                                  String parentCommentId) throws BrondiException {
        String key = normalizeNotificationKey(notificationKey);
        String cf = normalizeCf(authorCf);
        String commentText = normalizeText(text);
        Notification report = findCommentableTravelerNotification(key);

        NotificationComment comment;
        NotificationComment parentComment = null;

        synchronized (LOCK) {
            List<NotificationComment> existingComments = storedComments(key);
            parentComment = findParentComment(existingComments, normalizeOptionalId(parentCommentId));
            comment = new NotificationComment(
                    UUID.randomUUID().toString(),
                    key,
                    cf,
                    parentComment == null ? null : parentComment.getId(),
                    parentComment == null ? null : parentComment.getAuthorCf(),
                    commentText,
                    new Timestamp(System.currentTimeMillis())
            );

            try {
                new SocialDataRepository().saveComment(comment);
            } catch (DAOExceptionRemoli dbError) {
                try {
                    Files.createDirectories(COMMENT_DIR);
                    Files.writeString(
                            commentFile(key),
                            serialize(comment) + System.lineSeparator(),
                            StandardCharsets.UTF_8,
                            StandardOpenOption.CREATE,
                            StandardOpenOption.APPEND
                    );
                } catch (IOException e) {
                    throw new BrondiException("Unable to save the comment.", "COMMENT_IO", "Comment store write error", e);
                }
            }
        }

        notifyInterestedTravelers(report, comment, parentComment);
        enrichAuthors(List.of(comment), cf);
        return comment;
    }

    public Map<String, List<NotificationComment>> commentsFor(Set<String> notificationKeys, String currentUserCf)
            throws BrondiException {
        Map<String, List<NotificationComment>> commentsByKey = new HashMap<>();
        if (notificationKeys == null || notificationKeys.isEmpty()) {
            return commentsByKey;
        }

        synchronized (LOCK) {
            for (String rawKey : notificationKeys) {
                if (rawKey == null || rawKey.isBlank()) {
                    continue;
                }
                String key = normalizeNotificationKey(rawKey);
                commentsByKey.put(key, storedComments(key));
            }
        }

        List<NotificationComment> allComments = commentsByKey.values()
                .stream()
                .flatMap(List::stream)
                .toList();
        enrichAuthors(allComments, currentUserCf);
        return commentsByKey;
    }

    public Notification findCommentableTravelerNotificationByKey(String notificationKey) throws BrondiException {
        LayerPersistenza layer = FactoryLayerPersistenza.createLayerPersistenza();
        try {
            for (Notification notification : layer.getMessagesRAM()) {
                boolean travelerReport = TRAVELER_ROLE.equalsIgnoreCase(notification.getSenderRole());
                boolean approved = "APPROVED".equalsIgnoreCase(notification.getStatus());
                boolean publicReport = notification.getRecipientCf() == null || notification.getRecipientCf().isBlank();
                if (travelerReport
                        && approved
                        && publicReport
                        && notificationKey.equals(NotificationLikeControllerApplicativo.keyFor(notification))) {
                    return notification;
                }
            }
        } catch (DAOExceptionRemoli e) {
            throw new BrondiException("Unable to verify the report.", "COMMENT_NOTIFICATION_LOOKUP", "Notification lookup failed", e);
        }

        throw new BrondiException("This report cannot be commented.", "COMMENT_NOTIFICATION_INVALID", notificationKey);
    }

    private Notification findCommentableTravelerNotification(String notificationKey) throws BrondiException {
        return findCommentableTravelerNotificationByKey(notificationKey);
    }

    private List<NotificationComment> readComments(String notificationKey) throws BrondiException {
        Path file = commentFile(notificationKey);
        if (!Files.exists(file)) {
            return new ArrayList<>();
        }

        try {
            List<NotificationComment> comments = new ArrayList<>();
            for (String line : Files.readAllLines(file, StandardCharsets.UTF_8)) {
                parseLine(notificationKey, line).ifPresent(comments::add);
            }
            comments.sort(Comparator.comparing(NotificationComment::getCreatedAt));
            return comments;
        } catch (IOException e) {
            throw new BrondiException("Unable to read report comments.", "COMMENT_IO", "Comment store read error", e);
        }
    }

    private List<NotificationComment> storedComments(String notificationKey) throws BrondiException {
        Map<String, NotificationComment> commentsById = new LinkedHashMap<>();
        boolean databaseRead = false;
        try {
            for (NotificationComment comment : new SocialDataRepository().commentsFor(notificationKey)) {
                commentsById.put(comment.getId(), comment);
            }
            databaseRead = true;
        } catch (DAOExceptionRemoli ignored) {
            // Fall back to the legacy file store when the social tables are not installed yet.
        }

        try {
            for (NotificationComment comment : readComments(notificationKey)) {
                commentsById.putIfAbsent(comment.getId(), comment);
            }
        } catch (BrondiException legacyError) {
            if (!databaseRead) {
                throw legacyError;
            }
        }

        List<NotificationComment> comments = new ArrayList<>(commentsById.values());
        comments.sort(Comparator.comparing(NotificationComment::getCreatedAt));
        return comments;
    }

    private java.util.Optional<NotificationComment> parseLine(String notificationKey, String line) {
        if (line == null || line.isBlank()) {
            return java.util.Optional.empty();
        }

        String[] parts = line.split("\\t", -1);
        if (parts.length != 4 && parts.length != 6) {
            return java.util.Optional.empty();
        }

        try {
            String parentCommentId = parts.length == 6 && !parts[3].isBlank() ? parts[3] : null;
            String replyToCf = parts.length == 6 && !parts[4].isBlank() ? parts[4] : null;
            String encodedText = parts.length == 6 ? parts[5] : parts[3];
            String text = new String(Base64.getUrlDecoder().decode(padBase64(encodedText)), StandardCharsets.UTF_8);
            return java.util.Optional.of(new NotificationComment(
                    parts[0],
                    notificationKey,
                    parts[2],
                    parentCommentId,
                    replyToCf,
                    text,
                    new Timestamp(Long.parseLong(parts[1]))
            ));
        } catch (IllegalArgumentException e) {
            return java.util.Optional.empty();
        }
    }

    private void enrichAuthors(List<NotificationComment> comments, String currentUserCf) {
        if (comments == null || comments.isEmpty()) {
            return;
        }

        Set<String> profileCfs = new HashSet<>();
        for (NotificationComment comment : comments) {
            if (comment.getAuthorCf() != null && !comment.getAuthorCf().isBlank()) {
                profileCfs.add(comment.getAuthorCf());
            }
            if (comment.getReplyToCf() != null && !comment.getReplyToCf().isBlank()) {
                profileCfs.add(comment.getReplyToCf());
            }
        }

        Map<String, UserProfileSummary> profiles;
        try {
            profiles = new UserProfileControllerApplicativo().getProfilesByCodiceFiscale(profileCfs);
        } catch (BrondiException e) {
            profiles = Collections.emptyMap();
        }

        String currentCf = currentUserCf == null ? "" : currentUserCf.trim().toUpperCase(Locale.ROOT);
        for (NotificationComment comment : comments) {
            String authorCf = normalizeCfQuietly(comment.getAuthorCf());
            boolean currentUserAuthor = !currentCf.isBlank() && currentCf.equals(authorCf);
            UserProfileSummary profile = profiles.get(authorCf);

            comment.setCurrentUserAuthor(currentUserAuthor);
            if (profile != null) {
                comment.setAuthorDisplayName(currentUserAuthor ? "me" : profile.getDisplayName());
                comment.setAuthorInitials(profile.getInitials());
                comment.setAuthorAvatarPresent(profile.isAvatarPresent());
            } else {
                comment.setAuthorDisplayName(currentUserAuthor ? "me" : "Unknown traveler");
                comment.setAuthorInitials(currentUserAuthor ? "ME" : "U");
                comment.setAuthorAvatarPresent(false);
            }

            String replyToCf = normalizeCfQuietly(comment.getReplyToCf());
            if (!replyToCf.isBlank()) {
                boolean replyToCurrentUser = !currentCf.isBlank() && currentCf.equals(replyToCf);
                UserProfileSummary replyProfile = profiles.get(replyToCf);
                comment.setReplyToDisplayName(replyToCurrentUser
                        ? "me"
                        : replyProfile == null ? "Unknown traveler" : replyProfile.getDisplayName());
            }
        }
    }

    private String serialize(NotificationComment comment) {
        return comment.getId()
                + "\t"
                + comment.getCreatedAt().getTime()
                + "\t"
                + normalizeCfQuietly(comment.getAuthorCf())
                + "\t"
                + blankIfNull(comment.getParentCommentId())
                + "\t"
                + normalizeCfQuietly(comment.getReplyToCf())
                + "\t"
                + Base64.getUrlEncoder().withoutPadding()
                .encodeToString(comment.getText().getBytes(StandardCharsets.UTF_8));
    }

    private NotificationComment findParentComment(List<NotificationComment> comments, String parentCommentId)
            throws BrondiException {
        if (parentCommentId == null || parentCommentId.isBlank()) {
            return null;
        }

        return comments.stream()
                .filter(comment -> parentCommentId.equals(comment.getId()))
                .findFirst()
                .orElseThrow(() -> new BrondiException(
                        "The comment you are replying to is no longer available.",
                        "COMMENT_PARENT_NOT_FOUND",
                        parentCommentId
                ));
    }

    private void notifyInterestedTravelers(Notification report,
                                           NotificationComment comment,
                                           NotificationComment parentComment) throws BrondiException {
        Set<String> recipients = new HashSet<>();
        String authorCf = normalizeCfQuietly(comment.getAuthorCf());
        String reportOwnerCf = normalizeCfQuietly(report.getSenderCf());
        String replyOwnerCf = parentComment == null ? "" : normalizeCfQuietly(parentComment.getAuthorCf());

        if (!reportOwnerCf.isBlank() && !reportOwnerCf.equals(authorCf)) {
            recipients.add(reportOwnerCf);
        }
        if (!replyOwnerCf.isBlank() && !replyOwnerCf.equals(authorCf)) {
            recipients.add(replyOwnerCf);
        }

        if (recipients.isEmpty()) {
            return;
        }

        for (String recipientCf : recipients) {
            boolean replyNotification = parentComment != null && recipientCf.equals(replyOwnerCf);
            sendCommentNotification(report, comment, recipientCf, replyNotification);
        }
    }

    private void sendCommentNotification(Notification report,
                                         NotificationComment comment,
                                         String recipientCf,
                                         boolean replyNotification) throws BrondiException {
        Notification notification = new Notification(
                (replyNotification ? "Someone replied to your comment: " : "Someone commented on your traveler report: ")
                        + summarize(comment.getText()),
                new Timestamp(System.currentTimeMillis()),
                false,
                true,
                false,
                "APPROVED",
                TRAVELER_ROLE,
                comment.getAuthorCf(),
                recipientCf,
                report.getCity(),
                report.isPickpocketAlert(),
                report.isFightAlert(),
                report.isCrowdAlert(),
                report.isGeneralAlert(),
                report.getStationName(),
                report.getSuspectClothing()
        );

        try {
            FactoryLayerPersistenza.createLayerPersistenza().sendMessage(notification);
            storeNotificationTarget(notification, report, comment);
        } catch (DAOExceptionRemoli e) {
            throw new BrondiException("Unable to notify the traveler.", "COMMENT_OWNER_NOTIFICATION", "Comment notification failed", e);
        }
    }

    public String targetUrlForInternalNotification(Notification notification) {
        if (notification == null) {
            return null;
        }

        try {
            String internalNotificationKey = NotificationLikeControllerApplicativo.keyFor(notification);
            String value;
            try {
                value = new SocialDataRepository()
                        .internalNotificationTarget(internalNotificationKey)
                        .orElse(null);
            } catch (DAOExceptionRemoli ignored) {
                value = null;
            }
            if (value == null || value.isBlank()) {
                value = loadTargetProperties().getProperty(internalNotificationKey);
            }
            if (value == null || value.isBlank()) {
                if (isPrivateChatNotification(notification)) {
                    return notification.getSenderCf() == null || notification.getSenderCf().isBlank()
                            ? "directMessages"
                            : "directMessages?travelerCf=" + notification.getSenderCf();
                }
                return inferredInternalTargetUrl(notification);
            }

            String[] parts = value.split("\\|", 2);
            if (parts.length == 0 || parts[0].isBlank()) {
                return null;
            }

            if (isPrivateChatNotification(notification)) {
                String travelerCf = parts.length == 2 && parts[1].startsWith("chat-")
                        ? parts[1].substring("chat-".length())
                        : notification.getSenderCf();
                return travelerCf == null || travelerCf.isBlank()
                        ? "directMessages"
                        : "directMessages?notificationKey=" + parts[0] + "&travelerCf=" + travelerCf;
            }

            StringBuilder url = new StringBuilder("viewNotifications?notificationKey=").append(parts[0]);
            if (parts.length == 2 && !parts[1].isBlank()) {
                url.append("&commentId=").append(parts[1]);
            }
            return url.toString();
        } catch (BrondiException e) {
            return null;
        }
    }

    private String inferredInternalTargetUrl(Notification internalNotification) throws BrondiException {
        if (!isApprovalNotification(internalNotification)
                && !isTravelerReportActivityNotification(internalNotification)
                && !isAdminAlertNotification(internalNotification)) {
            return null;
        }

        Notification bestMatch = null;
        int bestScore = Integer.MIN_VALUE;
        LayerPersistenza layer = FactoryLayerPersistenza.createLayerPersistenza();
        try {
            for (Notification candidate : layer.getMessagesRAM()) {
                if (!isApprovedPublicTargetFor(candidate, internalNotification)) {
                    continue;
                }

                int score = approvalMatchScore(internalNotification, candidate);
                if (bestMatch == null
                        || score > bestScore
                        || score == bestScore && candidate.getDate().after(bestMatch.getDate())) {
                    bestMatch = candidate;
                    bestScore = score;
                }
            }
        } catch (DAOExceptionRemoli e) {
            throw new BrondiException("Unable to infer approval target.", "COMMENT_TARGET_INFER", "Approval target lookup failed", e);
        }

        return bestMatch == null
                ? null
                : "viewNotifications?notificationKey=" + NotificationLikeControllerApplicativo.keyFor(bestMatch);
    }

    private boolean isAdminAlertNotification(Notification notification) {
        return notification != null
                && "ADMIN".equalsIgnoreCase(notification.getSenderRole())
                && notification.getRecipientCf() != null
                && !notification.getRecipientCf().isBlank()
                && notification.getMessage() != null
                && notification.getMessage().startsWith("New Safe Flow admin alert:");
    }

    private boolean isTravelerReportActivityNotification(Notification notification) {
        if (notification == null
                || notification.getRecipientCf() == null
                || notification.getRecipientCf().isBlank()
                || notification.getMessage() == null) {
            return false;
        }

        String message = notification.getMessage();
        return TRAVELER_ROLE.equalsIgnoreCase(notification.getSenderRole())
                && (message.startsWith("Someone liked your traveler report:")
                || message.startsWith("Someone commented on your traveler report:")
                || message.startsWith("Someone replied to your comment:")
                || message.startsWith("Someone liked your comment:"));
    }

    private boolean isPrivateChatNotification(Notification notification) {
        return notification != null
                && TRAVELER_ROLE.equalsIgnoreCase(notification.getSenderRole())
                && notification.getRecipientCf() != null
                && !notification.getRecipientCf().isBlank()
                && notification.getSenderCf() != null
                && !notification.getSenderCf().isBlank()
                && notification.getMessage() != null
                && notification.getMessage().startsWith("New private message");
    }

    private boolean isApprovalNotification(Notification notification) {
        return notification != null
                && "ADMIN".equalsIgnoreCase(notification.getSenderRole())
                && notification.getRecipientCf() != null
                && !notification.getRecipientCf().isBlank()
                && notification.getMessage() != null
                && notification.getMessage().startsWith("Your traveler report has been approved");
    }

    private boolean isApprovedPublicTargetFor(Notification candidate, Notification internalNotification) {
        if (candidate == null
                || !"APPROVED".equalsIgnoreCase(candidate.getStatus())
                || candidate.getRecipientCf() != null) {
            return false;
        }

        if (isAdminAlertNotification(internalNotification)) {
            return "ADMIN".equalsIgnoreCase(candidate.getSenderRole());
        }

        return TRAVELER_ROLE.equalsIgnoreCase(candidate.getSenderRole())
                && "APPROVED".equalsIgnoreCase(candidate.getStatus())
                && candidate.getSenderCf() != null
                && !candidate.getSenderCf().isBlank();
    }

    private int approvalMatchScore(Notification internalNotification, Notification candidate) {
        int score = 0;
        score += adminAlertMatchesMessage(internalNotification, candidate) ? 100 : 0;
        score += sameText(internalNotification.getCity(), candidate.getCity()) ? 20 : 0;
        score += sameText(internalNotification.getStationName(), candidate.getStationName()) ? 35 : 0;
        score += sameText(internalNotification.getSuspectClothing(), candidate.getSuspectClothing()) ? 10 : 0;
        score += internalNotification.isPickpocketAlert() == candidate.isPickpocketAlert() ? 8 : 0;
        score += internalNotification.isFightAlert() == candidate.isFightAlert() ? 8 : 0;
        score += internalNotification.isCrowdAlert() == candidate.isCrowdAlert() ? 8 : 0;
        score += internalNotification.isGeneralAlert() == candidate.isGeneralAlert() ? 8 : 0;
        score += sameText(internalNotification.getRecipientCf(), candidate.getSenderCf()) ? 25 : 0;
        if (internalNotification.getDate() != null && candidate.getDate() != null
                && !candidate.getDate().after(internalNotification.getDate())) {
            score += 5;
        }
        return score;
    }

    private boolean adminAlertMatchesMessage(Notification internalNotification, Notification candidate) {
        if (!isAdminAlertNotification(internalNotification)
                || internalNotification.getMessage() == null
                || candidate == null
                || candidate.getMessage() == null) {
            return false;
        }

        String alertMessage = internalNotification.getMessage().trim();
        String prefix = "New Safe Flow admin alert:";
        String extracted = alertMessage.substring(prefix.length()).trim();
        if (extracted.startsWith("\"") && extracted.endsWith("\"") && extracted.length() >= 2) {
            extracted = extracted.substring(1, extracted.length() - 1);
        }
        return sameText(extracted, candidate.getMessage());
    }

    private boolean sameText(String left, String right) {
        String normalizedLeft = left == null ? "" : left.trim();
        String normalizedRight = right == null ? "" : right.trim();
        return normalizedLeft.equalsIgnoreCase(normalizedRight);
    }

    private void storeNotificationTarget(Notification internalNotification,
                                         Notification report,
                                         NotificationComment comment) throws BrondiException {
        storeTargetForInternalNotification(
                internalNotification,
                NotificationLikeControllerApplicativo.keyFor(report),
                comment.getId()
        );
    }

    public void storeTargetForInternalNotification(Notification internalNotification,
                                                   String reportNotificationKey,
                                                   String commentId) throws BrondiException {
        String key = normalizeNotificationKey(reportNotificationKey);
        String id = normalizeOptionalId(commentId);
        synchronized (LOCK) {
            String internalNotificationKey = NotificationLikeControllerApplicativo.keyFor(internalNotification);
            try {
                new SocialDataRepository().saveInternalNotificationTarget(internalNotificationKey, key, id);
            } catch (DAOExceptionRemoli ignored) {
                // The legacy file store below remains a second copy when the DB table is not available.
            }
            Properties properties = loadTargetProperties();
            properties.setProperty(
                    internalNotificationKey,
                    key + "|" + blankIfNull(id)
            );
            storeTargetProperties(properties);
        }
    }

    private Properties loadTargetProperties() throws BrondiException {
        Properties properties = new Properties();
        if (!Files.exists(COMMENT_NOTIFICATION_TARGET_STORE)) {
            return properties;
        }

        try (InputStream input = Files.newInputStream(COMMENT_NOTIFICATION_TARGET_STORE)) {
            properties.load(input);
            return properties;
        } catch (IOException e) {
            throw new BrondiException("Unable to read comment notification targets.", "COMMENT_TARGET_IO", "Target store read error", e);
        }
    }

    private void storeTargetProperties(Properties properties) throws BrondiException {
        try {
            Files.createDirectories(COMMENT_NOTIFICATION_TARGET_STORE.getParent());
            try (OutputStream output = Files.newOutputStream(COMMENT_NOTIFICATION_TARGET_STORE)) {
                properties.store(output, "Safe Flow comment notification targets");
            }
        } catch (IOException e) {
            throw new BrondiException("Unable to save comment notification targets.", "COMMENT_TARGET_IO", "Target store write error", e);
        }
    }

    private String summarize(String message) {
        if (message == null || message.isBlank()) {
            return "a new comment";
        }

        String normalized = message.trim().replaceAll("\\s+", " ");
        if (normalized.length() <= 90) {
            return "\"" + normalized + "\"";
        }
        return "\"" + normalized.substring(0, 87) + "...\"";
    }

    private Path commentFile(String notificationKey) {
        return COMMENT_DIR.resolve(notificationKey + ".tsv");
    }

    private String normalizeNotificationKey(String notificationKey) throws BrondiException {
        if (notificationKey == null || notificationKey.trim().isEmpty()) {
            throw new BrondiException("Invalid report.", "COMMENT_NOTIFICATION_KEY", "Missing notification key");
        }

        String key = notificationKey.trim();
        if (!key.matches("[A-Za-z0-9_-]+")) {
            throw new BrondiException("Invalid report.", "COMMENT_NOTIFICATION_KEY", "Invalid notification key");
        }
        return key;
    }

    private String normalizeCf(String codiceFiscale) throws BrondiException {
        String cf = normalizeCfQuietly(codiceFiscale);
        if (cf.isBlank()) {
            throw new BrondiException("Invalid user session.", "COMMENT_SESSION", "Missing codice fiscale");
        }
        return cf;
    }

    private String normalizeCfQuietly(String codiceFiscale) {
        return codiceFiscale == null ? "" : codiceFiscale.trim().toUpperCase(Locale.ROOT);
    }

    private String normalizeOptionalId(String value) {
        return value == null || value.trim().isBlank() ? null : value.trim();
    }

    private String blankIfNull(String value) {
        return value == null ? "" : value;
    }

    private String normalizeText(String text) throws BrondiException {
        String normalized = text == null ? "" : text.trim().replaceAll("[\\r\\n\\t]+", " ").replaceAll("\\s{2,}", " ");
        if (normalized.isBlank()) {
            throw new BrondiException("Write a comment before posting.", "COMMENT_TEXT_EMPTY", "Empty comment");
        }
        if (normalized.length() > MAX_COMMENT_LENGTH) {
            throw new BrondiException("Comments cannot exceed 600 characters.", "COMMENT_TEXT_TOO_LONG", "Comment too long");
        }
        return normalized;
    }

    private String padBase64(String value) {
        return value + "=".repeat((4 - value.length() % 4) % 4);
    }
}
