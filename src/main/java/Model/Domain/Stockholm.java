package Model.Domain;

import Controller.Applicativo.CityLifeController;

public class Stockholm extends CityLifeController {
    public Stockholm() throws Exception {

        this.matriceAdiacenza = new int[101][101];
        caricaMatriceDaClasspath("/Adjacency_Matrix/Stockholm.csv");
    }
}
