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
 * A user with unique Id, who we can potentially track with cookies.
 * 
 * @author swilliams
 */
public class User {
    
    /** Unique id for the user */
    private final UUID id;
    
    /**
     * Constructor.
     * 
     * @param id the user id for this object
     */
    public User(UUID id) {
        this.id = id;
    }
    
    /** 
     * @return the user objects id
     */
    public UUID getId() {
        return id;
    }
    
    /**
     * Return a User object using the FB_ID cookie if one is present. Return 
     * null if there is not FB_ID cookie.
     * 
     * @param request the HttpServletRequest to check for cookie
     * @return the User object or null if no cookie is found
     * @throws Exception if unable to complete the operation
     */
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
     * @param cookieValue the string representation of the cookie value
     * @return the User object associated with the cookie user Id
     * @throws Exception if unable to complete the operation
     */
    private static User getUser(String cookieValue) throws Exception {
        try {
            return new User(UUID.fromString(cookieValue));
        } catch (Exception e) {
            // ignore it, we'll throw one anyway
        }
        throw new Exception("Unable to read/parse the user cookie");
    }
    
    /**
     * Drop a FB_ID cookie on the browser.
     * 
     * @param response the HttpServletResponse to which the cookie is added
     * @throws UnsupportedEncodingException if unable to encode the cookie value
     */
    public void writeTo(HttpServletResponse response) throws UnsupportedEncodingException {
        String cookieValue = id.toString();
        
        DateTime currentTime = new DateTime();
        DateTime expiryTime = currentTime.plus(180);
        int expiryTimeInSeconds = Seconds.secondsBetween(currentTime, expiryTime).getSeconds();
        
        Cookie newCookie = new Cookie("FB_ID", URLEncoder.encode(cookieValue, "UTF-8"));
        newCookie.setPath("/fireblade-server");
        
        response.addCookie(newCookie);
    }

}
