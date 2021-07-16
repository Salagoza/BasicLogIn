package io.muic.ssc.webapp.servlet;

import io.muic.ssc.webapp.Routable;
import io.muic.ssc.webapp.model.User;
import io.muic.ssc.webapp.service.SecurityService;
import io.muic.ssc.webapp.service.UserService;
import org.apache.commons.lang.StringUtils;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ChangePasswordServlet extends HttpServlet implements Routable {

    private SecurityService securityService;


    @Override
    public String getMapping() {
        return "/user/password";
    }

    @Override
    public void setSecurityService(SecurityService securityService) {
        this.securityService = securityService;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        boolean authorized = securityService.isAuthorized(request);
        if(authorized){
            String username = StringUtils.trim((String) request.getParameter("username"));
            System.out.println(username);
            UserService userService = UserService.getInstance();

            User user = userService.findbyUsername(username);
            request.setAttribute("user",user);
            request.setAttribute("username",user.getUsername());

            RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/password.jsp");
            rd.include(request,response);

            request.getSession().removeAttribute("hasError");
            request.getSession().removeAttribute("message");

        }else{
            request.removeAttribute("hasError");
            request.removeAttribute("message");
            response.sendRedirect("/login");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        boolean authorized = securityService.isAuthorized(request);
        if(authorized){
            // allow only editing displayName
            // make sure that username and displayName doesn't contain leading and trailing space
            String username = StringUtils.trim((String) request.getParameter("username"));
            String password = (String) request.getParameter("password");
            String cpassword = (String) request.getParameter("cpassword");

            UserService userService = UserService.getInstance();
            User user = userService.findbyUsername(username);

            String errorMessage = null;


            if(user == null){
                errorMessage = String.format("User %s does not exist.", username);
            }

            else if(StringUtils.isBlank(password)){
                errorMessage = "Password can't be blank.";
            }

            else if(!StringUtils.equals(password,cpassword)){
                errorMessage = "Confirmed password doesn't matched.";
            }

            if(errorMessage != null){
                request.getSession().setAttribute("hasError",true);
                request.getSession().setAttribute("message",errorMessage);
            }else{
                try{
                    userService.changePassword(username,password);
                    request.getSession().setAttribute("hasError",false);
                    request.getSession().setAttribute("message",String.format("Password %s has been changed successfully.", username));
                    response.sendRedirect("/");
                    return;
                } catch (Exception e) {
                    request.getSession().setAttribute("hasError",true);
                    request.getSession().setAttribute("message",e.getMessage());
                }
            }

            request.setAttribute("username",username);

            RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/password.jsp");
            rd.include(request,response);

            request.getSession().removeAttribute("hasError");
            request.getSession().removeAttribute("message");

        }else{
            request.removeAttribute("hasError");
            request.removeAttribute("message");
            response.sendRedirect("/login");
        }
    }
}
