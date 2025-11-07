package Bean;

public class InformazioniPercorsoBean
{
    CityLifeBean cityLife;
    int numero_stazioni_usate;
    double minutaggio;
    double percentuale_stazioni_usate;

    public double getMinutaggio() {
        return minutaggio;
    }

    public void setMinutaggio(double minutaggio) {
        this.minutaggio = minutaggio;
    }

    public double getPercentuale_stazioni_usate() {
        return percentuale_stazioni_usate;
    }

    public void setPercentuale_stazioni_usate(double percentuale_stazioni_usate) {
        this.percentuale_stazioni_usate = percentuale_stazioni_usate;
    }

    public int getNumero_stazioni_usate() {
        return numero_stazioni_usate;
    }

    public void setNumero_stazioni_usate(int numero_stazioni_usate) {
        this.numero_stazioni_usate = numero_stazioni_usate;
    }

    public CityLifeBean getCityLife() {
        return cityLife;
    }

    public void setCityLife(CityLifeBean cityLife) {
        this.cityLife = cityLife;
    }
}
