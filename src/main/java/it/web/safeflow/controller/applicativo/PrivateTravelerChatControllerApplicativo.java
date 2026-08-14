package it.web.safeflow.controller.applicativo;

import it.web.safeflow.dao.LayerPersistenza;
import it.web.safeflow.dao.SocialDataRepository;
import it.web.safeflow.exception.BrondiException;
import it.web.safeflow.exception.DAOExceptionRemoli;
import it.web.safeflow.model.Notification;
import it.web.safeflow.model.PrivateChatMessage;
import it.web.safeflow.model.PrivateChatThread;
import it.web.safeflow.model.UserProfileSummary;
import it.web.safeflow.utility.factory.FactoryLayerPersistenza;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public class PrivateTravelerChatControllerApplicativo {

    private static final Logger logger = LoggerFactory.getLogger(PrivateTravelerChatControllerApplicativo.class);
    private static final int MAX_MESSAGE_LENGTH = 600;
    private static final int MAX_RAW_STORAGE_KEY_LENGTH = 96;
    private static final Object FILE_LOCK = new Object();
    private static final Path CHAT_DIR = Path.of(System.getProperty("java.io.tmpdir"), "safe-flow", "private-chats");
    private static final Path LEGACY_CHAT_DIR = Path.of(System.getProperty("user.home"), ".safe-flow", "private-chats");

    public List<PrivateChatMessage> messages(String notificationKey,
                                             String currentUserCf,
                                             String otherTravelerCf) throws BrondiException {
        String key = normalizeNotificationKey(notificationKey);
        String storageKey = storageNotificationKey(key);
        String currentCf = normalizeCf(currentUserCf);
        String otherCf = normalizeCf(otherTravelerCf);
        ensureChatAllowed(key, currentCf, otherCf);

        try {
            new SocialDataRepository().markPrivateChatThreadRead(storageKey, currentCf, otherCf);
            List<PrivateChatMessage> messages = new SocialDataRepository()
                    .privateChatMessages(storageKey, currentCf, otherCf);
            enrichSenders(messages, currentCf, otherCf);
            markPrivateMessageNotificationsRead(key, currentCf, otherCf);
            return messages;
        } catch (DAOExceptionRemoli ignored) {
            List<PrivateChatMessage> messages = readMessagesFromFile(storageKey, currentCf, otherCf);
            enrichSenders(messages, currentCf, otherCf);
            markPrivateMessageNotificationsRead(key, currentCf, otherCf);
            return messages;
        }
    }

    public List<PrivateChatThread> threads(String currentUserCf) throws BrondiException {
        String currentCf = normalizeCf(currentUserCf);
        try {
            List<PrivateChatMessage> messages = new SocialDataRepository().privateChatMessagesForTraveler(currentCf);
            Map<String, Integer> unreadCounts = new SocialDataRepository().unreadPrivateChatCountsByThread(currentCf);
            List<PrivateChatThread> threads = buildThreads(currentCf, messages, unreadCounts);
            markUnreadThreadsFromNotifications(currentCf, threads);
            enrichThreadProfiles(threads);
            threads.sort(Comparator.comparing(PrivateChatThread::getLastMessageAt).reversed());
            return threads;
        } catch (DAOExceptionRemoli ignored) {
            List<PrivateChatThread> threads = buildThreads(currentCf, readAllMessagesFromFiles(currentCf), Map.of());
            markUnreadThreadsFromNotifications(currentCf, threads);
            enrichThreadProfiles(threads);
            threads.sort(Comparator.comparing(PrivateChatThread::getLastMessageAt).reversed());
            return threads;
        }
    }

    public int unreadCount(String currentUserCf) throws BrondiException {
        String currentCf = normalizeCf(currentUserCf);
        int unreadThreads = (int) threads(currentCf).stream()
                .filter(PrivateChatThread::isUnread)
                .count();
        return Math.max(unreadThreads, unreadPrivateNotificationThreadCount(currentCf));
    }

    public PrivateChatMessage send(String notificationKey,
                                   String senderCf,
                                   String recipientCf,
                                   String text) throws BrondiException {
        String key = normalizeNotificationKey(notificationKey);
        String storageKey = storageNotificationKey(key);
        String sender = normalizeCf(senderCf);
        String recipient = normalizeCf(recipientCf);
        String messageText = normalizeText(text);
        Notification report = ensureChatAllowed(key, sender, recipient);

        try {
            PrivateChatMessage message = new SocialDataRepository().savePrivateChatMessage(
                    storageKey,
                    sender,
                    recipient,
                    messageText,
                    new Timestamp(System.currentTimeMillis())
            );
            message.setSenderDisplayName("me");
            message.setSenderInitials("ME");
            notifyRecipientQuietly(report, key, sender, recipient, messageText);
            return message;
        } catch (DAOExceptionRemoli ignored) {
            PrivateChatMessage message = saveMessageToFile(storageKey, sender, recipient, messageText);
            message.setSenderDisplayName("me");
            message.setSenderInitials("ME");
            notifyRecipientQuietly(report, key, sender, recipient, messageText);
            return message;
        }
    }

    private void notifyRecipientQuietly(Notification report,
                                        String notificationKey,
                                        String senderCf,
                                        String recipientCf,
                                        String text) {
        try {
            notifyRecipient(report, notificationKey, senderCf, recipientCf, text);
        } catch (DAOExceptionRemoli e) {
            logger.warn("Private chat message saved, but internal notification could not be sent.", e);
        }
    }

    private Notification ensureChatAllowed(String notificationKey,
                                           String currentUserCf,
                                           String otherTravelerCf) throws BrondiException {
        if (currentUserCf.equalsIgnoreCase(otherTravelerCf)) {
            throw new BrondiException("You cannot open a private chat with yourself.", "PRIVATE_CHAT_SELF", "Self chat");
        }

        try {
            LayerPersistenza layer = FactoryLayerPersistenza.createLayerPersistenza();
            for (Notification notification : layer.getMessagesRAM()) {
                String publicKey = NotificationLikeControllerApplicativo.keyFor(notification);
                if (!notificationKey.equals(publicKey) && !notificationKey.equals(storageNotificationKey(publicKey))) {
                    continue;
                }
                String reportOwnerCf = notification.getSenderCf();
                boolean publicTravelerReport = "TRAVELER".equalsIgnoreCase(notification.getSenderRole())
                        && "APPROVED".equalsIgnoreCase(notification.getStatus())
                        && (notification.getRecipientCf() == null || notification.getRecipientCf().isBlank())
                        && reportOwnerCf != null
                        && !reportOwnerCf.isBlank();
                boolean currentUserIsOwner = reportOwnerCf != null && reportOwnerCf.equalsIgnoreCase(currentUserCf);
                boolean otherTravelerIsOwner = reportOwnerCf != null && reportOwnerCf.equalsIgnoreCase(otherTravelerCf);
                if (!publicTravelerReport || (!currentUserIsOwner && !otherTravelerIsOwner)) {
                    break;
                }
                return notification;
            }
        } catch (DAOExceptionRemoli e) {
            throw new BrondiException("Unable to verify the private chat.", "PRIVATE_CHAT_REPORT_LOOKUP", "Report lookup failed", e);
        }

        throw new BrondiException("This private chat is not available.", "PRIVATE_CHAT_NOT_ALLOWED", notificationKey);
    }

    private void notifyRecipient(Notification report,
                                 String notificationKey,
                                 String senderCf,
                                 String recipientCf,
                                 String text) throws DAOExceptionRemoli {
        boolean recipientOwnsReport = report.getSenderCf() != null
                && report.getSenderCf().equalsIgnoreCase(recipientCf);
        Notification notification = new Notification(
                (recipientOwnsReport ? "New private message about your report: " : "New private message: ")
                        + summarize(text),
                new Timestamp(System.currentTimeMillis()),
                false,
                true,
                false,
                "APPROVED",
                "TRAVELER",
                senderCf,
                recipientCf,
                report.getCity(),
                report.isPickpocketAlert(),
                report.isFightAlert(),
                report.isCrowdAlert(),
                report.isGeneralAlert(),
                report.getStationName(),
                report.getSuspectClothing()
        );

        LayerPersistenza layer = FactoryLayerPersistenza.createLayerPersistenza();
        layer.sendMessage(notification);
        try {
            new NotificationCommentControllerApplicativo()
                    .storeTargetForInternalNotification(notification, notificationKey, "chat-" + senderCf);
        } catch (BrondiException e) {
            throw new DAOExceptionRemoli("Unable to link the private chat notification.", e);
        }
    }

    private int unreadPrivateNotificationThreadCount(String currentCf) throws BrondiException {
        Set<String> unreadThreads = new HashSet<>();
        NotificationCommentControllerApplicativo targets = new NotificationCommentControllerApplicativo();
        try {
            for (Notification notification : FactoryLayerPersistenza.createLayerPersistenza().getMessagesRAM()) {
                if (!isUnreadPrivateMessageNotification(notification, currentCf)) {
                    continue;
                }

                String targetUrl = targets.targetUrlForInternalNotification(notification);
                if (targetUrl != null && targetUrl.startsWith("directMessages")) {
                    unreadThreads.add(targetUrl);
                } else {
                    unreadThreads.add(notification.getSenderCf());
                }
            }
        } catch (DAOExceptionRemoli e) {
            throw new BrondiException("Unable to count unread private messages.", "PRIVATE_CHAT_UNREAD_NOTIFICATIONS", "Unread private notification lookup failed", e);
        }
        return unreadThreads.size();
    }

    private void markUnreadThreadsFromNotifications(String currentCf,
                                                    List<PrivateChatThread> threads) throws BrondiException {
        if (threads.isEmpty()) {
            return;
        }

        Map<String, Set<String>> unreadTargetKeysBySender = new LinkedHashMap<>();
        Set<String> unreadSendersWithoutTargetKey = new HashSet<>();
        NotificationCommentControllerApplicativo targets = new NotificationCommentControllerApplicativo();
        try {
            for (Notification notification : FactoryLayerPersistenza.createLayerPersistenza().getMessagesRAM()) {
                if (!isUnreadPrivateMessageNotification(notification, currentCf)) {
                    continue;
                }

                String senderCf = notification.getSenderCf().toUpperCase(Locale.ROOT);
                String targetUrl = targets.targetUrlForInternalNotification(notification);
                String targetKey = queryParameter(targetUrl, "notificationKey");
                if (targetKey != null && !targetKey.isBlank()) {
                    unreadTargetKeysBySender
                            .computeIfAbsent(senderCf, ignored -> new HashSet<>())
                            .add(targetKey);
                } else {
                    unreadSendersWithoutTargetKey.add(senderCf);
                }
            }
        } catch (DAOExceptionRemoli e) {
            throw new BrondiException("Unable to mark unread private chats.", "PRIVATE_CHAT_UNREAD_THREAD_MARK", "Unread private notification lookup failed", e);
        }

        for (PrivateChatThread thread : threads) {
            String otherCf = thread.getOtherTravelerCf().toUpperCase(Locale.ROOT);
            Set<String> unreadTargetKeys = unreadTargetKeysBySender.getOrDefault(otherCf, Set.of());
            if (unreadSendersWithoutTargetKey.contains(otherCf) || unreadTargetKeys.contains(thread.getNotificationKey())) {
                thread.setUnread(true);
            }
        }
    }

    private void markPrivateMessageNotificationsRead(String notificationKey,
                                                     String currentCf,
                                                     String otherCf) {
        NotificationCommentControllerApplicativo targets = new NotificationCommentControllerApplicativo();
        try {
            LayerPersistenza layer = FactoryLayerPersistenza.createLayerPersistenza();
            for (Notification notification : layer.getMessagesRAM()) {
                if (!isUnreadPrivateMessageNotification(notification, currentCf)
                        || notification.getSenderCf() == null
                        || !notification.getSenderCf().equalsIgnoreCase(otherCf)) {
                    continue;
                }

                String targetUrl = targets.targetUrlForInternalNotification(notification);
                if (targetUrl != null
                        && targetUrl.contains("notificationKey=")
                        && !targetUrl.contains("notificationKey=" + notificationKey)) {
                    continue;
                }

                notification.setLetto(true);
                layer.markNotificationAsRead(notification);
            }
        } catch (DAOExceptionRemoli e) {
            logger.warn("Private chat opened, but internal private notifications could not be marked as read.", e);
        }
    }

    private boolean isUnreadPrivateMessageNotification(Notification notification, String currentCf) {
        return notification != null
                && !notification.isLetto()
                && "APPROVED".equalsIgnoreCase(notification.getStatus())
                && "TRAVELER".equalsIgnoreCase(notification.getSenderRole())
                && notification.getRecipientCf() != null
                && notification.getRecipientCf().equalsIgnoreCase(currentCf)
                && notification.getSenderCf() != null
                && !notification.getSenderCf().isBlank()
                && notification.getMessage() != null
                && notification.getMessage().startsWith("New private message");
    }

    private String queryParameter(String url, String name) {
        if (url == null || name == null || name.isBlank()) {
            return null;
        }

        int queryStart = url.indexOf('?');
        if (queryStart < 0 || queryStart == url.length() - 1) {
            return null;
        }

        String prefix = name + "=";
        String[] pairs = url.substring(queryStart + 1).split("&");
        for (String pair : pairs) {
            if (pair.startsWith(prefix)) {
                return pair.substring(prefix.length());
            }
        }
        return null;
    }

    private void enrichSenders(List<PrivateChatMessage> messages, String currentCf, String otherCf) {
        Set<String> cfs = new HashSet<>();
        cfs.add(currentCf);
        cfs.add(otherCf);

        Map<String, UserProfileSummary> profiles;
        try {
            profiles = new UserProfileControllerApplicativo().getProfilesByCodiceFiscale(cfs);
        } catch (BrondiException e) {
            profiles = Map.of();
        }

        for (PrivateChatMessage message : messages) {
            if (message.isCurrentUserSender()) {
                message.setSenderDisplayName("me");
                message.setSenderInitials("ME");
                continue;
            }

            UserProfileSummary profile = profiles.get(message.getSenderCf());
            if (profile == null) {
                message.setSenderDisplayName("Traveler");
                message.setSenderInitials("T");
            } else {
                message.setSenderDisplayName(profile.getDisplayName());
                message.setSenderInitials(profile.getInitials());
            }
        }
    }

    private List<PrivateChatThread> buildThreads(String currentCf,
                                                 List<PrivateChatMessage> messages,
                                                 Map<String, Integer> unreadCounts) throws BrondiException {
        Map<String, Notification> reports = publicTravelerReportsByKey();
        Map<String, PrivateChatMessage> latestByThread = new LinkedHashMap<>();

        for (PrivateChatMessage message : messages) {
            String otherCf = message.isCurrentUserSender() ? message.getRecipientCf() : message.getSenderCf();
            if (otherCf == null || otherCf.isBlank()) {
                continue;
            }
            String key = threadKey(message.getNotificationKey(), otherCf);
            PrivateChatMessage currentLatest = latestByThread.get(key);
            if (currentLatest == null || message.getCreatedAt().after(currentLatest.getCreatedAt())) {
                latestByThread.put(key, message);
            }
        }

        List<PrivateChatThread> threads = new ArrayList<>();
        for (PrivateChatMessage latest : latestByThread.values()) {
            String otherCf = latest.isCurrentUserSender() ? latest.getRecipientCf() : latest.getSenderCf();
            Notification report = reports.get(latest.getNotificationKey());
            if (report == null) {
                continue;
            }
            String publicKey = NotificationLikeControllerApplicativo.keyFor(report);
            String reportText = report.getMessage();
            String city = report.getCity();
            threads.add(new PrivateChatThread(
                    publicKey,
                    otherCf,
                    reportText,
                    city,
                    latest.getText(),
                    latest.getCreatedAt(),
                    unreadCounts.getOrDefault(threadKey(latest.getNotificationKey(), otherCf), 0)
            ));
        }
        return threads;
    }

    private Map<String, Notification> publicTravelerReportsByKey() throws BrondiException {
        Map<String, Notification> reports = new LinkedHashMap<>();
        try {
            for (Notification notification : FactoryLayerPersistenza.createLayerPersistenza().getMessagesRAM()) {
                if ("TRAVELER".equalsIgnoreCase(notification.getSenderRole())
                        && "APPROVED".equalsIgnoreCase(notification.getStatus())
                        && (notification.getRecipientCf() == null || notification.getRecipientCf().isBlank())) {
                    String publicKey = NotificationLikeControllerApplicativo.keyFor(notification);
                    reports.put(publicKey, notification);
                    reports.put(storageNotificationKey(publicKey), notification);
                }
            }
            return reports;
        } catch (DAOExceptionRemoli e) {
            throw new BrondiException("Unable to load private chat reports.", "PRIVATE_CHAT_REPORTS", "Report lookup failed", e);
        }
    }

    private void enrichThreadProfiles(List<PrivateChatThread> threads) {
        Set<String> cfs = new HashSet<>();
        for (PrivateChatThread thread : threads) {
            cfs.add(thread.getOtherTravelerCf());
        }

        Map<String, UserProfileSummary> profiles;
        try {
            profiles = new UserProfileControllerApplicativo().getProfilesByCodiceFiscale(cfs);
        } catch (BrondiException e) {
            profiles = Map.of();
        }

        for (PrivateChatThread thread : threads) {
            UserProfileSummary profile = profiles.get(thread.getOtherTravelerCf());
            if (profile == null) {
                thread.setOtherTravelerDisplayName("Traveler");
                thread.setOtherTravelerInitials("T");
            } else {
                thread.setOtherTravelerDisplayName(profile.getDisplayName());
                thread.setOtherTravelerInitials(profile.getInitials());
            }
        }
    }

    private String normalizeNotificationKey(String notificationKey) throws BrondiException {
        if (notificationKey == null || notificationKey.trim().isEmpty()) {
            throw new BrondiException("Invalid private chat.", "PRIVATE_CHAT_NOTIFICATION_KEY", "Missing notification key");
        }

        String key = notificationKey.trim();
        if (!key.matches("[A-Za-z0-9_-]+")) {
            throw new BrondiException("Invalid private chat.", "PRIVATE_CHAT_NOTIFICATION_KEY", "Invalid notification key");
        }
        return key;
    }

    private String storageNotificationKey(String notificationKey) throws BrondiException {
        if (notificationKey.length() <= MAX_RAW_STORAGE_KEY_LENGTH) {
            return notificationKey;
        }

        try {
            byte[] digest = MessageDigest.getInstance("SHA-256")
                    .digest(notificationKey.getBytes(StandardCharsets.UTF_8));
            return "chat_" + Base64.getUrlEncoder().withoutPadding().encodeToString(digest);
        } catch (NoSuchAlgorithmException e) {
            throw new BrondiException("Unable to prepare the private chat.", "PRIVATE_CHAT_STORAGE_KEY", "Missing SHA-256", e);
        }
    }

    private List<PrivateChatMessage> readMessagesFromFile(String notificationKey,
                                                          String currentCf,
                                                          String otherCf) throws BrondiException {
        synchronized (FILE_LOCK) {
            try {
                List<PrivateChatMessage> messages = new ArrayList<>();
                for (Path file : chatFiles(notificationKey, currentCf, otherCf)) {
                    if (!Files.exists(file)) {
                        continue;
                    }
                    for (String line : Files.readAllLines(file, StandardCharsets.UTF_8)) {
                        parseLine(line, currentCf).ifPresent(messages::add);
                    }
                }
                return messages;
            } catch (IOException e) {
                throw new BrondiException("Unable to load the private chat.", "PRIVATE_CHAT_FILE_LOAD", "Chat file read failed", e);
            }
        }
    }

    private List<PrivateChatMessage> readAllMessagesFromFiles(String currentCf) throws BrondiException {
        synchronized (FILE_LOCK) {
            List<PrivateChatMessage> messages = new ArrayList<>();
            for (Path directory : List.of(CHAT_DIR, LEGACY_CHAT_DIR)) {
                if (!Files.isDirectory(directory)) {
                    continue;
                }
                try (java.util.stream.Stream<Path> files = Files.list(directory)) {
                    for (Path file : files.filter(path -> path.getFileName().toString().endsWith(".tsv")).toList()) {
                        for (String line : Files.readAllLines(file, StandardCharsets.UTF_8)) {
                            parseLine(line, currentCf)
                                    .filter(message -> currentCf.equalsIgnoreCase(message.getSenderCf())
                                            || currentCf.equalsIgnoreCase(message.getRecipientCf()))
                                    .ifPresent(messages::add);
                        }
                    }
                } catch (IOException e) {
                    throw new BrondiException("Unable to load direct messages.", "PRIVATE_CHAT_FILE_INBOX", "Chat file inbox read failed", e);
                }
            }
            return messages;
        }
    }

    private PrivateChatMessage saveMessageToFile(String notificationKey,
                                                 String senderCf,
                                                 String recipientCf,
                                                 String text) throws BrondiException {
        synchronized (FILE_LOCK) {
            Timestamp createdAt = new Timestamp(System.currentTimeMillis());
            long id = createdAt.getTime();
            PrivateChatMessage message = new PrivateChatMessage(
                    id,
                    notificationKey,
                    senderCf,
                    recipientCf,
                    text,
                    createdAt,
                    true
            );

            String line = id
                    + "\t" + notificationKey
                    + "\t" + senderCf
                    + "\t" + recipientCf
                    + "\t" + createdAt.getTime()
                    + "\t" + Base64.getEncoder().encodeToString(text.getBytes(StandardCharsets.UTF_8));
            IOException failure = null;
            for (Path file : chatFiles(notificationKey, senderCf, recipientCf)) {
                try {
                    Files.createDirectories(file.getParent());
                    Files.writeString(
                            file,
                            line + System.lineSeparator(),
                            StandardCharsets.UTF_8,
                            StandardOpenOption.CREATE,
                            StandardOpenOption.APPEND
                    );
                    return message;
                } catch (IOException e) {
                    failure = e;
                }
            }
            throw new BrondiException("Unable to send the private message.", "PRIVATE_CHAT_FILE_SEND", "Chat file write failed", failure);
        }
    }

    private java.util.Optional<PrivateChatMessage> parseLine(String line, String currentCf) {
        if (line == null || line.isBlank()) {
            return java.util.Optional.empty();
        }

        String[] parts = line.split("\\t", 6);
        if (parts.length != 6) {
            return java.util.Optional.empty();
        }

        try {
            long id = Long.parseLong(parts[0]);
            String text = new String(Base64.getDecoder().decode(parts[5]), StandardCharsets.UTF_8);
            String senderCf = parts[2];
            return java.util.Optional.of(new PrivateChatMessage(
                    id,
                    parts[1],
                    senderCf,
                    parts[3],
                    text,
                    new Timestamp(Long.parseLong(parts[4])),
                    senderCf.equalsIgnoreCase(currentCf)
            ));
        } catch (IllegalArgumentException e) {
            return java.util.Optional.empty();
        }
    }

    private List<Path> chatFiles(String notificationKey, String firstCf, String secondCf) {
        String left = firstCf.compareToIgnoreCase(secondCf) <= 0 ? firstCf : secondCf;
        String right = firstCf.compareToIgnoreCase(secondCf) <= 0 ? secondCf : firstCf;
        String raw = notificationKey + "|" + left + "|" + right;
        String fileName = Base64.getUrlEncoder().withoutPadding()
                .encodeToString(raw.getBytes(StandardCharsets.UTF_8));
        return List.of(
                CHAT_DIR.resolve(fileName + ".tsv"),
                LEGACY_CHAT_DIR.resolve(fileName + ".tsv")
        );
    }

    private String threadKey(String notificationKey, String otherCf) {
        return (notificationKey == null ? "" : notificationKey) + "|" + otherCf.toUpperCase(Locale.ROOT);
    }

    private String normalizeCf(String codiceFiscale) throws BrondiException {
        String cf = codiceFiscale == null ? "" : codiceFiscale.trim().toUpperCase(Locale.ROOT);
        if (cf.isBlank()) {
            throw new BrondiException("Invalid user session.", "PRIVATE_CHAT_SESSION", "Missing codice fiscale");
        }
        return cf;
    }

    private String normalizeText(String text) throws BrondiException {
        String normalized = text == null ? "" : text.trim().replaceAll("[\\r\\n\\t]+", " ").replaceAll("\\s{2,}", " ");
        if (normalized.isBlank()) {
            throw new BrondiException("Write a message before sending.", "PRIVATE_CHAT_EMPTY", "Empty message");
        }
        if (normalized.length() > MAX_MESSAGE_LENGTH) {
            throw new BrondiException("Private messages cannot exceed 600 characters.", "PRIVATE_CHAT_TOO_LONG", "Message too long");
        }
        return normalized;
    }

    private String summarize(String message) {
        if (message == null || message.isBlank()) {
            return "a new message";
        }

        String normalized = message.trim().replaceAll("\\s+", " ");
        if (normalized.length() <= 90) {
            return "\"" + normalized + "\"";
        }
        return "\"" + normalized.substring(0, 87) + "...\"";
    }
}
