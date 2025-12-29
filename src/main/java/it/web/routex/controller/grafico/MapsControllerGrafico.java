package it.web.routex.controller.grafico;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/viewMaps")
public class MapsControllerGrafico extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        System.out.println("[MapsControllerGrafico] Caricamento mappe...");

        // Percorso assoluto cartella /images/maps sotto webapp
        String folderPath = getServletContext().getRealPath("/images/maps");
        File folder = new File(folderPath);

        List<String> fileNames = new ArrayList<>();

        if (!folder.exists()) {
            System.out.println("[MapsControllerGrafico] Cartella non trovata: " + folderPath);
        } else {
            File[] files = folder.listFiles();
            if (files != null) {
                for (File f : files) {
                    if (f.isFile()) {
                        fileNames.add(f.getName());
                    }
                }
            }
        }

        request.setAttribute("listOfFiles", fileNames);

        request.getRequestDispatcher("viewMaps.jsp").forward(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response);
    }

}