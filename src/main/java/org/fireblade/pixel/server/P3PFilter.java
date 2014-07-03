package org.fireblade.pixel.server;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

/**
 * Add the P3P header to all responses.
 * 
 * @author swilliams
 */
public class P3PFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // do nothing
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletResponse httpServletResponse;
        httpServletResponse = (HttpServletResponse) response;
        
        httpServletResponse.setHeader("P3P", "CP=\"NON DSP CURa ADMa DEVa TAIa PSAa PSDa OUR IND UNI COM NAV INT DEM CNT STA PRE LOC\"");
        
        chain.doFilter(request, response);
        System.out.println("p3pfilter");
    }

    @Override
    public void destroy() {
        // do nothing
    }

}
