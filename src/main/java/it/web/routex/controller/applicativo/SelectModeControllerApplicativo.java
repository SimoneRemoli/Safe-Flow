package it.web.routex.controller.applicativo;

import it.web.routex.bean.ApplicationModeBean;
import it.web.routex.enumerator.ApplicationMode;
import it.web.routex.utility.singleton.ApplicationModeManager;

public class SelectModeControllerApplicativo {

    public void selectMode(ApplicationModeBean bean) {

        if (bean == null || bean.getMode() == null) {
            throw new IllegalArgumentException("Application mode not specified");
        }

        ApplicationMode mode;

        try {
            mode = ApplicationMode.valueOf(bean.getMode());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid application mode");
        }
        ApplicationModeManager.getSingletonInstance().setMode(mode);
    }
}
