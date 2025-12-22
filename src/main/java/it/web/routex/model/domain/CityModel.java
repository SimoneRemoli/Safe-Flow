package it.web.routex.model.domain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

public abstract class CityModel {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    protected int[][] matriceAdiacenza;

    protected CityModel(int size, String resourcePath) {
        this.matriceAdiacenza = new int[size][size];
        caricaMatriceDaClasspath(resourcePath);
    }

    public int[][] getMatriceAdiacenza() {
        return matriceAdiacenza;
    }

    public int getNumeroStazioni() {
        return matriceAdiacenza.length;
    }

    protected void caricaMatriceDaClasspath(String resourcePath)
    {
        final Logger logger = LoggerFactory.getLogger(getClass());

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
            logger.error("Matrice caricata correttamente da classpath");
        } catch (IOException | NumberFormatException e) {
            logger.error("Errore nel caricamento della matrice da classpath");
        }
    }
}
