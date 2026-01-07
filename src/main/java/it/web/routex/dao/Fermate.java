package it.web.routex.dao;

public class Fermate {

    private int id;
    private String nome;
    private boolean disabile;
    private String linea;

    public Fermate(int id, String nome, boolean disabile, String linea) {
        this.id = id;
        this.nome = nome;
        this.disabile = disabile;
        this.linea = linea;
    }

    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public boolean isDisabile() {
        return disabile;
    }

    public String getLinea() {
        return linea;
    }
}
