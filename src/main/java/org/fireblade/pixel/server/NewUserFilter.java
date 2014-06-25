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

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // do nothing
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        
        User user;
        
        try {
            user = User.readFrom(httpServletRequest);
        } catch (Exception iue) {
            httpServletResponse.setStatus(HttpServletResponse.SC_NO_CONTENT);
            return;
        }
        
        // new user
        if (user == null) {
            // TODO - act on new user
        }
        
        // proceed with request
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        // do nothing
    }

}
