package Model.Domain;

import Controller.Applicativo.CityLifeController;

public class Rome extends CityLifeController {

    public Rome() throws Exception {

        this.matriceAdiacenza = new int[76][76]; // Roma ha 76 stazioni
        caricaMatriceDaClasspath("/Adjacency_Matrix/Rome.csv");

    }

}
