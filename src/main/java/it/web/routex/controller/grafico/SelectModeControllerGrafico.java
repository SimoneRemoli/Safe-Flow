package it.web.routex.controller.grafico;

import it.web.routex.bean.ApplicationModeBean;
import it.web.routex.controller.applicativo.SelectModeControllerApplicativo;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/selectMode")
public class SelectModeControllerGrafico extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {

        String modeParam = request.getParameter("mode");

        ApplicationModeBean bean = new ApplicationModeBean();
        bean.setMode(modeParam);

        SelectModeControllerApplicativo a =  new SelectModeControllerApplicativo();
        a.selectMode(bean);

        try{
            response.sendRedirect("index.jsp");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
