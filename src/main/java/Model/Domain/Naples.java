package Model.Domain;

import Controller.Applicativo.CityLifeController;

public class Naples extends CityLifeController
{
    public Naples() throws Exception {

        this.matriceAdiacenza = new int[39][39];
        caricaMatriceDaClasspath("/Adjacency_Matrix/Naples.csv");
    }
}
