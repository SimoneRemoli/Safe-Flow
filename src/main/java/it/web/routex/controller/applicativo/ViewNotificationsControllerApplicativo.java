package it.web.routex.controller.applicativo;
import it.web.routex.bean.MessageBean;
import it.web.routex.dao.LayerPersistenza;
import it.web.routex.exception.BrondiException;
import it.web.routex.exception.DAOExceptionRemoli;
import it.web.routex.model.Notification;
import it.web.routex.utility.factory.FactoryLayerPersistenza;
import java.util.ArrayList;
import java.util.List;

public class ViewNotificationsControllerApplicativo {

    public List<MessageBean> messages(String ruolo, String codiceFiscale) throws BrondiException {

        List<MessageBean> result = new ArrayList<>();

        LayerPersistenza layer = FactoryLayerPersistenza.createLayerPersistenza();
        try {
            List<Notification> notifications = layer.getMessagesRAM();
            for (Notification n : notifications) {
                boolean include = false;

                if ("TRAVELER".equalsIgnoreCase(ruolo)) {
                    include = "APPROVED".equalsIgnoreCase(n.getStatus()) && n.getRecipientCf() == null;
                }

                if (include) {
                    MessageBean bean = new MessageBean(n.getMessage(), n.getDate());
                    bean.setRisolto(n.isRisolto());
                    bean.setApprovato(n.isApprovato());
                    bean.setLetto(n.isLetto());
                    bean.setStatus(n.getStatus());
                    bean.setSenderRole(n.getSenderRole());
                    bean.setSenderCf(n.getSenderCf());
                    bean.setRecipientCf(n.getRecipientCf());
                    bean.setCity(n.getCity());
                    bean.setPickpocketAlert(n.isPickpocketAlert());
                    bean.setFightAlert(n.isFightAlert());
                    bean.setCrowdAlert(n.isCrowdAlert());
                    bean.setGeneralAlert(n.isGeneralAlert());
                    bean.setStationName(n.getStationName());
                    bean.setSuspectClothing(n.getSuspectClothing());
                    result.add(bean);
                }
            }

            return result;

        } catch (DAOExceptionRemoli e) {
            throw new BrondiException(
                    "Errore nel recupero delle notifiche",
                    "BRONDI_020",
                    "ViewNotificationsControllerApplicativo.messages",
                    e
            );
        }
    }
}
