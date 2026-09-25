package com.isera.assetmanagement.security.handler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import tools.jackson.databind.json.JsonMapper;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

public class RestAuthenticationEntryPoint
        implements AuthenticationEntryPoint {

    private final JsonMapper jsonMapper;

    public RestAuthenticationEntryPoint(JsonMapper jsonMapper) {
        this.jsonMapper = jsonMapper;
    }

    @Override
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException authException
    ) throws IOException {

        response.setStatus(
                HttpServletResponse.SC_UNAUTHORIZED
        );

        response.setContentType(
                MediaType.APPLICATION_JSON_VALUE
        );

        Map<String, Object> body =
                new LinkedHashMap<>();

        body.put(
                "timestamp",
                LocalDateTime.now()
        );

        body.put(
                "status",
                401
        );

        body.put(
                "error",
                "Unauthorized"
        );

        body.put(
                "message",
                "Authentication is required"
        );

        body.put(
                "path",
                request.getRequestURI()
        );

        response.getWriter().write(
                jsonMapper.writeValueAsString(body)
        );
    }
}