package Bean;

import java.util.ArrayList;

public class CityLifeBean //OGGETTO DI TRASPORTO
{
    ArrayList<String> Percorsi_Con_Nomi = new ArrayList<String>();
    private int numero_cambi;
    ArrayList<String> linee = new ArrayList<String>();
    ArrayList<String> Sequenze_di_cambiamento = new ArrayList<String>();
    ArrayList<String> Sequenze_nodi_cruciali = new ArrayList<String>();
    int numero_stazioni;

    public void setNumero_stazioni_totali(int numero_stazioni) {
        this.numero_stazioni = numero_stazioni;
    }

    public int getNumero_stazioni_totali() {
        return numero_stazioni;
    }

    public ArrayList<String> getLinee() {
        return linee;
    }

    public ArrayList<String> getPercorsi_Con_Nomi() {
        return Percorsi_Con_Nomi;
    }

    public ArrayList<String> getSequenze_di_cambiamento() {
        return Sequenze_di_cambiamento;
    }

    public ArrayList<String> getSequenze_nodi_cruciali() {
        return Sequenze_nodi_cruciali;
    }

    public int getNumero_cambi() {
        return numero_cambi;
    }

    public void setLinee(ArrayList<String> linee) {
        this.linee = linee;
    }

    public void setNumero_cambi(int numero_cambi) {
        this.numero_cambi = numero_cambi;
    }


    public void setPercorsi_Con_Nomi(ArrayList<String> percorsi_Con_Nomi) {
        Percorsi_Con_Nomi = percorsi_Con_Nomi;
    }

    public void setSequenze_di_cambiamento(ArrayList<String> sequenze_di_cambiamento) {
        Sequenze_di_cambiamento = sequenze_di_cambiamento;
    }

    public void setSequenze_nodi_cruciali(ArrayList<String> sequenze_nodi_cruciali) {
        Sequenze_nodi_cruciali = sequenze_nodi_cruciali;
    }
}
