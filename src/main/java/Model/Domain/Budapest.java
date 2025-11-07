package Model.Domain;

import Controller.Applicativo.CityLifeController;

public class Budapest extends CityLifeController {
    public Budapest() throws Exception {

        this.matriceAdiacenza = new int[48][48];
        caricaMatriceDaClasspath("/Adjacency_Matrix/Budapest.csv");
    }
}
