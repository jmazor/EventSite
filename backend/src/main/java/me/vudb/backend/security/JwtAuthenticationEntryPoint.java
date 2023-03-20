package me.vudb.backend.security;

import java.io.IOException;
import java.io.Serializable;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jakarta.servlet.ServletException;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint, Serializable {
    private static final long serialVersionUID = -7858869558953243875L;
    private static final Logger logger = Logger.getLogger(JwtAuthenticationEntryPoint.class.getName());
    @Override
    public void commence(jakarta.servlet.http.HttpServletRequest request, jakarta.servlet.http.HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        logger.severe("JwtAuthenticationEntryPoint triggered for request: " + request.getRequestURI());
        logger.severe("Exception message: " + authException.getMessage());

        int errorCode = 401; // Your custom error code
        String errorMessage = authException.getMessage();

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        String responseBody = String.format("{\"errorCode\": %d, \"message\": \"%s\"}", errorCode, errorMessage);
        response.getWriter().write(responseBody);
    }
}