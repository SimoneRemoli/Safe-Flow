package it.web.routex.model.dao;
import it.web.routex.bean.TicketBean;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import it.web.routex.exception.PathNotFoundExceptionRemoli;
import it.web.routex.exception.DAOExceptionRemoli;
import it.web.routex.utility.singleton.Credentials;
import it.web.routex.exception.CredentialsExceptionRemoli;
import it.web.routex.utility.configLoader.ConfigLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TicketDAOFile extends TicketDAOLayer
{
    protected final Logger logger = LoggerFactory.getLogger(getClass());
    private final String filePath = ConfigLoader.get("ticket.csv.path");

    public List<TicketBean> getTicketByCF(String cf) throws DAOExceptionRemoli, PathNotFoundExceptionRemoli {

        List<TicketBean> tickets = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {

            String line;

            // Salta header
            String header = br.readLine();
            if (header == null) {
                throw new DAOExceptionRemoli("File tickets CSV vuoto o corrotto");
            }


            while ((line = br.readLine()) != null) {

                String[] parts = line.split(",");

                if (parts.length < 8) continue; // sicurezza

                String codiceFiscale = parts[0];
                String citta = parts[5];
                String[] codiciBiglietti = parts[6].split(";");
                String timestamp = parts[7];

                // Filtra per CF
                if (codiceFiscale.equals(cf)) {

                    // Per ogni codice biglietto → 1 TicketBean
                    for (String codice : codiciBiglietti) {

                        TicketBean t = new TicketBean();
                        t.setCodice(codice);     // codice biglietto singolo
                        t.setCitta(citta);       // città dell'acquisto
                        t.setDataAcquisto(timestamp); // timestamp pagamento

                        tickets.add(t);
                    }
                }
            }

        } catch (IOException e) {
            throw new DAOExceptionRemoli("Errore lettura CSV: " + e.getMessage(), e);
        }

        if (tickets.isEmpty()) {
            throw new PathNotFoundExceptionRemoli("Nessun biglietto trovato per CF: ", cf, 404, "Errore in TicketDAOFile.java");
        }

        return tickets;
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
}
