package it.web.routex.utility.builder;

import it.web.routex.boundary.cli.view.PathNOREGCLI;
import it.web.routex.dao.PathNoRegData;

import java.util.List;

public class PathNORegBuilder
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

    public PathNORegBuilder(List<String> a)
    {
        this.percorsiConNomi = a;
    }
    public PathNORegBuilder numeroCambi(int b)
    {
        this.numeroCambi = b;
        return this;
    }
    public PathNORegBuilder linee(List<String> linee)
    {
        this.linee = linee;
        return this;
    }
    public PathNORegBuilder numeroStazioniUsate(int n)
    {
        this.numeroStazioniUsate = n;
        return this;
    }
    public PathNORegBuilder minutaggio(double minutaggio)
    {
        this.minutaggio = minutaggio;
        return this;
    }
    public PathNORegBuilder numeroStazioniTotali(int numeroStazioniTotali)
    {
        this.numeroStazioniTotali = numeroStazioniTotali;
        return this;
    }
    public PathNORegBuilder percentualeStazioniUsate(double percentualeStazioniUsate)
    {
        this.percentualeStazioniUsate = percentualeStazioniUsate;
        return this;
    }
    public PathNORegBuilder sequenzeDiCambiamento(List<String> sequenzeDiCambiamento)
    {
        this.sequenzeDiCambiamento = sequenzeDiCambiamento;
        return this;
    }
    public PathNORegBuilder sequenzeNodiCruciali(List<String> sequenzeNodiCruciali)
    {
        this.sequenzeNodiCruciali = sequenzeNodiCruciali;
        return this;
    }

    public PathNOREGCLI build() {
        PathNoRegData data = new PathNoRegData();
        data.setLinee(linee);
        data.setMinutaggio(minutaggio);
        data.setNumeroCambi(numeroCambi);
        data.setNumeroStazioniTotali(numeroStazioniTotali);
        data.setNumeroStazioniUsate(numeroStazioniUsate);
        data.setPercentualeStazioniUsate(percentualeStazioniUsate);
        data.setPercorsiConNomi(percorsiConNomi);
        data.setSequenzeDiCambiamento(sequenzeDiCambiamento);
        data.setSequenzeNodiCruciali(sequenzeNodiCruciali);
        return new PathNOREGCLI(data);

    }

}
