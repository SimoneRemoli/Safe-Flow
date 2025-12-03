package it.web.routex.model.domain;


import it.web.routex.controller.applicativo.CityLifeController;

public class Athens extends CityLifeController
{
    public Athens() {

        this.matriceAdiacenza = new int[66][66];
        caricaMatriceDaClasspath("/Adjacency_Matrix/Athens.csv");
    }
}