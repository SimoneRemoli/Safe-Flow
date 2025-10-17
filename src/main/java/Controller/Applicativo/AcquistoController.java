package Controller.Applicativo;


import Bean.PrezzoTotaleBean;
import Model.Domain.City;
import java.util.List;

public class AcquistoController
{

    public PrezzoTotaleBean ottieni_prezzo_totale(String city, List<City> cities, String qty) {
        int quantita = Integer.parseInt(qty);
        PrezzoTotaleBean price = null;
        for (City a : cities)
        {
            if(a.getName().equals(city))
            {
                price = new PrezzoTotaleBean(calculate(a.getCosto_biglietto(), quantita));
            }
        }
        return price;
    }
    private double calculate(double costo, int qty)
    {
        return qty*costo;
    }
}
