package it.web.routex.controller.grafico;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@WebServlet("/uploadMap")
@MultipartConfig

public class UploadMapControllerGrafico extends HttpServlet {


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Part filePart = request.getPart("mapImage");
        String appPath = request.getServletContext().getRealPath("");

        String message;
        try {
            message = uploadMap(filePart, appPath);
        } catch (IOException ex) {
            message = "Errore durante l'upload della mappa: " + ex.getMessage();
        }

        request.setAttribute("uploadMessage", message);
        response.sendRedirect(request.getContextPath() + "/viewMaps");
    }

    // ---- Metodo di upload con auto-rinomina ----
    public static String uploadMap(Part filePart, String appPath) throws IOException {
        if (filePart == null || filePart.getSize() == 0) {
            return "Nessun file selezionato.";
        }

        String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
        String lowerName = fileName.toLowerCase();

        if (!lowerName.endsWith(".jpg") && !lowerName.endsWith(".jpeg") && !lowerName.endsWith(".png")) {
            return "Formato non supportato. Usa JPG, JPEG o PNG.";
        }

        String uploadPath = appPath + File.separator + "images" + File.separator + "maps";
        File uploadDir = new File(uploadPath);

        if (!uploadDir.exists()) {
            boolean created = uploadDir.mkdirs();
            if (!created) {
                throw new IOException("Impossibile creare la directory: " + uploadDir.getAbsolutePath());
            }
        }

        // Auto-rinomina se il file esiste già
        File file = new File(uploadDir, fileName);
        String baseName = fileName.contains(".") ? fileName.substring(0, fileName.lastIndexOf('.')) : fileName;
        String extension = fileName.contains(".") ? fileName.substring(fileName.lastIndexOf('.')) : "";
        int counter = 1;
        while (file.exists()) {
            file = new File(uploadDir, baseName + "_" + counter + extension);
            counter++;
        }

        try (InputStream input = filePart.getInputStream()) {
            Files.copy(input, file.toPath(), StandardCopyOption.REPLACE_EXISTING);
        }

        return "Mappa caricata con successo: " + file.getName();
    }
}