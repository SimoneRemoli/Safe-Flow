package it.web.routex.extractor;

import it.web.routex.exception.BrondiInvalidCommunicationInputException;
import it.web.routex.record.CommunicationInput;

import javax.servlet.http.HttpServletRequest;
import java.io.InputStream;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

public final class CommunicationInputExtractor {

    private static final Set<String> FORBIDDEN_WORDS = loadForbiddenWords();
    private static final Set<String> SUPPORTED_NOTIFICATION_CITIES = Set.of("Rome", "Naples", "Athens", "Budapest");

    private CommunicationInputExtractor() {
        // utility class
    }

    public static CommunicationInput extract(HttpServletRequest request)
            throws BrondiInvalidCommunicationInputException {

        String testo = request.getParameter("message");
        String city = request.getParameter("city");
        boolean pickpocketAlert = "true".equalsIgnoreCase(request.getParameter("pickpocketAlert"))
                || "on".equalsIgnoreCase(request.getParameter("pickpocketAlert"));
        boolean fightAlert = "true".equalsIgnoreCase(request.getParameter("fightAlert"))
                || "on".equalsIgnoreCase(request.getParameter("fightAlert"));
        boolean crowdAlert = "true".equalsIgnoreCase(request.getParameter("crowdAlert"))
                || "on".equalsIgnoreCase(request.getParameter("crowdAlert"));
        boolean generalAlert = "true".equalsIgnoreCase(request.getParameter("generalAlert"))
                || "on".equalsIgnoreCase(request.getParameter("generalAlert"));
        String stationName = request.getParameter("stationName");

        // Controllo input vuoto
        if (testo == null || testo.trim().isEmpty()) {
            throw new BrondiInvalidCommunicationInputException(
                    "The report message cannot be empty."
            );
        }

        String cleanText = testo.trim();
        String cleanCity = city == null ? "" : city.trim();
        String cleanStationName = stationName == null ? "" : stationName.trim();

        if (cleanCity.isEmpty()) {
            throw new BrondiInvalidCommunicationInputException(
                    "A city must be selected for the report."
            );
        }

        if (!SUPPORTED_NOTIFICATION_CITIES.contains(cleanCity)) {
            throw new BrondiInvalidCommunicationInputException(
                    "The selected city is not supported by RouteX notifications."
            );
        }

        String normalized = cleanText.toLowerCase().replaceAll("[^a-zàèéìòù]", " ");

        // Controllo parole proibite
        for (String forbidden : FORBIDDEN_WORDS) {
            if (normalized.contains(forbidden)) {
                throw new BrondiInvalidCommunicationInputException(
                        "The message contains disallowed language."
                );
            }
        }

        if (!pickpocketAlert && !fightAlert && !crowdAlert && !generalAlert) {
            throw new BrondiInvalidCommunicationInputException(
                    "Select at least one alert type before submitting the report."
            );
        }

        // Input valido
        return new CommunicationInput(
                cleanText,
                new Timestamp(System.currentTimeMillis()),
                cleanCity,
                pickpocketAlert,
                fightAlert,
                crowdAlert,
                generalAlert,
                cleanStationName.isEmpty() ? null : cleanStationName,
                null
        );
    }


    private static Set<String> loadForbiddenWords() {

        Properties props = new Properties();

        try (InputStream is = CommunicationInputExtractor.class.getClassLoader().getResourceAsStream("forbiddenwords.properties")) {

            if (is == null) {
                return Set.of();
            }

            props.load(is);

            String raw = props.getProperty("forbidden.words");

            if (raw == null || raw.isBlank()) {
                return Set.of();
            }

            return new HashSet<>(
                    Arrays.asList(raw.toLowerCase().split(","))
            );

        } catch (Exception e) {
            return Set.of();
        }
    }
}
