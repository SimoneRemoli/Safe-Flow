package it.web.routex.dao;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import it.web.routex.exception.PathNotFoundExceptionRemoli;
import it.web.routex.exception.DAOExceptionRemoli;
import it.web.routex.model.Ticket;
import it.web.routex.utility.singleton.Credentials;
import it.web.routex.exception.CredentialsExceptionRemoli;
import it.web.routex.utility.configloader.ConfigLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TicketDAOFile extends TicketDAOLayer
{
    protected final Logger logger = LoggerFactory.getLogger(getClass());
    private final String filePath = ConfigLoader.get("ticket.csv.path");
    private static final DateTimeFormatter CSV_TIMESTAMP_FORMAT =
            DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

    public List<Ticket> getTicketByCF(String cf)
            throws DAOExceptionRemoli, PathNotFoundExceptionRemoli {

        List<Ticket> tickets = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {

            String line;

            // Salta header
            String header = br.readLine();
            if (header == null) {
                throw new DAOExceptionRemoli("File tickets CSV vuoto o corrotto");
            }

            while ((line = br.readLine()) != null) {

                String[] parts = line.split(",");

                // sicurezza minima
                if (parts.length < 8) continue;

                String codiceFiscale = parts[0].trim();
                String citta = parts[5].trim();
                String[] codiciBiglietti = parts[6].split(";");
                String timestamp = parts[7].trim();

                // Filtra per codice fiscale
                if (codiceFiscale.equals(cf)) {

                    LocalDateTime dataAcquisto = parseTimestamp(timestamp);

                    // Un Ticket per ogni codice biglietto
                    for (String codice : codiciBiglietti) {
                        Ticket t = new Ticket(
                                codice.trim(),
                                citta,
                                dataAcquisto
                        );
                        tickets.add(t);
                    }
                }
            }

        } catch (IOException e) {
            throw new DAOExceptionRemoli(
                    "Errore lettura CSV: " + e.getMessage(),
                    e
            );
        }

        if (tickets.isEmpty()) {
            throw new PathNotFoundExceptionRemoli(
                    "Nessun biglietto trovato per CF",
                    cf,
                    404,
                    "TicketDAOFile.getTicketByCF"
            );
        }

        return tickets;
    }

    @Override
    public int deleteTicketsByCodes(String cf, List<String> ticketCodes) throws DAOExceptionRemoli {
        File file = new File(filePath);
        if (!file.exists()) {
            return 0;
        }

        List<String> rewrittenLines = new ArrayList<>();
        int deleted = 0;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String header = br.readLine();
            if (header == null) {
                throw new DAOExceptionRemoli("File tickets CSV vuoto o corrotto");
            }
            rewrittenLines.add(header);

            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length < 8) {
                    continue;
                }

                if (!parts[0].trim().equals(cf)) {
                    rewrittenLines.add(line);
                    continue;
                }

                List<String> remainingCodes = new ArrayList<>();
                int removedForLine = 0;
                for (String code : parts[6].split(";")) {
                    String normalizedCode = code.trim();
                    if (ticketCodes.contains(normalizedCode)) {
                        removedForLine++;
                    } else if (!normalizedCode.isEmpty()) {
                        remainingCodes.add(normalizedCode);
                    }
                }

                deleted += removedForLine;
                if (!remainingCodes.isEmpty()) {
                    parts[6] = String.join(";", remainingCodes);
                    rewrittenLines.add(String.join(",", Arrays.copyOf(parts, 8)));
                }
            }
        } catch (IOException e) {
            throw new DAOExceptionRemoli("Errore lettura CSV: " + e.getMessage(), e);
        }

        rewriteTicketFile(rewrittenLines);
        return deleted;
    }

    @Override
    public int deleteAllTickets(String cf) throws DAOExceptionRemoli {
        File file = new File(filePath);
        if (!file.exists()) {
            return 0;
        }

        List<String> rewrittenLines = new ArrayList<>();
        int deleted = 0;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String header = br.readLine();
            if (header == null) {
                throw new DAOExceptionRemoli("File tickets CSV vuoto o corrotto");
            }
            rewrittenLines.add(header);

            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length < 8) {
                    continue;
                }

                if (parts[0].trim().equals(cf)) {
                    for (String code : parts[6].split(";")) {
                        if (!code.trim().isEmpty()) {
                            deleted++;
                        }
                    }
                    continue;
                }

                rewrittenLines.add(line);
            }
        } catch (IOException e) {
            throw new DAOExceptionRemoli("Errore lettura CSV: " + e.getMessage(), e);
        }

        rewriteTicketFile(rewrittenLines);
        return deleted;
    }


    @Override
    public void salvataggio(Credentials cred, List<String> codiciBiglietti, String metodopayment, String city) throws CredentialsExceptionRemoli
    {
        boolean fileVuoto = !new File(filePath).exists() || new File(filePath).length() == 0;
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath, true))) {

            // Se il file è nuovo o vuoto → scrivi l'header UNA sola volta
            if (fileVuoto) {
                bw.write("codice_fiscale,nome,cognome,disabile,metodo_pagamento,citta,codici_biglietti,timestamp");
                bw.newLine();
            }

            String listaCodici = String.join(";", codiciBiglietti);

            // Timestamp formattato
            String timestamp = java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));

            // Scrittura della riga
            bw.write(
                    cred.getCodiceFiscale() + "," +
                            cred.getNome() + "," +
                            cred.getCognome() + "," +
                            cred.getDisabile() + "," +
                            metodopayment + "," +
                            city + "," +
                            listaCodici + "," +
                            timestamp
            );
            bw.newLine();

        } catch (IOException e) {
            throw new CredentialsExceptionRemoli("Errore scrittura CSV: " + e.getMessage(),
                    "Errore in TicketDAOFile.java");
        }
    }
    private LocalDateTime parseTimestamp(String timestamp) throws DAOExceptionRemoli {
        try {
            return LocalDateTime.parse(timestamp, CSV_TIMESTAMP_FORMAT);
        } catch (DateTimeParseException e) {
            throw new DAOExceptionRemoli(
                    "Timestamp non valido nel CSV: " + timestamp,
                    e
            );
        }
    }

    private void rewriteTicketFile(List<String> lines) throws DAOExceptionRemoli {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath, false))) {
            for (String rewrittenLine : lines) {
                bw.write(rewrittenLine);
                bw.newLine();
            }
        } catch (IOException e) {
            throw new DAOExceptionRemoli("Errore scrittura CSV: " + e.getMessage(), e);
        }
    }

}
