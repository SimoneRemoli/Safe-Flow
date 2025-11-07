package Model.Domain;

import Controller.Applicativo.CityLifeController;

//p
public class Milan extends CityLifeController
{
    public Milan() throws Exception {

        this.matriceAdiacenza = new int[124][124]; //milano ha 124 stazioni
        caricaMatriceDaClasspath("/Adjacency_Matrix/Milan.csv");
    }
}
