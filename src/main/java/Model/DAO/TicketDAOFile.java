package Model.DAO;
import Bean.TicketBean;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import Exception.PathNotFoundExceptionRemoli;;
import Exception.DAOExceptionRemoli;
import utility.Singleton.Credentials;
import Exception.CredentialsExceptionRemoli;

public class TicketDAOFile extends TicketDAOLayer
{
    public List<TicketBean> getTicketByCF(String cf)
            throws DAOExceptionRemoli, PathNotFoundExceptionRemoli {

        List<TicketBean> tickets = new ArrayList<>();

        final String filePath = "/Users/simoneremoli/IdeaProjects/RouteX_MVC_Project/FilePersistenza/Tickets_sync.csv";

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {

            String line;

            // Salta header
            br.readLine();

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

        final String filePath = "/Users/simoneremoli/IdeaProjects/RouteX_MVC_Project/FilePersistenza/Tickets_sync.csv";

        boolean fileVuoto = !new File(filePath).exists() || new File(filePath).length() == 0;

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath, true))) {

            // Se il file è nuovo o vuoto → scrivi l'header UNA sola volta
            if (fileVuoto) {
                bw.write("codice_fiscale,nome,cognome,disabile,metodo_pagamento,citta,codici_biglietti,timestamp");
                bw.newLine();
            }

            // Conversione lista biglietti in stringa separata da ;
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
