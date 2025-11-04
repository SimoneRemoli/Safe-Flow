package Controller.Grafico;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 * Listener globale che viene eseguito all'avvio e allo spegnimento dell'applicazione.
 * Utilizzato per invalidare tutte le sessioni al riavvio dell'applicazione.
 */
@WebListener
public class AppContextListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        sce.getServletContext().setAttribute("forceLogout", Boolean.TRUE);
        sce.getServletContext().setAttribute("appStarted", Boolean.TRUE);
        System.out.println(" RouteX avviata: flag di riavvio impostato.");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        System.out.println(" RouteX arrestata: contesto servlet distrutto.");
    }
}
