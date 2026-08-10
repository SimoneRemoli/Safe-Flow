package it.web.safeflow.controller.applicativo;

import it.web.safeflow.bean.MessageBean;
import it.web.safeflow.dao.LayerPersistenza;
import it.web.safeflow.exception.BrondiException;
import it.web.safeflow.exception.DAOExceptionRemoli;
import it.web.safeflow.model.Notification;
import it.web.safeflow.model.ReportImageAttachment;
import it.web.safeflow.utility.factory.FactoryLayerPersistenza;

import javax.servlet.http.Part;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

public class ReportImageControllerApplicativo {

    private static final Path REPORT_IMAGES_DIR = Path.of(System.getProperty("user.home"), ".safe-flow", "report-images");
    private static final Path REPORT_IMAGE_INDEX = Path.of(System.getProperty("user.home"), ".safe-flow", "report-image-index.properties");
    private static final int MAX_IMAGES_PER_REPORT = 5;
    private static final long MAX_IMAGE_SIZE = 5L * 1024L * 1024L;

    public void validateImages(Collection<Part> parts) throws BrondiException {
        validImageParts(parts);
    }

    public void saveImages(MessageBean message, Collection<Part> parts) throws BrondiException {
        List<Part> images = validImageParts(parts);
        if (images.isEmpty()) {
            return;
        }

        String notificationKey = NotificationLikeControllerApplicativo.keyFor(
                message.getDate(),
                message.getSenderCf(),
                message.getMessage()
        );
        Path reportDir = REPORT_IMAGES_DIR.resolve(notificationKey);

        try {
            Files.createDirectories(reportDir);
            int index = 1;
            for (Part image : images) {
                String extension = extensionFor(image);
                Path imagePath = reportDir.resolve(String.format("%03d%s", index++, extension));
                try (InputStream input = image.getInputStream()) {
                    Files.copy(input, imagePath, java.nio.file.StandardCopyOption.REPLACE_EXISTING);
                }
            }
            storeImageIndex(notificationKey, message.getSenderCf(), message.getMessage());
        } catch (IOException e) {
            throw new BrondiException("Unable to save report images.", "REPORT_IMAGE_IO", "Report image write error", e);
        }
    }

    public int imageCount(String notificationKey) {
        return listImageAttachments(notificationKey).size();
    }

    public List<ReportImageAttachment> viewableImages(String notificationKey, String viewerCf, String viewerRole)
            throws BrondiException {
        ensureCanView(notificationKey, viewerCf, viewerRole);
        return listImageAttachments(notificationKey);
    }

    public Path viewableImagePath(String notificationKey, String fileName, String viewerCf, String viewerRole)
            throws BrondiException {
        ensureCanView(notificationKey, viewerCf, viewerRole);
        String cleanFileName = sanitizeStoredFileName(fileName);
        Path reportDir = resolveReportDir(notificationKey).normalize();
        Path imagePath = reportDir.resolve(cleanFileName).normalize();

        if (!imagePath.startsWith(reportDir) || !Files.exists(imagePath)) {
            throw new BrondiException("Report image not found.", "REPORT_IMAGE_NOT_FOUND", cleanFileName);
        }
        return imagePath;
    }

    public String reportMessage(String notificationKey, String viewerCf, String viewerRole) throws BrondiException {
        return findViewableNotification(notificationKey, viewerCf, viewerRole)
                .map(Notification::getMessage)
                .orElse("");
    }

    private List<Part> validImageParts(Collection<Part> parts) throws BrondiException {
        List<Part> images = new ArrayList<>();
        if (parts == null) {
            return images;
        }

        for (Part part : parts) {
            if (part == null || !"reportImages".equals(part.getName()) || part.getSize() <= 0) {
                continue;
            }
            if (images.size() >= MAX_IMAGES_PER_REPORT) {
                throw new BrondiException("You can attach up to 5 images.", "REPORT_IMAGE_LIMIT", "Too many report images");
            }
            if (part.getSize() > MAX_IMAGE_SIZE) {
                throw new BrondiException("Each report image can be up to 5 MB.", "REPORT_IMAGE_SIZE", "Report image too large");
            }
            extensionFor(part);
            images.add(part);
        }
        return images;
    }

