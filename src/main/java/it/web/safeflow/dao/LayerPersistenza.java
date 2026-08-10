package it.web.safeflow.dao;
import it.web.safeflow.exception.*;
import it.web.safeflow.model.*;
import it.web.safeflow.model.Credentials;
import java.util.List;

public abstract class LayerPersistenza {

    private List<City> cachedCities;
    private List<Notification> cachedNotifications;
    private long cachedCitiesTimestamp;
    private static final long CITY_CACHE_TTL_MS = 5L * 60 * 1000; //5 minuti = 300.000 millisecondi

    public abstract Credentials login(String email, String password) throws DAOExceptionRemoli, LoginNotFoundRemoli;

    public abstract void registerTraveler(String nome,
                                          String cognome,
                                          String codiceFiscale,
                                          String email,
                                          String password,
                                          java.sql.Date dataDiNascita,
                                          boolean disabile) throws DAOExceptionRemoli;

    public final List<City> listCitiesRAM() throws DAOExceptionRemoli {

        long now = System.currentTimeMillis();

        if (cachedCities == null || isCityCacheExpired(now)) {
            cachedCities = listCities();
            cachedCitiesTimestamp = now;
        }

        return cachedCities;
    }
    private boolean isCityCacheExpired(long now) {
        return (now - cachedCitiesTimestamp) > CITY_CACHE_TTL_MS;
    }

    public final List<Notification> getMessagesRAM() throws DAOExceptionRemoli {
        if (cachedNotifications == null) {
            cachedNotifications = getMessages(); // DB una volta
        }
        return cachedNotifications;
    }

    public final void invalidateNotificationsCache() {
        cachedNotifications = null;
    }
    public abstract List<City> listCities() throws DAOExceptionRemoli;

    public abstract List<Notification> getMessages() throws DAOExceptionRemoli;

    public abstract void sendMessage(Notification notification) throws DAOExceptionRemoli;

    public abstract void markNotificationAsRead(Notification notification) throws DAOExceptionRemoli;

    public abstract boolean approvePendingTravelerNotification(Notification notification) throws DAOExceptionRemoli;

    public abstract boolean rejectPendingTravelerNotification(Notification notification) throws DAOExceptionRemoli;

    public abstract List<Credentials> listAdmins() throws DAOExceptionRemoli;

    public abstract void createAdmin(String nome,
                                     String cognome,
                                     String email,
                                     String password,
                                     String codiceFiscale) throws DAOExceptionRemoli;

    public abstract int deleteAdmins(List<String> codiceFiscali) throws DAOExceptionRemoli;

    public abstract List<Credentials> listTravelers() throws DAOExceptionRemoli;

    public abstract int deleteTravelers(List<String> codiceFiscali) throws DAOExceptionRemoli;

}
