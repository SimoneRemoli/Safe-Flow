package it.web.routex.controller.applicativo;


import it.web.routex.bean.WorkerScheduleBean;
import it.web.routex.exception.DAOExceptionRemoli;
import it.web.routex.exception.LoginNotFoundRemoli;
import it.web.routex.dao.WorkerScheduleDAO;

public class ViewWorkScheduleControllerApplicativo {

    public WorkerScheduleBean getSchedule(String cf) {
        WorkerScheduleDAO dao = new WorkerScheduleDAO();
        try {
            return dao.getWorkerSchedule(cf);
        } catch (DAOExceptionRemoli | LoginNotFoundRemoli daoExceptionRemoli) {
            throw new RuntimeException(daoExceptionRemoli);
        }
    }
}