package com.stat.statserver.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.stat.statserver.exceptions.InvalidApiKeyException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;
import java.io.PrintWriter;

public class AuthenticationFilter extends GenericFilterBean {

    String appApiKey;

    public AuthenticationFilter(String appApiKey) {
        this.appApiKey = appApiKey;
    }

    ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        try {
            Authentication authentication = AuthenticationService.getAuthentication((HttpServletRequest) request, appApiKey);
            SecurityContextHolder.getContext().setAuthentication(authentication);

        } catch (InvalidApiKeyException exp) {
            HttpServletResponse httpResponse = (HttpServletResponse) response;
            httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            httpResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);
            PrintWriter writer = httpResponse.getWriter();

            ErrorDto invalidAuthError = new ErrorDto(exp.getErrorCode(), exp.getMessage(), HttpStatus.UNAUTHORIZED);
            writer.print(objectMapper.writeValueAsString(invalidAuthError));
            writer.flush();
            writer.close();
        }

        chain.doFilter(request, response);

    }

}