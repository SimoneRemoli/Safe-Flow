package it.web.routex.model.domain;

import it.web.routex.controller.applicativo.CityLifeController;

public class Rome extends CityLifeController {

    public Rome() {

        this.matriceAdiacenza = new int[76][76]; // Roma ha 76 stazioni
        caricaMatriceDaClasspath("/Adjacency_Matrix/Rome.csv");

    }

}
