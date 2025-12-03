package it.web.routex.bean;

public class InformazioniPercorsoBean {

    private CityLifeBean cityLife;
    private int numeroStazioniUsate;
    private double minutaggio;
    private double percentualeStazioniUsate;

    public double getMinutaggio() {
        return minutaggio;
    }

    public void setMinutaggio(double minutaggio) {
        this.minutaggio = minutaggio;
    }

    public double getPercentualeStazioniUsate() {
        return percentualeStazioniUsate;
    }

    public void setPercentualeStazioniUsate(double percentualeStazioniUsate) {
        this.percentualeStazioniUsate = percentualeStazioniUsate;
    }

    public int getNumeroStazioniUsate() {
        return numeroStazioniUsate;
    }

    public void setNumeroStazioniUsate(int numeroStazioniUsate) {
        this.numeroStazioniUsate = numeroStazioniUsate;
    }

    public CityLifeBean getCityLife() {
        return cityLife;
    }

    public void setCityLife(CityLifeBean cityLife) {
        this.cityLife = cityLife;
    }
}
