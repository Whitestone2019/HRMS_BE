package com.whitestone.hrms.controller;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;

@Component
public class ApiKeyFilter implements Filter {
    private static final String API_KEY = "Wh!te$t@Ne";
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        if ("OPTIONS".equalsIgnoreCase(httpRequest.getMethod())) {
            chain.doFilter(request, response);
            return;
        }
        System.out.println("KEY>>>>>>>" + httpRequest.getHeader("apiKey"));
        String apiKey = httpRequest.getHeader("apiKey");
        if (apiKey == null || !API_KEY.equals(apiKey)) {
            httpResponse.sendError(HttpServletResponse.SC_FORBIDDEN, "Invalid API Key");
            return;
        }
        chain.doFilter(request, response);
    }
}