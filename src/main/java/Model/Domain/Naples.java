package Model.Domain;

import Controller.Applicativo.CityLifeController;

public class Naples extends CityLifeController
{
    public Naples() {

        this.matriceAdiacenza = new int[39][39];
        caricaMatriceDaClasspath("/Adjacency_Matrix/Naples.csv");
    }
}
