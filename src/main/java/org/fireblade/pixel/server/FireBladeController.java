package org.fireblade.pixel.server;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Handle all requests to "/fb" and log a page view if able.
 * 
 * @author swilliams
 */
@Controller
public class FireBladeController {

    /**
     * Get the User object from the request and log a page view.
     * 
     * @param request the HttpServletRequest to read from
     * @param response the HttpServletReponse to write to
     */
    @RequestMapping(value = {"/fb"}, method = RequestMethod.GET)
    public void handle(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("controller");
        User user = (User) request.getAttribute("user");
        
        logPageView(request, user);
    }
    
    /**
     * Get the header information from the request and log the page view.
     * 
     * @param request the HttpServletRequest to read from
     * @param user the User object associated with this request
     */
    private void logPageView(HttpServletRequest request, User user) {
        System.out.println("controller: logPageView");
        String userAgent = request.getHeader("User-Agent");
        String language = request.getLocale().getLanguage();
        String rawUrl = request.getHeader("Referer");
        
        Logger logger = Logger.getLogger("FireBladeController.java");
        FileHandler fileHandler;
        
        try {
            fileHandler = new FileHandler("classpath:fireblade-server.log");
            logger.addHandler(fileHandler);
            logger.setLevel(Level.INFO);
            SimpleFormatter formatter = new SimpleFormatter();
            fileHandler.setFormatter(formatter);
            logger.log(Level.INFO, "User: {0} rawUrl: {1}", new Object[]{user.getId(), rawUrl});
        } catch (Exception e) {
            // TODO - handle
            e.printStackTrace();
        }
    }
}
