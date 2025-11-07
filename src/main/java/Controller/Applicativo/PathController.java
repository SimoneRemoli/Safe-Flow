package Controller.Applicativo;

import Bean.CityLifeBean;
import Bean.InformazioniPercorsoBean;
import Model.DAO.RestituisciIdStazioniPartenzaArrivoDAO;
import utility.CityLifeFactory;
import Exception.DAOException;

import java.util.ArrayList;

public class PathController
{

    private void remove_duplicate(ArrayList<String> Sequenze_di_cambiamento_full,int indice)
    {
        for(int i=indice;i<Sequenze_di_cambiamento_full.size();i++)
        {
            if(i==Sequenze_di_cambiamento_full.size()-1)
                return;
            if(Sequenze_di_cambiamento_full.get(i).equals(Sequenze_di_cambiamento_full.get(i+1)))
            {
                Sequenze_di_cambiamento_full.remove(i);
                remove_duplicate(Sequenze_di_cambiamento_full,i);

            }
        }

    }
    public InformazioniPercorsoBean run(String startStation, String endStation, String city) throws Exception
    {
        int numero_stazioni_totali;
        ArrayList<String> Percorsi_Con_Nomi = new ArrayList<String>();
        ArrayList<Integer> Percorsi_Codifiche = new ArrayList<Integer>();
        int numero_cambi = 0, numero_stazioni_usate = 0;
        ArrayList<String> Linee_Metropolitane = new ArrayList<String>();
        ArrayList<String> Sequenze_di_cambiamento_full = new ArrayList<String>();
        ArrayList<String> Sequenze_nodi_cruciali_full = new ArrayList<String>();
        String status="";
        Double minutaggio = 0.0;
        Double percentuale_stazioni_usate = 0.0, app = 0.0;


        RestituisciIdStazioniPartenzaArrivoDAO dao = new RestituisciIdStazioniPartenzaArrivoDAO();
        dao.restituisciIdStazioni(startStation, endStation, city);

        int codeStartStation = dao.getStazioneDiPartenza();
        int codeFinishStation = dao.getStazioneDiArrivo();

        if (codeFinishStation == 0 || codeStartStation == 0) {
            throw new DAOException("Stazioni non trovate o nomi errati");
        }

        System.out.println("Id partenza = " + codeStartStation);
        System.out.println("Id arrivo = " + codeFinishStation);


        CityLifeController metropoli = CityLifeFactory.createCity(city); //FIN QUì OK

        Percorsi_Codifiche = metropoli.Dijkstra(codeStartStation, codeFinishStation, city); //potrei far tornare una bean

        CityLifeBean cityLife = metropoli.calcolaPercorso(Percorsi_Codifiche,city);

        InformazioniPercorsoBean trasferimento = new InformazioniPercorsoBean();

        System.out.println(" ");
        System.out.println("-----------Numero cambi =  " + numero_cambi);
        for (String s : cityLife.getLinee()) System.out.println("Sequenza di linee metropolitane =  " + s);
        if (cityLife.getNumero_cambi() == -1) cityLife.setNumero_cambi(0);

        System.out.println("Stampe robe");
        for (int i = 0; i < cityLife.getPercorsi_Con_Nomi().size(); i++)
            System.out.print(" " + cityLife.getPercorsi_Con_Nomi().get(i) + " ---> ");
        for (int i = 0; i < Percorsi_Codifiche.size(); i++)
            System.out.println(" " + Percorsi_Codifiche.get(i) + " ----> ");
        numero_stazioni_usate = cityLife.getPercorsi_Con_Nomi().size();
        minutaggio = numero_stazioni_usate * 2.5;
        app = (double) numero_stazioni_usate / cityLife.getNumero_stazioni_totali();
        percentuale_stazioni_usate = (double) app * 100.0;


        trasferimento.setCityLife(cityLife);
        trasferimento.setMinutaggio(minutaggio);
        trasferimento.setNumero_stazioni_usate(numero_stazioni_usate);
        trasferimento.setPercentuale_stazioni_usate(percentuale_stazioni_usate);
        trasferimento.setPercentuale_stazioni_usate(percentuale_stazioni_usate);

        return trasferimento;
    }
}