    private String extensionFor(Part part) throws BrondiException {
        String contentType = part.getContentType() == null ? "" : part.getContentType().toLowerCase(Locale.ROOT);
        return switch (contentType) {
            case "image/jpeg", "image/jpg" -> ".jpg";
            case "image/png" -> ".png";
            case "image/webp" -> ".webp";
            case "image/gif" -> ".gif";
            default -> throw new BrondiException("Attach only JPG, PNG, WEBP, or GIF images.", "REPORT_IMAGE_TYPE", contentType);
        };
    }

    private List<ReportImageAttachment> listImageAttachments(String notificationKey) {
        if (notificationKey == null || notificationKey.isBlank()) {
            return List.of();
        }

        Path reportDir = resolveReportDir(notificationKey);
        if (!Files.isDirectory(reportDir)) {
            return List.of();
        }

        try (var files = Files.list(reportDir)) {
            return files
                    .filter(Files::isRegularFile)
                    .sorted(Comparator.comparing(path -> path.getFileName().toString()))
                    .map(path -> new ReportImageAttachment(
                            path.getFileName().toString(),
                            contentTypeFor(path.getFileName().toString())
                    ))
                    .toList();
        } catch (IOException e) {
            return List.of();
        }
    }

    private Path resolveReportDir(String notificationKey) {
        Path exactPath = REPORT_IMAGES_DIR.resolve(notificationKey);
        if (Files.isDirectory(exactPath)) {
            return exactPath;
        }
        Optional<Path> indexedPath = indexedReportDir(notificationKey);
        if (indexedPath.isPresent()) {
            return indexedPath.get();
        }
        return legacyCompatibleReportDir(notificationKey).orElse(exactPath);
    }

    private Optional<Path> indexedReportDir(String notificationKey) {
        ReportKey requested = decodeReportKey(notificationKey).orElse(null);
        if (requested == null || !Files.exists(REPORT_IMAGE_INDEX)) {
            return Optional.empty();
        }

        java.util.Properties index = new java.util.Properties();
        try (InputStream input = Files.newInputStream(REPORT_IMAGE_INDEX)) {
            index.load(input);
            String folderKey = index.getProperty(identityIndexKey(requested.senderCf(), requested.message()));
            if (folderKey == null || folderKey.isBlank()) {
                return Optional.empty();
            }

            Path indexedPath = REPORT_IMAGES_DIR.resolve(folderKey);
            return Files.isDirectory(indexedPath) ? Optional.of(indexedPath) : Optional.empty();
        } catch (IOException e) {
            return Optional.empty();
        }
    }

    private Optional<Path> legacyCompatibleReportDir(String notificationKey) {
        ReportKey requested = decodeReportKey(notificationKey).orElse(null);
        if (requested == null || !Files.isDirectory(REPORT_IMAGES_DIR)) {
            return Optional.empty();
        }

        try (var directories = Files.list(REPORT_IMAGES_DIR)) {
            return directories
                    .filter(Files::isDirectory)
                    .filter(path -> decodeReportKey(path.getFileName().toString())
                            .map(requested::sameReportIgnoringMilliseconds)
                            .orElse(false)
                            || decodeReportKey(path.getFileName().toString())
                            .map(requested::sameReportIdentity)
                            .orElse(false))
                    .findFirst();
        } catch (IOException e) {
            return Optional.empty();
        }
    }

    private void storeImageIndex(String folderKey, String senderCf, String message) {
        java.util.Properties index = new java.util.Properties();
        try {
            Files.createDirectories(REPORT_IMAGES_DIR.getParent());
            if (Files.exists(REPORT_IMAGE_INDEX)) {
                try (InputStream input = Files.newInputStream(REPORT_IMAGE_INDEX)) {
                    index.load(input);
                }
            }
            index.setProperty(identityIndexKey(senderCf, message), folderKey);
            try (java.io.OutputStream output = Files.newOutputStream(REPORT_IMAGE_INDEX)) {
                index.store(output, "Safe Flow report image index");
            }
        } catch (IOException ignored) {
            // The image folder remains the source of truth; legacy scanning still works if this index cannot be written.
        }
    }

