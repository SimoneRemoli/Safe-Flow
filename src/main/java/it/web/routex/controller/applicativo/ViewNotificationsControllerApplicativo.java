package it.web.routex.controller.applicativo;


import it.web.routex.bean.MessageBean;
import it.web.routex.exception.DAOExceptionRemoli;
import it.web.routex.exception.PathNotFoundExceptionRemoli;
import it.web.routex.dao.GetCommunicationsDAO;
import java.util.List;

public class ViewNotificationsControllerApplicativo
{
    public List<MessageBean> messages() throws DAOExceptionRemoli, PathNotFoundExceptionRemoli {

        GetCommunicationsDAO dao = new GetCommunicationsDAO();
        return dao.getMessages();

    }
}