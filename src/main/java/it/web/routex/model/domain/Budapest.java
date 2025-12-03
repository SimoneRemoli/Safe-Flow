package it.web.routex.model.domain;

import it.web.routex.controller.applicativo.CityLifeController;

public class Budapest extends CityLifeController {
    public Budapest(){

        this.matriceAdiacenza = new int[48][48];
        caricaMatriceDaClasspath("/Adjacency_Matrix/Budapest.csv");
    }
}
