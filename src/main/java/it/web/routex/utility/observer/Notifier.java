package it.web.routex.utility.observer;

public class Notifier extends Subject {

    private Notifier() {
        attach(new InvioEmail());
        attach(new CacheInvalidator());
    }
    private static class LazyContainer {
        private static final Notifier singletonInstance = new Notifier();
    }

    public static Notifier getInstanceSingleton() {
        return LazyContainer.singletonInstance;
    }

    public void comunicazioneInviata() {
        notifyObservers(EventType.COMUNICAZIONE_CORRETTAMENTE_INVIATA);
    }

    public void comunicazioneRisolta() {
        notifyObservers(EventType.COMUNICAZIONE_CORRETTAMENTE_RISOLTA);
    }
}
