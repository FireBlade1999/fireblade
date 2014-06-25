package org.fireblade.pixel.server;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Handle requests
 * 
 * @author swilliams
 */
@Controller
public class FireBladeController {

    @RequestMapping(value = {"/fb"}, method = RequestMethod.GET)
    public void handle(HttpServletRequest request, HttpServletResponse response) {
        System.out.println(request.getHeaderNames());
    }
}
