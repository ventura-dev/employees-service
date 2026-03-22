package com.invex.employees.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.slf4j.MDC;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Collections;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * The type Request logging filter.
 */
@Slf4j
@Component
public class RequestLoggingFilter implements Filter {


    /**
     * The constant     private static final String TRACE_HEADER = "X-Trace-Id";.
     */
    private static final String TRACE_HEADER = "X-Trace-Id";

    private static final String TRACE_ID = "TRACE_ID";


    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;

        String traceId = resolveTraceId(httpRequest);
        MDC.put(TRACE_ID, traceId);

        String headers = Collections.list(httpRequest.getHeaderNames())
                .stream()
                .map(headerName -> headerName + "=" + maskSensitiveHeader(headerName, httpRequest.getHeader(headerName)))
                .collect(Collectors.joining(", "));

        log.debug("Incoming request: {} {} | Headers: {}",
                httpRequest.getMethod(),
                httpRequest.getRequestURI(),
                headers);

        chain.doFilter(request, response);
    }

    private String resolveTraceId(HttpServletRequest request) {
        String incomingTraceId = request.getHeader(TRACE_HEADER);
        return (incomingTraceId != null && !incomingTraceId.isBlank())
                ? incomingTraceId
                : UUID.randomUUID().toString();
    }

    private String maskSensitiveHeader(String headerName, String value) {
        if ("authorization".equalsIgnoreCase(headerName)
                || "cookie".equalsIgnoreCase(headerName)
                || "set-cookie".equalsIgnoreCase(headerName)) {
            return "***MASKED***";
        }
        return value;
    }
}
