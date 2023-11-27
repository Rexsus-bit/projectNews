package com.stat.statserver.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.stat.statserver.handler.StatsErrorApi;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;
import java.io.PrintWriter;

@Component
public class AuthenticationFilter extends GenericFilterBean {

    @Value("${STATS_SERVER_API_KEY}")
    private String appApiKey;

    private static final String AUTH_TOKEN_HEADER_NAME = "X-API-KEY";

    ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        String path = req.getRequestURI();
        if (path.startsWith("/stat") == false) {
            chain.doFilter(request, response);
            return;
        }

        String key = null;
        if (req.getHeader(AUTH_TOKEN_HEADER_NAME) != null){
            key = req.getHeader(AUTH_TOKEN_HEADER_NAME);
        } else if (req.getParameter(AUTH_TOKEN_HEADER_NAME) != null){
            key = req.getParameter(AUTH_TOKEN_HEADER_NAME);
        }

        if (appApiKey.equals(key)) {
            chain.doFilter(request, response);
        } else {
            String error = "Invalid API KEY";
            HttpServletResponse httpResponse = (HttpServletResponse) response;
            httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            httpResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);
            PrintWriter writer = httpResponse.getWriter();

            StatsErrorApi invalidAuthError = new StatsErrorApi(1002, error, HttpStatus.UNAUTHORIZED);
            writer.print(objectMapper.writeValueAsString(invalidAuthError));
            writer.flush();
            writer.close();

        }

    }

}