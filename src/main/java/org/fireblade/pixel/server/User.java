package org.fireblade.pixel.server;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
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
    
    public static User readFrom(HttpServletRequest request) throws Exception {
        Cookie[] cookies = request.getCookies();
        
        if (cookies == null || cookies.length == 0) {
            return null;
        }
        
        for (Cookie cookie: cookies) {
            if ("FB_ID".equals(cookie.getName())) {
                return getUser(URLDecoder.decode(cookie.getValue(), "UTF-8"));
            }
        }
        
        // no cookie found
        return null;
    }
    
    /**
     * Take the string value of the cookie and return a User
     * 
     * @param cookieValue
     * @return
     * @throws Exception 
     */
    private static User getUser(String cookieValue) throws Exception {
        try {
            return new User(UUID.fromString(cookieValue));
        } catch (Exception e) {
            // ignore it, we'll throw one anyway
        }
        throw new Exception("Unable to read/parse the user cookie");
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
