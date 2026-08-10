package it.web.safeflow.utility;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public final class RomeMetroLineResolver {

    private static final MetroLine MA = new MetroLine("MA", "Metro A", "images/metro/ma.svg", "metro-a");
    private static final MetroLine MB = new MetroLine("MB", "Metro B", "images/metro/mb.svg", "metro-b");
    private static final MetroLine MC = new MetroLine("MC", "Metro C", "images/metro/mc.svg", "metro-c");

    private static final Set<String> LINE_A = normalizedSet(
            "Battistini", "Cornelia", "Baldo degli Ubaldi", "Valle Aurelia", "Cipro",
            "Ottaviano", "Lepanto", "Flaminio", "Spagna", "Barberini", "Repubblica",
            "Termini", "Vittorio Emanuele", "Manzoni", "San Giovanni", "Re di Roma",
            "Ponte Lungo", "Furio Camillo", "Colli Albani", "Arco di Travertino",
            "Porta Furba", "Numidio Quadrato", "Lucio Sestio", "Giulio Agricola",
            "Subaugusta", "Cinecitta", "Anagnina"
    );

    private static final Set<String> LINE_B = normalizedSet(
            "Laurentina", "EUR Fermi", "EUR Palasport", "EUR Magliana", "Marconi",
            "Basilica S. Paolo", "Basilica San Paolo", "Garbatella", "Piramide",
            "Circo Massimo", "Colosseo", "Cavour", "Termini", "Castro Pretorio",
            "Policlinico", "Bologna", "Tiburtina FS", "Quintiliani", "Monti Tiburtini",
            "Pietralata", "Santa Maria del Soccorso", "Ponte Mammolo", "Rebibbia",
            "Sant Agnese Annibaliano", "S. Agnese Annibaliano", "Annibaliano",
            "Libia", "Conca d'Oro", "Conca D oro", "Jonio"
    );

    private static final Set<String> LINE_C = normalizedSet(
            "Monte Compatri Pantano", "Pantano", "Graniti", "Finocchio", "Bolognetta",
            "Borghesiana", "Due Leoni - Fontana Candida", "Due Leoni Fontana Candida",
            "Grotte Celoni", "Torre Gaia", "Torre Angela", "Torrenova", "Giardinetti",
            "Torre Maura", "Torre Spaccata", "Alessandrino", "Parco di Centocelle",
            "Mirti", "Gardenie", "Teano", "Malatesta", "Pigneto", "Lodi",
            "San Giovanni", "Porta Metronia", "Colosseo"
    );

    private static final Map<MetroLine, Set<String>> LINES = Map.of(
            MA, LINE_A,
            MB, LINE_B,
            MC, LINE_C
    );

    private RomeMetroLineResolver() {
    }

    public static List<MetroLine> linesFor(String city, String stationName) {
        if (!"Rome".equalsIgnoreCase(city == null ? "" : city.trim())) {
            return List.of();
        }

        String station = normalize(stationName);
        if (station.isBlank()) {
            return List.of();
        }

        List<MetroLine> result = new ArrayList<>();
        for (Map.Entry<MetroLine, Set<String>> entry : LINES.entrySet()) {
            if (entry.getValue().contains(station)) {
                result.add(entry.getKey());
            }
        }
        result.sort((left, right) -> left.code().compareTo(right.code()));
        return result;
    }

    private static Set<String> normalizedSet(String... values) {
        java.util.LinkedHashSet<String> set = new java.util.LinkedHashSet<>();
        for (String value : values) {
            set.add(normalize(value));
        }
        return Set.copyOf(set);
    }

    private static String normalize(String value) {
        if (value == null) {
            return "";
        }
        String noAccents = Normalizer.normalize(value, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "");
        return noAccents.toLowerCase(Locale.ROOT)
                .replaceAll("[^a-z0-9]+", " ")
                .replaceAll("\\s+", " ")
                .trim();
    }

    public record MetroLine(String code, String label, String assetPath, String cssClass) {
    }
}
