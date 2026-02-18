package com.whitestone.hrms.controller;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class CorsFilter implements Filter {

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) 
            throws IOException, ServletException {
        
        HttpServletResponse response = (HttpServletResponse) res;
        HttpServletRequest request = (HttpServletRequest) req;
        
        // ALLOW ALL ORIGINS
        response.setHeader("Access-Control-Allow-Origin", "*");
        
        // CRITICAL: ALLOW ALL HEADERS using wildcard (*)
        response.setHeader("Access-Control-Allow-Headers", "*");
        
        // ALLOW ALL METHODS
        response.setHeader("Access-Control-Allow-Methods", "*");
        
        // MAX AGE
        response.setHeader("Access-Control-Max-Age", "3600");
        
        // ALLOW CREDENTIALS (set to false when using *)
        response.setHeader("Access-Control-Allow-Credentials", "false");
        
        // Handle OPTIONS preflight request
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            System.out.println("CORS: Handling OPTIONS preflight for: " + request.getRequestURI());
            System.out.println("Origin: " + request.getHeader("Origin"));
            System.out.println("Request Headers: " + request.getHeader("Access-Control-Request-Headers"));
            response.setStatus(HttpServletResponse.SC_OK);
            return;
        }
        
        chain.doFilter(req, res);
    }

    @Override
    public void init(FilterConfig filterConfig) {
        System.out.println("CORS Filter initialized - ALLOWING ALL HEADERS");
    }

    @Override
    public void destroy() {
    }
}