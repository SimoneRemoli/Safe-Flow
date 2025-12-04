package it.web.routex.model.domain;

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

}
