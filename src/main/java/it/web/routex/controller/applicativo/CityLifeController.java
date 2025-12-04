package it.web.routex.controller.applicativo;
import it.web.routex.bean.CityLifeBean;
import it.web.routex.bean.FermataRecordBean;
import it.web.routex.model.dao.FermataDAO;
import java.io.*;
import java.sql.SQLException;
import java.util.*;

import it.web.routex.exception.FuoriRangeExceptionRemoli;
import it.web.routex.exception.UnreacheableNodeExceptionRemoli;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CityLifeController
{
    protected int[][] matriceAdiacenza; //le classi figlie possono specializzare la loro matrice di adiacenza
    private void removeChangeLines(List<String> sequenzeDiCambiamentoFull)
    {
        for (int i = sequenzeDiCambiamentoFull.size() - 1; i >= 0; i--) {
            if (sequenzeDiCambiamentoFull.get(i).contains("-")) {
                sequenzeDiCambiamentoFull.remove(i);
            }
        }

    }
    private void removeDuplicate(List<String> sequenzeDiCambiamentoFull,int indice) {
        for(int i=indice;i<sequenzeDiCambiamentoFull.size();i++)
        {
            if(i==sequenzeDiCambiamentoFull.size()-1)
                return;
            if(sequenzeDiCambiamentoFull.get(i).equals(sequenzeDiCambiamentoFull.get(i+1)))
            {
                sequenzeDiCambiamentoFull.remove(i);
                removeDuplicate(sequenzeDiCambiamentoFull,i);

            }
        }

    }

    public CityLifeBean calcolaPercorso(List<Integer> ids, String city) throws SQLException {
        final Logger logger = LoggerFactory.getLogger(getClass());
        List<String> percorsiConFermate = new ArrayList<String>();
        List<Integer> percorsiCodifica = new ArrayList<Integer>();
        List<String> sequenzediCambiamento = new ArrayList<String>();
        List<String> sequenzeNodiCruciali = new ArrayList<String>();
        List<String> listaAppoggio = new ArrayList<String>();
        List<String> nomeCambio = new ArrayList<String>();
        List<String> cambi = new ArrayList<String>();
        List<String> cambiIniziali = new ArrayList<String>();
        List<String> cambiInizialiLinee = new ArrayList<String>();
        int cambiLineeMetropolitane = 0;
        String nomeStazioneCambio = "";
        String ev="";
        List<String> linee = new ArrayList<String>();
        List<String> inMezzo = new ArrayList<String>();
        List<String> inMezzoNomi = new ArrayList<String>();


        FermataDAO fermataDAO = new FermataDAO();
        List<FermataRecordBean> fermateTot = fermataDAO.getFermateByIds(ids, city);

        String lineaTemp = "";
        String daRaggiungere="";
        String temp="";
        String successivo="";
        String success="";
        String daNonRipetere="";

        boolean check = false;
        boolean noPass=false;
        boolean controllo=false;
        boolean ciSonPassato=false;
        boolean stopping=false;
        boolean ancora=false;
        boolean uno=false;

        int countBin = 0;
        int quantoCiPasso=0;
        int contatore=0;
        int count=0;
        int conta=0;
        int checkino=0;

        for (int i = 0; i < fermateTot.size(); i++)
        {
            FermataRecordBean fermata = fermateTot.get(i);
            String fermate = fermata.getNome();
            String linea = fermata.getLinea();

            if (linea.contains("-") && i == 0) {
                controllo = true;
                cambiIniziali.add(fermate);
                cambiInizialiLinee.add(linea);
                uno = true;
            }

            if (linea.contains("-") && i == 1) {
                noPass = true;
                cambiIniziali.add(fermate);
                cambiInizialiLinee.add(linea);
                stopping = false;
                if (uno)
                    ancora = true;

            } else if ((!lineaTemp.equals(linea)) && (i == 1)) {
                noPass = true;
            }
            if ((!stopping) && (ancora)) {
                if (i > 1) {
                    if ((linea.contains("-"))) {
                        stopping = false;
                        noPass = true;
                        cambiIniziali.add(fermate);
                        cambiInizialiLinee.add(linea);
                        ancora = true;
                    } else {
                        stopping = true;
                        success = linea;
                        noPass = true;
                    }
                }
            }

            if (check) {
                if (!lineaTemp.equals(linea)) {
                    if (linea.contains("-")) {
                        quantoCiPasso = quantoCiPasso + 1;


                        inMezzo.add(linea);
                        inMezzoNomi.add(fermate);
                        check = true;
                        ciSonPassato = true;

//nuovo
                        if (i == percorsiCodifica.size() - 1) {
                            for (int k = 0; k < inMezzo.size(); k++) {
                                String[] appoggio = inMezzo.get(k).split("-");
                                for (String parola : appoggio) {
                                    if (!parola.equals(ev)) {
                                        conta = conta + 1;
                                        if (conta == appoggio.length) {
                                            cambiLineeMetropolitane = cambiLineeMetropolitane + 1;
                                            sequenzeNodiCruciali.add(inMezzoNomi.get(k - 1));
                                            sequenzediCambiamento.add(ev);
                                            for (int index = 0; index < inMezzo.size() - 1; index++) {
                                                String[] parolina = inMezzo.get(index).split("-");
                                                for (String p : parolina) {
                                                    if (!p.equals(ev)) {
                                                        String par = p;
                                                        String[] parolina2 = inMezzo.get(index + 1).split("-");
                                                        for (String g : parolina2) {
                                                            if (par.equals(g)) {
                                                                sequenzediCambiamento.add(par);
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    } else {
                                        conta = 0;
                                    }
                                }
                            }
                        }
                    } else {
                        cambiLineeMetropolitane = cambiLineeMetropolitane + 1;
                        sequenzediCambiamento.add(lineaTemp);
                        successivo = linea;
                        daNonRipetere = linea;
                        if (quantoCiPasso == 0)
                            sequenzeNodiCruciali.add(nomeStazioneCambio);
                        else {

                            for (int j = 0; j < inMezzo.size() - 1; j++) {
                                if (inMezzo.get(j).equals(inMezzo.get(j + 1))) {
                                    count = count + 1;
                                }
                            }
                            if (count == inMezzo.size() - 1) {
                                count = 0;
                                sequenzeNodiCruciali.add(inMezzoNomi.get(inMezzoNomi.size() - 1));
                            } else {


                                while (!(daRaggiungere.equals(ev))) {
                                    for (int j = 0; j < inMezzo.size(); j++) {
                                        if (inMezzo.get(j).contains(linea)) {
                                            temp = inMezzo.get(j);
                                            nomeCambio.add(inMezzoNomi.get(j)); //nome_cambio ho tutto
                                            listaAppoggio.add(temp);
                                            inMezzo.set(j, "");
                                            inMezzoNomi.set(j, "");

                                        }
                                    }
                                    //puo essere che lista appoggio abbia più di un elemento
                                    //ora strtok sugli elementi iesimi
                                    for (int l = 0; l < listaAppoggio.size(); l++) {
                                        checkino = 0;


                                        String[] parole = listaAppoggio.get(l).split("-");
                                        for (String parola : parole) {
                                            if (!(parola.equals(daNonRipetere))) //se parola != linea
                                            {
                                                daRaggiungere = parola;
                                                if (!(daRaggiungere.equals(ev))) {
                                                    linea = daRaggiungere;
                                                    sequenzediCambiamento.add(linea);
                                                    if (checkino == 0) {
                                                        cambiLineeMetropolitane = cambiLineeMetropolitane + 1;
                                                        cambi.add(nomeCambio.get(l));
                                                    }
                                                    checkino++;
                                                } else {
                                                    cambi.add(nomeCambio.get(l));
                                                    l = 1000;
                                                    break;
                                                }
                                            }
                                        }
                                    }
                                    listaAppoggio.clear();
                                    nomeCambio.clear();
                                }
                            }

                        }
                        inMezzo.clear();
                        inMezzoNomi.clear();
                        check = false;
                        lineaTemp = daNonRipetere;
                        quantoCiPasso = 0;
                        sequenzediCambiamento.add(successivo);

                    }
                } else {

                    check = false;
                    lineaTemp = linea;

                    if (ciSonPassato) {
                        inMezzo.clear();
                        inMezzoNomi.clear();
                        check = false;
                        lineaTemp = linea;
                        ciSonPassato = false;
                    }
                }
            } else {
                if (countBin != 0) {
                    if (!lineaTemp.equals(linea)) {

                        if ((i == 1) && (!controllo)) {
                            check = true;
                            nomeStazioneCambio = fermate; //la aggiungo solo se poi effettivamente rispetta check
                            noPass = false;
                            inMezzo.add(linea);
                            inMezzoNomi.add(fermate);
                            ev = lineaTemp;
                        } else {
                            if (!noPass) {
                                check = true;
                                nomeStazioneCambio = fermate; //la aggiungo solo se poi effettivamente rispetta check
                                inMezzo.add(linea);
                                inMezzoNomi.add(fermate);
                                ev = lineaTemp;
                                ciSonPassato = true;
                            } else {
                                noPass = false;
                                if (stopping) {
                                    for (int j = 0; j < cambiInizialiLinee.size(); j++) {
                                        String[] parole = cambiInizialiLinee.get(j).split("-");
                                        for (String parola : parole) {
                                            if (parola.equals(success)) {
                                                contatore++;
                                                nomeStazioneCambio = cambiIniziali.get(j);
                                            }
                                        }
                                    }
                                    if (contatore == cambiInizialiLinee.size()) {
                                        nomeStazioneCambio = "";
                                    } else {
                                        sequenzeNodiCruciali.add(nomeStazioneCambio);
                                        sequenzediCambiamento.add(success);
                                    }
                                    contatore = 0;
                                    cambiIniziali.clear();
                                    cambiInizialiLinee.clear();
                                }
                                lineaTemp = linea;


                            }
                        }

                    } else {
                        lineaTemp = linea;
                    }
                } else {
                    lineaTemp = linea;
                    countBin = countBin + 1;

                }
            }
            percorsiConFermate.add(fermate); //linea che consente di stampare i percorsi con fermate (non toccare)
            linee.add(linea);
        }//end for

        removeDuplicate(cambi, 0);

        for(int j=cambi.size()-1;j>=0;j--)
        {
            logger.info("Cambio {} ", cambi.get(j));
            sequenzeNodiCruciali.add(cambi.get(j));
        }
        cambiLineeMetropolitane = sequenzeNodiCruciali.size();
        logger.info("Cambi linee metropolitane in CityLifeController.java {}", cambiLineeMetropolitane);

        removeDuplicate(sequenzediCambiamento, 0);
        removeChangeLines(sequenzediCambiamento);


        CityLifeBean c = new CityLifeBean();
        c.setLinee(linee);
        c.setNumeroCambi(cambiLineeMetropolitane);
        c.setSequenzeDiCambiamento(sequenzediCambiamento);
        c.setSequenzeNodiCruciali(sequenzeNodiCruciali);
        c.setPercorsiConNomi(percorsiConFermate);
        c.setNumeroStazioniTotali(matriceAdiacenza[0].length);
        c.setSequenzeNodiCruciali(sequenzeNodiCruciali);

        return c;
    }


    protected void caricaMatriceDaClasspath(String resourcePath) {
        try (
                InputStream inputStream = getClass().getResourceAsStream(resourcePath);
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))
        ) {
            String line;
            int row = 0;
            while ((line = reader.readLine()) != null && row < 76) {
                String[] values = line.split(",");
                for (int col = 0; col < values.length && col < 76; col++) {
                    matriceAdiacenza[row][col] = Integer.parseInt(values[col].trim());
                }
                row++;
            }
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
            throw new RuntimeException("Errore nel caricamento della matrice da classpath", e);
        }
    }

    private boolean checkfill(int[]a, int dim)
    {
        for(int i=0;i<dim;i++)
        {
            if(a[i] == -1)
                return true;
        }
        return false;
    }
    private int[] trovaAdj(int nodo, int[][]adiacenza)
    {
        int index = 0;
        int [] adiacenti = new int[(adiacenza.length)-1];
        Arrays.fill(adiacenti,-1);
        for(int i=0;i< adiacenza.length;i++)
        {
            if((adiacenza[nodo][i]!=-1)&&(adiacenza[nodo][i]!=0))
            {
                adiacenti[index] = i;
                index = index + 1;
            }
        }
        return adiacenti;
    }
    private int nodoCostoMinore(int[]cost, int[]know)
    {
        int min=0;
        int minimoNodo=0;
        int temp = 10000;
        for(int i=0;i<cost.length;i++)
        {
            if(know[i]==-1)
            {
                min = cost[i];
                if(temp > min)
                {
                    temp = min;
                    minimoNodo = i;
                }
            }
        }
        return minimoNodo;
    }
    private void stampaPercorso(int nodo, int[] precedente, ArrayList<Integer> percorsiCodifica) {
        Deque<Integer> percorso = new ArrayDeque<>();
        int app;
        while (nodo != -1) {
            percorso.push(nodo);
            nodo = precedente[nodo];
        }
        while (!percorso.isEmpty()) {
            app = percorso.pop();
            percorsiCodifica.add(app);

        }
    }
    public ArrayList<Integer> Dijkstra(int partenza, int arrivo) throws FuoriRangeExceptionRemoli, UnreacheableNodeExceptionRemoli {
        if (partenza < 0 || partenza >= matriceAdiacenza.length)
            throw new FuoriRangeExceptionRemoli("ID partenza fuori range: " + partenza, FuoriRangeExceptionRemoli.Severity.CRITICAL );

        if (arrivo < 0 || arrivo >= matriceAdiacenza.length)
            throw new FuoriRangeExceptionRemoli("ID arrivo fuori range: " + arrivo, FuoriRangeExceptionRemoli.Severity.CRITICAL);


        ArrayList<Integer> percorsiCodifica = new ArrayList<Integer>();
        int nodoPartenza = partenza;
        int nodoArrivo = arrivo;
        int [] know = new int[matriceAdiacenza.length];
        int [] cost = new int[matriceAdiacenza.length];
        int [] adiacentiVector = new int[(matriceAdiacenza.length)-1];
        int adjTemp = 0;
        int[] precedente = new int[matriceAdiacenza.length];


        Arrays.fill(cost, 1000);
        Arrays.fill(know, -1);
        know[nodoPartenza] = 1;
        cost[nodoPartenza] = 0;
        Arrays.fill(precedente, -1);

        while(this.checkfill(know,matriceAdiacenza.length))
        {
            adiacentiVector = this.trovaAdj(nodoPartenza, matriceAdiacenza);
            for(int i=0;i<adiacentiVector.length;i++)
            {
                if(adiacentiVector[i]!=-1)
                {
                    adjTemp = adiacentiVector[i];
                    if(know[adjTemp]==-1)
                    {
                        if(cost[adjTemp] > cost[nodoPartenza] + matriceAdiacenza[nodoPartenza][adjTemp])
                        {
                            cost[adjTemp] = cost[nodoPartenza] + matriceAdiacenza[nodoPartenza][adjTemp];
                            precedente[adjTemp] = nodoPartenza;
                        }
                    }
                }
            }
            nodoPartenza = this.nodoCostoMinore(cost,know);
            know[nodoPartenza] = 1;
        }
        if (cost[nodoArrivo] == 1000 || precedente[nodoArrivo] == -1) {
            throw new UnreacheableNodeExceptionRemoli(
                    "Il percorso verso la stazione selezionata non è disponibile.",
                    "Nodo " + nodoArrivo + " irraggiungibile da " + nodoPartenza,
                    UnreacheableNodeExceptionRemoli.Severity.CRITICAL
            );
        }
        for (int i = 0; i < matriceAdiacenza.length; i++)
            if(i==nodoArrivo) this.stampaPercorso(i, precedente, percorsiCodifica);

        return percorsiCodifica;
    }
}
