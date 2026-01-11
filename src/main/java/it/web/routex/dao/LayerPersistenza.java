package it.web.routex.dao;
import it.web.routex.exception.DAOExceptionRemoli;
import it.web.routex.exception.LoginNotFoundRemoli;
import it.web.routex.exception.PaymentValidationExceptionRemoli;
import it.web.routex.exception.PathNotFoundExceptionRemoli;
import it.web.routex.model.*;

import it.web.routex.bean.FermataRecordBean;

import java.sql.SQLException;
import java.util.List;

public abstract class LayerPersistenza {

    public abstract void login(String email, String password) throws DAOExceptionRemoli, LoginNotFoundRemoli;

    public abstract Mastercard getPaymentMastercard(String nC, String sc, String cvv) throws DAOExceptionRemoli, PaymentValidationExceptionRemoli;

    public abstract Paypal getPaymentPaypal(String email, String codice) throws DAOExceptionRemoli, PaymentValidationExceptionRemoli;

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

}
