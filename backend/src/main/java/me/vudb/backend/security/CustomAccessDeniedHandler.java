package me.vudb.backend.security;

import jakarta.servlet.ServletException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(jakarta.servlet.http.HttpServletRequest request, jakarta.servlet.http.HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException {
        response.setStatus(HttpStatus.FORBIDDEN.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        // Here you can customize the response for access denied exceptions
        String responseBody = "{\"message\":\"" + accessDeniedException.getMessage() + "\",\"error\":\"Access Denied\"}";
        response.getWriter().write(responseBody);
    }

}
