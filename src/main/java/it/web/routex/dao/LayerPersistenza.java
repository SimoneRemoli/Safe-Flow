package it.web.routex.dao;
import it.web.routex.exception.*;
import it.web.routex.model.*;
import it.web.routex.utility.singleton.Credentials;
import java.sql.SQLException;
import java.util.List;

public abstract class LayerPersistenza {

    private List<City> cachedCities;
    private List<Notification> cachedNotifications;
    private long cachedCitiesTimestamp;
    private static final long CITY_CACHE_TTL_MS = 5L * 60 * 1000; //5 minuti = 300.000 millisecondi

    public abstract Credentials login(String email, String password) throws DAOExceptionRemoli, LoginNotFoundRemoli;

    public abstract Mastercard getPaymentMastercard(String nC, String sc, String cvv) throws DAOExceptionRemoli, PaymentValidationExceptionRemoli;

    public abstract Paypal getPaymentPaypal(String email, String codice) throws DAOExceptionRemoli, PaymentValidationExceptionRemoli;


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

    public abstract List<Fermata> getFermateByIds(List<Integer> ids, String city) throws SQLException;

    public abstract List<Station> restituisciIdStazioni(String startStation, String endStation, String city) throws SQLException;

    public abstract List<Notification> getMessages() throws DAOExceptionRemoli;

    public abstract void save(Route route) throws DAOExceptionRemoli;

    public abstract WorkerSchedule getWorkerSchedule(String codiceFiscale) throws DAOExceptionRemoli;

    public abstract void sendMessage(Notification notification) throws DAOExceptionRemoli;

    public abstract void solvedNotification(Notification notification) throws DAOExceptionRemoli;

    public abstract List<Route> getAllPathInfo() throws DAOExceptionRemoli;

    public abstract List<Route> getData(String cf) throws PathNotFoundExceptionRemoli, DAOExceptionRemoli;

    public abstract void salvataggio(
            Credentials cred,
            List<String> codiciBiglietti,
            String metodoPagamento,
            String city) throws CredentialsExceptionRemoli;

}
