package it.web.routex.controller.applicativo;

import it.web.routex.bean.WorkerScheduleBean;
import it.web.routex.dao.LayerPersistenza;
import it.web.routex.exception.BrondiException;
import it.web.routex.exception.DAOExceptionRemoli;
import it.web.routex.model.WorkerSchedule;
import it.web.routex.utility.factory.FactoryLayerPersistenza;

public class ViewWorkScheduleControllerApplicativo {

    public WorkerScheduleBean getSchedule(String cf) throws BrondiException {

        //WorkerScheduleDAO dao = new WorkerScheduleDAO();

        try {
            //Recupero MODEL

            LayerPersistenza layer = FactoryLayerPersistenza.createLayerPersistenza();
            WorkerSchedule model = layer.getWorkerSchedule(cf);
            //WorkerSchedule model = dao.getWorkerSchedule(cf);

            //logica applicativa
            if (!model.isValid()) {
                throw new BrondiException(
                        "Orario di lavoro non valido",
                        "BRONDI_001",
                        "oraInizio >= oraFine"
                );
            }

            int durata = model.durataTurno();

            //  Creo SOLO ORA la Bean di trasporto
            return new WorkerScheduleBean(
                    model.getOraInizio(),
                    model.getOraFine(),
                    model.getLuogoDiLavoro(),
                    durata
            );

        } catch (DAOExceptionRemoli e) {
            throw new BrondiException(
                    "Il Worker non possiede orari di lavoro assegnati.",
                    "BRONDI_002",
                    e.getMessage()
            );
        }
    }
}
