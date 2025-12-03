package it.web.routex.model.domain;

import it.web.routex.controller.applicativo.CityLifeController;

public class Naples extends CityLifeController
{
    public Naples() {

        this.matriceAdiacenza = new int[39][39];
        caricaMatriceDaClasspath("/Adjacency_Matrix/Naples.csv");
    }
}
