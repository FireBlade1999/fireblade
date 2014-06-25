package org.fireblade.pixel.server;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.UUID;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.joda.time.DateTime;
import org.joda.time.Seconds;

/**
 *
 * @author swilliams
 */
public class User {
    
    private final UUID id;
    
    public User(UUID id) {
        this.id = id;
    }
    
    public UUID getId() {
        return id;
    }
    
    public static User readFrom(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        
        if (cookies == null || cookies.length == 0) {
            return null;
        }
        
        for (Cookie cookie: cookies) {
            if ("FB_ID".equals(cookie.getName())) {
                // TODO - return something valuable
                return null;
            }
        }
        
        // no cookie found
        return null;
    }
    
    public void writeTo(HttpServletResponse response) throws UnsupportedEncodingException {
        String cookieValue = id.toString();
        
        DateTime currentTime = new DateTime();
        DateTime expiryTime = currentTime.plus(180);
        int expiryTimeInSeconds = Seconds.secondsBetween(currentTime, expiryTime).getSeconds();
        
        Cookie newCookie = new Cookie("FB_ID", URLEncoder.encode(cookieValue, "UTF-8"));
        newCookie.setPath("/");
        newCookie.setDomain("");
        newCookie.setMaxAge(expiryTimeInSeconds);
        
        response.addCookie(newCookie);
    }

}
