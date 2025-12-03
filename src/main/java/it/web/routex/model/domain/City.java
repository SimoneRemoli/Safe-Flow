package it.web.routex.model.domain;

public class City {
    private String name;
    private double costo_biglietto;
    private long numero_stazioni;

    public City() {}

    public City(String name, double costo, long numeroStazioni) {
        this.name = name;
        this.costo_biglietto = costo;
        this.numero_stazioni = numeroStazioni;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public double getCosto_biglietto() { return costo_biglietto; }
    public void setCosto_biglietto(double costo_biglietto) { this.costo_biglietto = costo_biglietto; }

    public long getNumero_stazioni() { return numero_stazioni; }
    public void setNumero_stazioni(long numero_stazioni) { this.numero_stazioni = numero_stazioni; }
}
