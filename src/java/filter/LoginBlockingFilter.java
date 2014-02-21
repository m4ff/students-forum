/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package filter;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author paolo
 */
public class LoginBlockingFilter extends HttpFilter{

    @Override
    public void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        if(request.getAttribute("user") == null) {
            request.setAttribute("redirect", "1");
            request.setAttribute("redirect_url", request.getRequestURI() + request.getQueryString());
            request.getRequestDispatcher("/login").forward(request, response);
        } else {
            chain.doFilter(request, response);
        }
    }
    
}
