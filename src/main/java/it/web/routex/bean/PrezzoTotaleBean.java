package it.web.routex.bean;

public class PrezzoTotaleBean
{
    private final double prezzoTotale;

    public PrezzoTotaleBean(double calculate)
    {
        this.prezzoTotale = calculate;
    }
    public double getPrezzoTotale() {
        return prezzoTotale;
    }
}
