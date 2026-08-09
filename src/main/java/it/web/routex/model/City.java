package it.web.routex.model;
/**
 * Classe di dominio che rappresenta una città all'interno del sistema RouteX.
 *
 * Questa classe fa parte del Model e incapsula le principali regole di dominio
 * legate al concetto di città supportata dal sistema di segnalazioni.
 *
 * La classe non è una Bean né un semplice DTO: oltre a contenere lo stato,
 * fornisce comportamento significativo attraverso metodi di validazione.
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

    public City() {}

    public City(String name) {
        this.name = name;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public boolean isValid() {
        return name != null &&
                !name.isBlank();
    }

}
