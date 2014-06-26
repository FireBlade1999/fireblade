package org.fireblade.pixel.server;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Add a user cookie to the response if none is present in the request.
 * 
 * @author swilliams
 */
public class NewUserFilter implements Filter {

    private final NewUserGenerator userGen;
    
    public NewUserFilter(NewUserGenerator userGen) {
        this.userGen = userGen;
    }
    
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // do nothing
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        
        User user;
        
        /*
            if this is a previously cookied user then proceed with the request,
            otherwise generate a user Id and drop a cookie, then redirect to a
            cookie check to verify it was successfully dropped. When doing the
            redirect, the request is stopped, in order to force the browser to
            perform the 302. 
        */
        try {
            user = User.readFrom(httpServletRequest);
        } catch (Exception iue) {
            httpServletResponse.setStatus(HttpServletResponse.SC_NO_CONTENT);
            return;
        }
        
        // new user
        if (user == null) {
            userGen.generate().writeTo(httpServletResponse);
            httpServletResponse.addHeader("Location", "/fireblade-server/cc");
            httpServletResponse.setStatus(HttpServletResponse.SC_MOVED_TEMPORARILY);
            
            // stop the request going down the chain when doing a redirect
            return;
        }
        
        // proceed with request
        httpServletRequest.setAttribute("user", user);
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        // do nothing
    }

}
