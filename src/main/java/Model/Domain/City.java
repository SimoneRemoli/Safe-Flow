package Model.Domain;

public class City {
    private String name;
    private double costo_biglietto;




    public City() {}

    public City(String name, double costo) {
        this.name = name;
        this.costo_biglietto = costo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getCosto_biglietto() {
        return costo_biglietto;
    }
}