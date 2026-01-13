package it.web.routex.utility.observer;


public class InvioEmail implements Observer {

    @Override
    public void update(EventType eventType) {

        if (eventType == EventType.COMUNICAZIONE_CORRETTAMENTE_INVIATA) {
            System.out.println("📧 Email inviata a tutti i lavoratori");
        }
        if (eventType == EventType.COMUNICAZIONE_CORRETTAMENTE_RISOLTA) {
            System.out.println("📧 Comunicazione risolta");
        }

    }
}