    private String identityIndexKey(String senderCf, String message) {
        String rawIdentity = (senderCf == null ? "" : senderCf.trim().toUpperCase(Locale.ROOT))
                + "|"
                + normalizeMessage(message);
        return "identity." + Base64.getUrlEncoder().withoutPadding().encodeToString(rawIdentity.getBytes(StandardCharsets.UTF_8));
    }

    private String normalizeMessage(String message) {
        return message == null ? "" : message.trim().replaceAll("\\s+", " ");
    }

    private Optional<ReportKey> decodeReportKey(String notificationKey) {
        if (notificationKey == null || notificationKey.isBlank()) {
            return Optional.empty();
        }

        try {
            String paddedKey = notificationKey + "=".repeat((4 - notificationKey.length() % 4) % 4);
            String decoded = new String(Base64.getUrlDecoder().decode(paddedKey), StandardCharsets.UTF_8);
            String[] parts = decoded.split("\\|", 3);
            if (parts.length != 3) {
                return Optional.empty();
            }
            return Optional.of(new ReportKey(
                    Long.parseLong(parts[0]),
                    parts[1].trim().toUpperCase(Locale.ROOT),
                    parts[2]
            ));
        } catch (IllegalArgumentException e) {
            return Optional.empty();
        }
    }

    private String contentTypeFor(String fileName) {
        String lower = fileName.toLowerCase(Locale.ROOT);
        if (lower.endsWith(".png")) {
            return "image/png";
        }
        if (lower.endsWith(".webp")) {
            return "image/webp";
        }
        if (lower.endsWith(".gif")) {
            return "image/gif";
        }
        return "image/jpeg";
    }

    private String sanitizeStoredFileName(String fileName) throws BrondiException {
        if (fileName == null || !fileName.matches("\\d{3}\\.(jpg|png|webp|gif)")) {
            throw new BrondiException("Report image not found.", "REPORT_IMAGE_NAME", "Invalid report image file name");
        }
        return fileName;
    }

    private void ensureCanView(String notificationKey, String viewerCf, String viewerRole) throws BrondiException {
        if (findViewableNotification(notificationKey, viewerCf, viewerRole).isEmpty()) {
            throw new BrondiException("Report images are not available.", "REPORT_IMAGE_FORBIDDEN", notificationKey);
        }
    }

    private Optional<Notification> findViewableNotification(String notificationKey, String viewerCf, String viewerRole)
            throws BrondiException {
        if (notificationKey == null || notificationKey.isBlank()) {
            return Optional.empty();
        }

        LayerPersistenza layer = FactoryLayerPersistenza.createLayerPersistenza();
        try {
            for (Notification notification : layer.getMessagesRAM()) {
                if (!notificationKey.equals(NotificationLikeControllerApplicativo.keyFor(notification))) {
                    continue;
                }

                boolean admin = "ADMIN".equalsIgnoreCase(viewerRole);
                boolean owner = viewerCf != null
                        && notification.getSenderCf() != null
                        && viewerCf.equalsIgnoreCase(notification.getSenderCf());
                boolean publicApproved = "APPROVED".equalsIgnoreCase(notification.getStatus())
                        && notification.getRecipientCf() == null;

                if (admin || owner || publicApproved) {
                    return Optional.of(notification);
                }
                return Optional.empty();
            }
        } catch (DAOExceptionRemoli e) {
            throw new BrondiException("Unable to load report images.", "REPORT_IMAGE_LOOKUP", "Report lookup failed", e);
        }

        return Optional.empty();
    }

    private record ReportKey(long timestampMillis, String senderCf, String message) {
        private boolean sameReportIgnoringMilliseconds(ReportKey other) {
            return (timestampMillis / 1000L) == (other.timestampMillis / 1000L)
                    && senderCf.equals(other.senderCf)
                    && normalize(message).equals(normalize(other.message));
        }

        private boolean sameReportIdentity(ReportKey other) {
            return senderCf.equals(other.senderCf)
                    && normalize(message).equals(normalize(other.message));
        }

        private String normalize(String value) {
            return value == null ? "" : value.trim().replaceAll("\\s+", " ");
        }
    }
}
