package it.web.routex.controller.grafico;


import it.web.routex.bean.PathInfoBean;
import it.web.routex.exception.DAOExceptionRemoli;
import it.web.routex.model.dao.PathInfoDAO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/PathInfoRAS")
public class ReportsControllerGrafico extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        //da aggiustare chiamando l'applicativo e poi dao
        PathInfoDAO dao = new PathInfoDAO();
        List<PathInfoBean> pathList = null;
        try {
            pathList = dao.getAllPathInfo();
        } catch (DAOExceptionRemoli e) {
            throw new RuntimeException(e);
        }

        request.setAttribute("pathList", pathList);
        request.getRequestDispatcher("viewRAS.jsp").forward(request, response);
    }
}