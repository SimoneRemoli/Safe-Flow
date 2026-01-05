package it.web.routex.controller.applicativo;

import it.web.routex.bean.WorkerScheduleBean;
import it.web.routex.dao.WorkerScheduleDAO;
import it.web.routex.exception.BrondiException;
import it.web.routex.exception.DAOExceptionRemoli;
import it.web.routex.exception.LoginNotFoundRemoli;
import it.web.routex.model.WorkerSchedule;

public class ViewWorkScheduleControllerApplicativo {

    public WorkerScheduleBean getSchedule(String cf) throws BrondiException {

        WorkerScheduleDAO dao = new WorkerScheduleDAO();

        try {
            //Recupero MODEL
            WorkerSchedule model = dao.getWorkerSchedule(cf);

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
                    "Errore durante il recupero dell'orario di lavoro",
                    "BRONDI_002",
                    e.getMessage()
            );
        }
    }
}
