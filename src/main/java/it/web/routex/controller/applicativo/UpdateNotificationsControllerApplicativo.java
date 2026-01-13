package it.web.routex.controller.applicativo;

import it.web.routex.bean.MessageBean;
import it.web.routex.dao.LayerPersistenza;
import it.web.routex.exception.DAOExceptionRemoli;
import it.web.routex.model.Notification;
import it.web.routex.utility.factory.FactoryLayerPersistenza;
import it.web.routex.utility.observer.Notifier;

import java.util.List;

public class UpdateNotificationsControllerApplicativo {

    public void aggiornaStatoNotifica(MessageBean bean) throws DAOExceptionRemoli {

        LayerPersistenza layer = FactoryLayerPersistenza.createLayerPersistenza();

        List<Notification> cached = layer.getMessagesRAM();

        for (Notification n : cached) {
            if (n.getMessage().equals(bean.getMessage())
                    && n.getDate().equals(bean.getDate())) {

                n.setRisolto(true);
                layer.solvedNotification(n);
                Notifier.getInstanceSingleton().comunicazioneRisolta();
                return;
            }
        }
        throw new DAOExceptionRemoli("Notifica non trovata in cache");
    }
}
