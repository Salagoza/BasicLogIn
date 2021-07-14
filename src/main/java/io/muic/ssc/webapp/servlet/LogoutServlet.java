package io.muic.ssc.webapp.servlet;

import io.muic.ssc.webapp.Routable;
import io.muic.ssc.webapp.service.SecurityService;
import org.apache.commons.lang.StringUtils;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class LogoutServlet extends HttpServlet implements Routable {

    private SecurityService securityService;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        securityService.logout(request);
        RequestDispatcher rd = request.getRequestDispatcher("WEB-INF/login.jsp");
        rd.include(request,response);
    }

    @Override
    public String getMapping() {
        return "/logout";
    }

    @Override
    public void setSecurityService(SecurityService securityService) {
        this.securityService = securityService;

    }
}
