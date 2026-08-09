package it.web.routex.controller.applicativo;

import it.web.routex.exception.BrondiException;
import it.web.routex.exception.DAOExceptionRemoli;
import it.web.routex.dao.LayerPersistenza;
import it.web.routex.model.Credentials;
import it.web.routex.model.Notification;
import it.web.routex.model.UserProfile;
import it.web.routex.model.UserProfileSummary;
import it.web.routex.model.UserProfileStats;
import it.web.routex.utility.factory.FactoryLayerPersistenza;

import javax.servlet.http.Part;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.LinkedHashSet;

public class UserProfileControllerApplicativo {

    private static final long MAX_AVATAR_SIZE = 2L * 1024L * 1024L;
    private static final Path PROFILE_DIR = Path.of(System.getProperty("user.home"), ".safe-flow", "profiles");
    private static final Path PROFILE_STORE = PROFILE_DIR.resolve("profiles.properties");

    public UserProfile getProfile(String codiceFiscale) throws BrondiException {
        String cf = normalizeCf(codiceFiscale);
        Properties properties = loadProperties();
        String bio = properties.getProperty(key(cf, "bio"), "");
        return new UserProfile(
                cf,
                bio,
                avatarPath(cf, properties).map(Files::exists).orElse(false),
                profileStats(Set.of(cf)).getOrDefault(cf, new UserProfileStats(0, 0, 0))
        );
    }

    public UserProfileSummary getPublicProfile(String codiceFiscale) throws BrondiException {
        String cf = normalizeCf(codiceFiscale);
        UserProfileSummary summary = getProfilesByCodiceFiscale(Set.of(cf)).get(cf);
        if (summary == null) {
            throw new BrondiException("User profile not found.", "PROFILE_NOT_FOUND", cf);
        }
        return summary;
    }

    public Map<String, UserProfileSummary> getProfilesByCodiceFiscale(Collection<String> codiciFiscali)
            throws BrondiException {
        Set<String> requested = normalizeCodiciFiscali(codiciFiscali);
        Map<String, UserProfileSummary> profiles = new HashMap<>();
        if (requested.isEmpty()) {
            return profiles;
        }

        LayerPersistenza layer = FactoryLayerPersistenza.createLayerPersistenza();
        Properties properties = loadProperties();
        try {
            Map<String, UserProfileStats> stats = profileStats(requested);
            addMatchingProfiles(profiles, requested, layer.listAdmins(), properties, stats);
            addMatchingProfiles(profiles, requested, layer.listTravelers(), properties, stats);
            return profiles;
        } catch (DAOExceptionRemoli e) {
            throw new BrondiException("Unable to load user profiles.", "PROFILE_USERS_LOAD", "Profile user lookup error", e);
        }
    }

    public void saveProfile(String codiceFiscale, String bio, Part avatarPart) throws BrondiException {
        String cf = normalizeCf(codiceFiscale);
        String normalizedBio = bio == null ? "" : bio.trim();

        if (normalizedBio.length() > 500) {
            throw new BrondiException("Bio can contain up to 500 characters.", "PROFILE_VALIDATION", "Bio too long");
        }

        Properties properties = loadProperties();
        properties.setProperty(key(cf, "bio"), normalizedBio);

        if (avatarPart != null && avatarPart.getSize() > 0) {
            String extension = resolveExtension(avatarPart);
            Path avatarPath = PROFILE_DIR.resolve(cf + extension);
            try {
                Files.createDirectories(PROFILE_DIR);
                try (InputStream input = avatarPart.getInputStream()) {
                    Files.copy(input, avatarPath, java.nio.file.StandardCopyOption.REPLACE_EXISTING);
                }
                properties.setProperty(key(cf, "avatar"), avatarPath.getFileName().toString());
            } catch (IOException e) {
                throw new BrondiException("Unable to save the profile image.", "PROFILE_IO", "Avatar write error", e);
            }
        }

        storeProperties(properties);
    }

    public void removeAvatar(String codiceFiscale) throws BrondiException {
        String cf = normalizeCf(codiceFiscale);
        Properties properties = loadProperties();
        String avatar = properties.getProperty(key(cf, "avatar"));

        if (avatar != null && !avatar.isBlank()) {
            try {
                Files.deleteIfExists(PROFILE_DIR.resolve(avatar));
            } catch (IOException e) {
                throw new BrondiException("Unable to remove the profile image.", "PROFILE_IO", "Avatar delete error", e);
            }
        }

        properties.remove(key(cf, "avatar"));
        storeProperties(properties);
    }

    public Path getAvatarPath(String codiceFiscale) throws BrondiException {
        String cf = normalizeCf(codiceFiscale);
        return avatarPath(cf)
                .filter(Files::exists)
                .orElseThrow(() -> new BrondiException("Profile image is not available.", "PROFILE_AVATAR_NOT_FOUND", cf));
    }

    private java.util.Optional<Path> avatarPath(String cf) throws BrondiException {
        return avatarPath(cf, loadProperties());
    }

    private java.util.Optional<Path> avatarPath(String cf, Properties properties) {
        String avatar = properties.getProperty(key(cf, "avatar"));
        if (avatar == null || avatar.isBlank()) {
            return java.util.Optional.empty();
        }
        return java.util.Optional.of(PROFILE_DIR.resolve(avatar));
    }

    private String normalizeCf(String codiceFiscale) throws BrondiException {
        if (codiceFiscale == null || codiceFiscale.trim().isEmpty()) {
            throw new BrondiException("Invalid user session.", "PROFILE_SESSION", "Missing codice fiscale");
        }
        return codiceFiscale.trim().toUpperCase(Locale.ROOT);
    }

