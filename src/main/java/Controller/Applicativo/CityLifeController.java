package Controller.Applicativo;

import Bean.CityLifeBean;
import Bean.FermataRecordBean;
import Model.DAO.FermataDAO;

import java.io.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;

public class CityLifeController
{


    public int[][] matriceAdiacenza; //le classi figlie possono specializzare la loro matrice di adiacenza

    private void remove_change_lines(ArrayList<String> Sequenze_di_cambiamento_full)
    {
        for(int i=0;i<Sequenze_di_cambiamento_full.size();i++)
        {
            if(Sequenze_di_cambiamento_full.get(i).contains("-"))
            {
                Sequenze_di_cambiamento_full.remove(i);
            }
        }
        for(int i=0;i<Sequenze_di_cambiamento_full.size();i++)
        {
            System.out.println("Valore = " + Sequenze_di_cambiamento_full.get(i));
        }
    }
    private void remove_duplicate(ArrayList<String> Sequenze_di_cambiamento_full,int indice) {
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

    public CityLifeBean calcolaPercorso(ArrayList<Integer> ids, String city) throws SQLException {
        ArrayList<String> Percorsi_Con_Fermate = new ArrayList<String>();
        ArrayList<Integer> Percorsi_Codifica = new ArrayList<Integer>();
        ArrayList<String> Sequenze_di_cambiamento = new ArrayList<String>();
        ArrayList<String> Sequenze_nodi_cruciali = new ArrayList<String>();
        ArrayList<String> lista_appoggio = new ArrayList<String>();
        ArrayList<String> nome_cambio = new ArrayList<String>();
        ArrayList<String> cambi = new ArrayList<String>();
        ArrayList<String> cambi_iniziali = new ArrayList<String>();
        ArrayList<String> cambi_iniziali_linee = new ArrayList<String>();
        int cambi_linee_metropolitane = 0;
        String nome_stazione_cambio = "";
        String ev="";
        String precedente = "";
        ArrayList<String> linee = new ArrayList<String>();
        ArrayList<String> in_mezzo = new ArrayList<String>();
        ArrayList<String> in_mezzo_nomi = new ArrayList<String>();


        FermataDAO fermataDAO = new FermataDAO();
        List<FermataRecordBean> fermate_tot = fermataDAO.getFermateByIds(ids, city);


        String linea_temp = "", da_raggiungere="",temp="",successivo="",success="", da_non_ripetere="";
        boolean check = false, no_pass=false,controllo=false,ci_son_passato=false,stopping=false,ancora=false,uno=false;
        int count_bin = 0,quanto_ci_passo=0,contatore=0,count=0,conta=0,checkino=0;

        for (int i = 0; i < fermate_tot.size(); i++)
        {
            FermataRecordBean fermata = fermate_tot.get(i);
            String fermate = fermata.getNome();
            String linea = fermata.getLinea();

            if (((linea.contains("-"))) && (i == 0)) {
                controllo = true;
                cambi_iniziali.add(fermate);
                cambi_iniziali_linee.add(linea);
                uno = true;
            }

            if (((linea.contains("-"))) && (i == 1)) {
                no_pass = true;
                cambi_iniziali.add(fermate);
                cambi_iniziali_linee.add(linea);
                stopping = false;
                if (uno)
                    ancora = true;

            } else if ((!linea_temp.equals(linea)) && (i == 1)) {
                no_pass = true;
            }
            if ((stopping == false) && (ancora == true)) {
                if (i > 1) {
                    if ((linea.contains("-"))) {
                        stopping = false;
                        no_pass = true;
                        cambi_iniziali.add(fermate);
                        cambi_iniziali_linee.add(linea);
                        ancora = true;
                    } else {
                        stopping = true;
                        success = linea;
                        no_pass = true;
                    }
                }
            }

            if (check) {
                if (!linea_temp.equals(linea)) {
                    if (linea.contains("-")) {
                        quanto_ci_passo = quanto_ci_passo + 1;


                        in_mezzo.add(linea);
                        in_mezzo_nomi.add(fermate);
                        check = true;
                        ci_son_passato = true;

//nuovo
                        if (i == Percorsi_Codifica.size() - 1) {
                            for (int k = 0; k < in_mezzo.size(); k++) {
                                String[] appoggio = in_mezzo.get(k).split("-");
                                for (String parola : appoggio) {
                                    if (!parola.equals(ev)) {
                                        conta = conta + 1;
                                        if (conta == appoggio.length) {
                                            cambi_linee_metropolitane = cambi_linee_metropolitane + 1;
                                            Sequenze_nodi_cruciali.add(in_mezzo_nomi.get(k - 1));
                                            Sequenze_di_cambiamento.add(ev);
                                            for (int index = 0; index < in_mezzo.size() - 1; index++) {
                                                String[] parolina = in_mezzo.get(index).split("-");
                                                for (String p : parolina) {
                                                    if (!p.equals(ev)) {
                                                        String par = p;
                                                        String[] parolina2 = in_mezzo.get(index + 1).split("-");
                                                        for (String g : parolina2) {
                                                            if (par.equals(g)) {
                                                                Sequenze_di_cambiamento.add(par);
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
                        cambi_linee_metropolitane = cambi_linee_metropolitane + 1;
                        Sequenze_di_cambiamento.add(linea_temp);
                        successivo = linea;
                        da_non_ripetere = linea;
                        if (quanto_ci_passo == 0)
                            Sequenze_nodi_cruciali.add(nome_stazione_cambio);
                        else {

                            for (int j = 0; j < in_mezzo.size() - 1; j++) {
                                if (in_mezzo.get(j).equals(in_mezzo.get(j + 1))) {
                                    count = count + 1;
                                }
                            }
                            if (count == in_mezzo.size() - 1) {
                                count = 0;
                                Sequenze_nodi_cruciali.add(in_mezzo_nomi.get(in_mezzo_nomi.size() - 1));
                            } else {


                                while (!(da_raggiungere.equals(ev))) {
                                    for (int j = 0; j < in_mezzo.size(); j++) {
                                        if (in_mezzo.get(j).contains(linea)) {
                                            temp = in_mezzo.get(j);
                                            nome_cambio.add(in_mezzo_nomi.get(j)); //nome_cambio ho tutto
                                            lista_appoggio.add(temp);
                                            in_mezzo.set(j, "");
                                            in_mezzo_nomi.set(j, "");

                                        }
                                    }
                                    //puo essere che lista appoggio abbia più di un elemento
                                    //ora strtok sugli elementi iesimi
                                    for (int l = 0; l < lista_appoggio.size(); l++) {
                                        checkino = 0;


                                        String[] parole = lista_appoggio.get(l).split("-");
                                        for (String parola : parole) {
                                            //System.out.println(parola);
                                            if (!(parola.equals(da_non_ripetere))) //se parola != linea
                                            {
                                                da_raggiungere = parola;
                                                if (!(da_raggiungere.equals(ev))) {
                                                    linea = da_raggiungere;
                                                    Sequenze_di_cambiamento.add(linea);
                                                    if (checkino == 0) {
                                                        cambi_linee_metropolitane = cambi_linee_metropolitane + 1;
                                                        cambi.add(nome_cambio.get(l));
                                                    }
                                                    checkino++;
                                                    // l = 1000;
                                                    //break; //esce dal for
                                                } else {
                                                    cambi.add(nome_cambio.get(l)); //ok
                                                    l = 1000;
                                                    break;
                                                }

                                                //linea = da_raggiungere;


                                            }

                                        }

                                    }

                                    lista_appoggio.clear();
                                    nome_cambio.clear();
                                }
                            }

                        }
                        in_mezzo.clear();
                        in_mezzo_nomi.clear();
                        check = false;
                        linea_temp = da_non_ripetere;
                        quanto_ci_passo = 0;
                        Sequenze_di_cambiamento.add(successivo);

                    }
                } else {

                    check = false;
                    linea_temp = linea;

                    if (ci_son_passato == true) {
                        in_mezzo.clear();
                        in_mezzo_nomi.clear();
                        check = false;
                        linea_temp = linea;
                        ci_son_passato = false;
                    }
                }
            } else {
                if (!(count_bin == 0)) {
                    if (!linea_temp.equals(linea)) {

                        if ((i == 1) && (controllo == false)) {
                            check = true;
                            precedente = linea;
                            nome_stazione_cambio = fermate; //la aggiungo solo se poi effettivamente rispetta check
                            no_pass = false;
                            in_mezzo.add(linea);
                            in_mezzo_nomi.add(fermate);
                            ev = linea_temp;
                        } else {
                            if (no_pass == false) {
                                check = true;
                                precedente = linea;
                                nome_stazione_cambio = fermate; //la aggiungo solo se poi effettivamente rispetta check


                                in_mezzo.add(linea);
                                in_mezzo_nomi.add(fermate);
                                ev = linea_temp;
                                ci_son_passato = true;


                            } else {
                                no_pass = false;

                                if (stopping == true) {
                                    for (int j = 0; j < cambi_iniziali_linee.size(); j++) {
                                        String[] parole = cambi_iniziali_linee.get(j).split("-");
                                        for (String parola : parole) {
                                            if (parola.equals(success)) {
                                                contatore++;
                                                nome_stazione_cambio = cambi_iniziali.get(j);
                                            }
                                        }
                                    }
                                    if (contatore == cambi_iniziali_linee.size()) {
                                        System.out.println("Nessun cambio");
                                        nome_stazione_cambio = "";
                                    } else {
                                        Sequenze_nodi_cruciali.add(nome_stazione_cambio);
                                        Sequenze_di_cambiamento.add(success);
                                    }
                                    contatore = 0;
                                    cambi_iniziali.clear();
                                    cambi_iniziali_linee.clear();
                                }
                                linea_temp = linea;


                            }
                        }

                    } else {
                        linea_temp = linea;
                    }
                } else if (count_bin == 0) {
                    linea_temp = linea;
                    count_bin = count_bin + 1;

                }
            }
            Percorsi_Con_Fermate.add(fermate); //linea che consente di stampare i percorsi con fermate (non toccare)
            linee.add(linea);
        }//end for

        remove_duplicate(cambi, 0);

        for(int j=cambi.size()-1;j>=0;j--)
        {
            System.out.println(" CAMBIO = " + cambi.get(j));
            Sequenze_nodi_cruciali.add(cambi.get(j));
        }
        cambi_linee_metropolitane = Sequenze_nodi_cruciali.size();
        System.out.println("CAMBI LINEE METROPOLITANE in CityLifeController.Java = " + cambi_linee_metropolitane);


        remove_duplicate(Sequenze_di_cambiamento, 0);
        remove_change_lines(Sequenze_di_cambiamento);


        CityLifeBean c = new CityLifeBean();
        c.setLinee(linee);
        c.setNumero_cambi(cambi_linee_metropolitane);
        c.setSequenze_di_cambiamento(Sequenze_di_cambiamento);
        c.setSequenze_nodi_cruciali(Sequenze_nodi_cruciali);
        c.setPercorsi_Con_Nomi(Percorsi_Con_Fermate);
        c.setNumero_stazioni_totali(matriceAdiacenza[0].length);
        c.setSequenze_nodi_cruciali(Sequenze_nodi_cruciali);
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

    private void views_arrays(int[]a,int[]b, int[][]matrice)
    {
        //System.out.print("Matrice.lenght =  "+ matrice.length);
        System.out.print("Know = ");
        for(int i=0; i< matrice.length; i++)
        {
            System.out.print( "[ " + a[i] + " ] ");
        }
        System.out.println();
        System.out.print("Cost = ");
        for(int i=0; i< matrice.length; i++)
        {
            System.out.print( "[ " + b[i] + " ] ");
        }

    }
    private boolean checkfill(int[]a, int[]b, int dim)
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
    private int nodo_costo_minore(int[]cost, int[]know)
    {
        int min=0, minimo_nodo=0, temp = 10000;
        for(int i=0;i<cost.length;i++)
        {
            if(know[i]==-1)
            {
                min = cost[i];

                if(temp > min)
                {
                    temp = min;
                    minimo_nodo = i;
                }
            }
        }
        return minimo_nodo;
    }
    private void stampaPercorso(int nodo, int[] precedente, ArrayList<Integer> percorsi_codifica) {
        Stack<Integer> percorso = new Stack<>();
        int app;
        while (nodo != -1) {
            percorso.push(nodo);
            nodo = precedente[nodo];
        }
        while (!percorso.isEmpty()) {
            app = percorso.pop();
            System.out.print(app + " ");
            percorsi_codifica.add(app);

        }
    }
    public ArrayList<Integer> Dijkstra(int partenza, int arrivo, String city) throws Exception
    {
        ArrayList<Integer> percorsi_codifica = new ArrayList<Integer>();
        int nodo_partenza = partenza, nodo_arrivo = arrivo;
        int [] know = new int[matriceAdiacenza.length];
        int [] cost = new int[matriceAdiacenza.length];
        Arrays.fill(cost, 1000);
        Arrays.fill(know, -1);
        this.views_arrays(know,cost, matriceAdiacenza);
        System.out.println();
        int [] adiacenti_vector = new int[(matriceAdiacenza.length)-1];
        int adj_temp = 0;
        know[nodo_partenza] = 1;
        cost[nodo_partenza] = 0;
        System.out.println();
        int[] precedente = new int[matriceAdiacenza.length];  // Array per tracciare il percorso
        Arrays.fill(precedente, -1);

        while(this.checkfill(know,cost,matriceAdiacenza.length))
        {
            adiacenti_vector = this.trovaAdj(nodo_partenza, matriceAdiacenza);
            for(int i=0;i<adiacenti_vector.length;i++)
            {
                if(adiacenti_vector[i]!=-1)
                {
                    adj_temp = adiacenti_vector[i];
                    if(know[adj_temp]==-1)
                    {
                        if(cost[adj_temp] > cost[nodo_partenza] + matriceAdiacenza[nodo_partenza][adj_temp])
                        {
                            cost[adj_temp] = cost[nodo_partenza] + matriceAdiacenza[nodo_partenza][adj_temp];
                            precedente[adj_temp] = nodo_partenza;
                        }
                    }
                }
            }
            nodo_partenza = this.nodo_costo_minore(cost,know);
            know[nodo_partenza] = 1;
        }
        this.views_arrays(know,cost, matriceAdiacenza);
        System.out.println();
        System.out.println("Path.");
        for (int i = 0; i < matriceAdiacenza.length; i++)
        {
            if(i==nodo_arrivo) { //se levassi questa condizione mi stamperebbe tutti i percorsi dalla stazione di partenza to *
                System.out.print("Nodo " + i + ": Num fermate da attraversare = " + cost[i] + ", Percorso = ");
                this.stampaPercorso(i, precedente, percorsi_codifica); // si crea solo questa
                System.out.println();
            }
        }
        return percorsi_codifica;
    }


}
