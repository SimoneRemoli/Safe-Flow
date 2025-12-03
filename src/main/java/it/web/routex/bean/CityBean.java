package it.web.routex.bean;


import it.web.routex.model.domain.City;

/**
 * Bean per il trasferimento del solo nome della città verso la View.
 */
public class CityBean {

    private String nome;

    /** Costruttore vuoto richiesto per JSP/Servlet */
    public CityBean() {}

    /** Costruttore che riceve un oggetto City del Model */
    public CityBean(City city) {
        this.nome = city.getName();
    }

    // === GETTER E SETTER ===
    public String getName() {
        return nome;
    }

    public void setName(String nome) {
        this.nome = nome;
    }

    @Override
    public String toString() {
        return nome;
    }
}

