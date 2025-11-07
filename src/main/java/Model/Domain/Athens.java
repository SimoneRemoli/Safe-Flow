package Model.Domain;


import Controller.Applicativo.CityLifeController;

public class Athens extends CityLifeController
{
    public Athens() throws Exception {

        this.matriceAdiacenza = new int[66][66];
        caricaMatriceDaClasspath("/Adjacency_Matrix/Athens.csv");
    }
}