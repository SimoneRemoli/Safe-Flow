package it.web.routex.controller.grafico;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;

@WebServlet("/deleteMap")
public class DeleteMapControllerGrafico extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String fileName = request.getParameter("fileName");
        HttpSession session = request.getSession();

        if (fileName == null || fileName.isEmpty()) {
            session.setAttribute("deleteMessage", "Nessun file selezionato.");
        } else {
            String appPath = request.getServletContext().getRealPath("");
            File file = new File(appPath + File.separator + "images" + File.separator + "maps", fileName);

            if (file.exists() && file.isFile()) {
                if (!file.delete())
                    session.setAttribute("deleteMessage", "Errore nella rimozione della mappa: " + fileName);
            } else {
                session.setAttribute("deleteMessage", "Mappa non trovata: " + fileName);
            }
        }



        // Redirect al servlet che ricarica la lista aggiornata
        response.sendRedirect(request.getContextPath() + "/viewMaps");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response);
    }


}