    private String resolveExtension(Part avatarPart) throws BrondiException {
        if (avatarPart.getSize() > MAX_AVATAR_SIZE) {
            throw new BrondiException("Profile image cannot exceed 2 MB.", "PROFILE_VALIDATION", "Avatar too large");
        }

        String contentType = avatarPart.getContentType() == null ? "" : avatarPart.getContentType().toLowerCase(Locale.ROOT);
        return switch (contentType) {
            case "image/jpeg", "image/jpg" -> ".jpg";
            case "image/png" -> ".png";
            case "image/webp" -> ".webp";
            case "image/gif" -> ".gif";
            default -> throw new BrondiException("Upload a JPG, PNG, WEBP, or GIF image.", "PROFILE_VALIDATION", "Invalid avatar type: " + contentType);
        };
    }

    private Properties loadProperties() throws BrondiException {
        Properties properties = new Properties();
        if (!Files.exists(PROFILE_STORE)) {
            return properties;
        }

        try (InputStream input = Files.newInputStream(PROFILE_STORE)) {
            properties.load(input);
            return properties;
        } catch (IOException e) {
            throw new BrondiException("Unable to read the user profile.", "PROFILE_IO", "Profile store read error", e);
        }
    }

    private void storeProperties(Properties properties) throws BrondiException {
        try {
            Files.createDirectories(PROFILE_DIR);
            try (java.io.OutputStream output = Files.newOutputStream(PROFILE_STORE)) {
                properties.store(output, "Safe Flow user profiles");
            }
        } catch (IOException e) {
            throw new BrondiException("Unable to save the user profile.", "PROFILE_IO", "Profile store write error", e);
        }
    }

    private String key(String cf, String field) {
        return cf + "." + field;
    }

    private Set<String> normalizeCodiciFiscali(Collection<String> codiciFiscali) {
        Set<String> normalized = new HashSet<>();
        if (codiciFiscali == null) {
            return normalized;
        }

        for (String codiceFiscale : codiciFiscali) {
            if (codiceFiscale != null && !codiceFiscale.isBlank()) {
                normalized.add(codiceFiscale.trim().toUpperCase(Locale.ROOT));
            }
        }
        return normalized;
    }

    private void addMatchingProfiles(Map<String, UserProfileSummary> profiles,
                                     Set<String> requested,
                                     Collection<Credentials> users,
                                     Properties properties,
                                     Map<String, UserProfileStats> stats) {
        for (Credentials user : users) {
            if (user.getCodiceFiscale() == null) {
                continue;
            }

            String cf = user.getCodiceFiscale().trim().toUpperCase(Locale.ROOT);
            if (!requested.contains(cf) || profiles.containsKey(cf)) {
                continue;
            }

            String role = user.getRuolo() == null ? "" : user.getRuolo().name();
            boolean avatarPresent = avatarPath(cf, properties).map(Files::exists).orElse(false);
            profiles.put(cf, new UserProfileSummary(
                    cf,
                    user.getNome(),
                    user.getCognome(),
                    role,
                    properties.getProperty(key(cf, "bio"), ""),
                    avatarPresent,
                    stats.getOrDefault(cf, new UserProfileStats(0, 0, 0))
            ));
        }
    }

    private Map<String, UserProfileStats> profileStats(Set<String> requested) throws BrondiException {
        Map<String, MutableStats> mutableStats = new HashMap<>();
        Map<String, String> likeKeyOwners = new HashMap<>();
        Set<String> likeKeys = new HashSet<>();
        for (String cf : requested) {
            mutableStats.put(cf, new MutableStats());
        }

        if (mutableStats.isEmpty()) {
            return Map.of();
        }

        LayerPersistenza layer = FactoryLayerPersistenza.createLayerPersistenza();
        try {
            for (Notification notification : layer.getMessagesRAM()) {
                if (!"TRAVELER".equalsIgnoreCase(notification.getSenderRole()) || notification.getSenderCf() == null) {
                    continue;
                }

                String senderCf = notification.getSenderCf().trim().toUpperCase(Locale.ROOT);
                MutableStats stats = mutableStats.get(senderCf);
                if (stats == null) {
                    continue;
                }

                stats.reportCount++;
                String likeKey = NotificationLikeControllerApplicativo.keyFor(notification);
                likeKeys.add(likeKey);
                likeKeyOwners.put(likeKey, senderCf);
                if (notification.getCity() != null && !notification.getCity().isBlank()) {
                    stats.cities.add(notification.getCity().trim());
                }
            }
        } catch (DAOExceptionRemoli e) {
            throw new BrondiException("Unable to load profile statistics.", "PROFILE_STATS_LOAD", "Profile stats lookup error", e);
        }

        Map<String, it.web.routex.model.NotificationLikeState> likeStates =
                new NotificationLikeControllerApplicativo().statesFor(likeKeys, null);
        for (Map.Entry<String, it.web.routex.model.NotificationLikeState> entry : likeStates.entrySet()) {
            String ownerCf = likeKeyOwners.get(entry.getKey());
            MutableStats stats = ownerCf == null ? null : mutableStats.get(ownerCf);
            if (stats != null) {
                stats.approvalCount += entry.getValue().getLikeCount();
            }
        }

        Map<String, UserProfileStats> result = new HashMap<>();
        for (Map.Entry<String, MutableStats> entry : mutableStats.entrySet()) {
            MutableStats stats = entry.getValue();
            result.put(entry.getKey(), new UserProfileStats(
                    stats.reportCount,
                    stats.cities.size(),
                    stats.approvalCount
            ));
        }
        return result;
    }

    private static class MutableStats {
        private int reportCount;
        private int approvalCount;
        private final Set<String> cities = new LinkedHashSet<>();
    }
}
