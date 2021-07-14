package io.muic.ssc.webapp.service;

import io.muic.ssc.webapp.model.User;
import org.apache.commons.lang.StringUtils;
import org.mindrot.jbcrypt.BCrypt;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

public class SecurityService {

    private UserService userService;

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public boolean isAuthorized(HttpServletRequest request) {
        String username = (String) request.getSession().getAttribute("username");
        User user = userService.findbyUsername(username);
        return (username != null && userService.findbyUsername(username) != null);
    }

    public boolean authenticate(String username, String password, HttpServletRequest request){
        User user = userService.findbyUsername(username);
        if (user != null && BCrypt.checkpw(password,user.getPassword())){
            request.getSession().setAttribute("username",username);
            return true;
        }else{
            return false;
        }
    }

    public void logout(HttpServletRequest request){ request.getSession().invalidate();}
}
