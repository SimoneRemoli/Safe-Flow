package it.web.routex.model;
/**
 * Classe di dominio che rappresenta una città all'interno del sistema RouteX.
 *
 * Questa classe fa parte del Model e incapsula le principali regole di dominio
 * legate al concetto di città, come il costo del biglietto, il numero di stazioni
 * e il calcolo dei prezzi associati all'acquisto dei ticket.
 *
 * La classe non è una Bean né un semplice DTO: oltre a contenere lo stato,
 * fornisce comportamento significativo attraverso metodi di logica applicativa
 * (es. validazione dei dati e calcolo del prezzo totale).
 *
 * Le responsabilità di accesso ai dati sono delegate al CityDAO, mentre la
 * conversione verso oggetti di presentazione è affidata ai CityBean.
 * In questo modo si mantiene una chiara separazione tra dominio, persistenza
 * e livello di presentazione, in accordo con i principi GRASP e l’architettura MVC.
 *
 * @author Simone Remoli
 */


public class City {
    private String name;
    private double costoBiglietto;
    private long numeroStazioni;

    public City() {}

    public City(String name, double costo, long numeroStazioni) {
        this.name = name;
        this.costoBiglietto = costo;
        this.numeroStazioni = numeroStazioni;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public double getCostoBiglietto() { return costoBiglietto; }

    public boolean isValid() {
        return name != null &&
                !name.isBlank() &&
                costoBiglietto > 0 &&
                numeroStazioni > 0;
    }

    public double calcolaPrezzoTotale(int quantita) {
        if (quantita <= 0) {
            throw new IllegalArgumentException("Quantità non valida");
        }
        return costoBiglietto * quantita;
    }


}
