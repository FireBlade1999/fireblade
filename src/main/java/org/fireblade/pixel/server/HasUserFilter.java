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
 * Verify the user cookie was successfully dropped.
 * 
 * @author swilliams
 */
public class HasUserFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // do nothing
    }

    /**
     * Read the HttpServletRequest and set the "user" attribute; or set status
     * to SC_NO_CONTENT if unable to do so.
     * 
     * @param request the HttpServletRequest to read from
     * @param response the HttpServletResponse to write to
     * @param chain the filter chain
     * @throws IOException if unable to complete IO operation
     * @throws ServletException if unable to continue the chain
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        
        User user;
        
        try {
            user = User.readFrom(httpServletRequest);
            System.out.println("hasuserfilter: user " + user);
        } catch (Exception iue) {
            httpServletResponse.setStatus(HttpServletResponse.SC_NO_CONTENT);
            return;
        }
        
        if (user == null) {
            // cookie was not dropped on browser, or is corrupted in some way.
            httpServletResponse.setStatus(HttpServletResponse.SC_NO_CONTENT);
            return;
        }
        
        // proceed with the request
        request.setAttribute("user", user);
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        // do nothing
    }

}
