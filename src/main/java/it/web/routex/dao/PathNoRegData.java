package it.web.routex.dao;

import java.util.List;

public class PathNoRegData
{
    private List<String> percorsiConNomi;
    private int numeroCambi;
    private List<String> linee;
    private int numeroStazioniUsate;
    private double minutaggio;
    private int numeroStazioniTotali;
    private double percentualeStazioniUsate;
    private List<String> sequenzeDiCambiamento;
    private List<String> sequenzeNodiCruciali;

    public List<String> getSequenzeNodiCruciali() {
        return sequenzeNodiCruciali;
    }

    public List<String> getSequenzeDiCambiamento() {
        return sequenzeDiCambiamento;
    }

    public List<String> getPercorsiConNomi() {
        return percorsiConNomi;
    }

    public List<String> getLinee() {
        return linee;
    }

    public int getNumeroStazioniUsate() {
        return numeroStazioniUsate;
    }

    public int getNumeroStazioniTotali() {
        return numeroStazioniTotali;
    }

    public int getNumeroCambi() {
        return numeroCambi;
    }

    public double getPercentualeStazioniUsate() {
        return percentualeStazioniUsate;
    }

    public double getMinutaggio() {
        return minutaggio;
    }

    public void setMinutaggio(double minutaggio) {
        this.minutaggio = minutaggio;
    }

    public void setLinee(List<String> linee) {
        this.linee = linee;
    }

    public void setNumeroCambi(int numeroCambi) {
        this.numeroCambi = numeroCambi;
    }

    public void setNumeroStazioniTotali(int numeroStazioniTotali) {
        this.numeroStazioniTotali = numeroStazioniTotali;
    }

    public void setNumeroStazioniUsate(int numeroStazioniUsate) {
        this.numeroStazioniUsate = numeroStazioniUsate;
    }

    public void setPercentualeStazioniUsate(double percentualeStazioniUsate) {
        this.percentualeStazioniUsate = percentualeStazioniUsate;
    }

    public void setPercorsiConNomi(List<String> percorsiConNomi) {
        this.percorsiConNomi = percorsiConNomi;
    }

    public void setSequenzeDiCambiamento(List<String> sequenzeDiCambiamento) {
        this.sequenzeDiCambiamento = sequenzeDiCambiamento;
    }

    public void setSequenzeNodiCruciali(List<String> sequenzeNodiCruciali) {
        this.sequenzeNodiCruciali = sequenzeNodiCruciali;
    }
}
