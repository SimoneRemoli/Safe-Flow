package it.web.routex.controller.applicativo;

import it.web.routex.exception.BrondiException;
import it.web.routex.model.UserProfile;

import javax.servlet.http.Part;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Locale;
import java.util.Properties;

public class UserProfileControllerApplicativo {

    private static final long MAX_AVATAR_SIZE = 2L * 1024L * 1024L;
    private static final Path PROFILE_DIR = Path.of(System.getProperty("user.home"), ".safe-flow", "profiles");
    private static final Path PROFILE_STORE = PROFILE_DIR.resolve("profiles.properties");

    public UserProfile getProfile(String codiceFiscale) throws BrondiException {
        String cf = normalizeCf(codiceFiscale);
        Properties properties = loadProperties();
        String bio = properties.getProperty(key(cf, "bio"), "");
        return new UserProfile(cf, bio, avatarPath(cf).map(Files::exists).orElse(false));
    }

    public void saveProfile(String codiceFiscale, String bio, Part avatarPart) throws BrondiException {
        String cf = normalizeCf(codiceFiscale);
        String normalizedBio = bio == null ? "" : bio.trim();

        if (normalizedBio.length() > 500) {
            throw new BrondiException("La bio può contenere al massimo 500 caratteri.", "PROFILE_VALIDATION", "Bio too long");
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
                throw new BrondiException("Impossibile salvare la foto profilo.", "PROFILE_IO", "Avatar write error", e);
            }
        }

        storeProperties(properties);
    }

    public Path getAvatarPath(String codiceFiscale) throws BrondiException {
        String cf = normalizeCf(codiceFiscale);
        return avatarPath(cf)
                .filter(Files::exists)
                .orElseThrow(() -> new BrondiException("Avatar non disponibile.", "PROFILE_AVATAR_NOT_FOUND", cf));
    }

    private java.util.Optional<Path> avatarPath(String cf) throws BrondiException {
        Properties properties = loadProperties();
        String avatar = properties.getProperty(key(cf, "avatar"));
        if (avatar == null || avatar.isBlank()) {
            return java.util.Optional.empty();
        }
        return java.util.Optional.of(PROFILE_DIR.resolve(avatar));
    }

    private String normalizeCf(String codiceFiscale) throws BrondiException {
        if (codiceFiscale == null || codiceFiscale.trim().isEmpty()) {
            throw new BrondiException("Sessione utente non valida.", "PROFILE_SESSION", "Missing codice fiscale");
        }
        return codiceFiscale.trim().toUpperCase(Locale.ROOT);
    }

    private String resolveExtension(Part avatarPart) throws BrondiException {
        if (avatarPart.getSize() > MAX_AVATAR_SIZE) {
            throw new BrondiException("La foto profilo non può superare 2 MB.", "PROFILE_VALIDATION", "Avatar too large");
        }

        String contentType = avatarPart.getContentType() == null ? "" : avatarPart.getContentType().toLowerCase(Locale.ROOT);
        return switch (contentType) {
            case "image/jpeg", "image/jpg" -> ".jpg";
            case "image/png" -> ".png";
            case "image/webp" -> ".webp";
            case "image/gif" -> ".gif";
            default -> throw new BrondiException("Carica una foto in formato JPG, PNG, WEBP o GIF.", "PROFILE_VALIDATION", "Invalid avatar type: " + contentType);
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
            throw new BrondiException("Impossibile leggere il profilo utente.", "PROFILE_IO", "Profile store read error", e);
        }
    }

    private void storeProperties(Properties properties) throws BrondiException {
        try {
            Files.createDirectories(PROFILE_DIR);
            try (java.io.OutputStream output = Files.newOutputStream(PROFILE_STORE)) {
                properties.store(output, "Safe Flow user profiles");
            }
        } catch (IOException e) {
            throw new BrondiException("Impossibile salvare il profilo utente.", "PROFILE_IO", "Profile store write error", e);
        }
    }

    private String key(String cf, String field) {
        return cf + "." + field;
    }
}
