package com.filae.api.infrastructure.logging;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

/**
 * HTTP Request/Response logging interceptor
 * Logs all incoming requests and outgoing responses with timing information
 */
@Component
public class LoggingInterceptor implements HandlerInterceptor {

    private static final Logger log = LoggerFactory.getLogger(LoggingInterceptor.class);
    private static final String START_TIME_ATTRIBUTE = "startTime";
    private static final String REQUEST_ID_ATTRIBUTE = "requestId";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String requestId = UUID.randomUUID().toString().substring(0, 8);
        Instant startTime = Instant.now();

        request.setAttribute(START_TIME_ATTRIBUTE, startTime);
        request.setAttribute(REQUEST_ID_ATTRIBUTE, requestId);

        String method = request.getMethod();
        String uri = request.getRequestURI();
        String queryString = request.getQueryString();
        String remoteAddr = getClientIpAddress(request);

        log.info("┌─ [{}] {} {} {} - Client: {}",
            requestId,
            method,
            uri,
            queryString != null ? "?" + queryString : "",
            remoteAddr
        );

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response,
                          Object handler, ModelAndView modelAndView) {
        // This method is called after the controller method but before the view is rendered
        // Not used in REST APIs typically
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
                               Object handler, Exception ex) {
        Instant startTime = (Instant) request.getAttribute(START_TIME_ATTRIBUTE);
        String requestId = (String) request.getAttribute(REQUEST_ID_ATTRIBUTE);

        if (startTime != null) {
            Duration duration = Duration.between(startTime, Instant.now());
            int status = response.getStatus();
            String method = request.getMethod();
            String uri = request.getRequestURI();

            String statusEmoji = getStatusEmoji(status);
            String level = getLogLevel(status);

            String logMessage = String.format("└─ [%s] %s %s - Status: %d %s - Duration: %dms",
                requestId,
                method,
                uri,
                status,
                statusEmoji,
                duration.toMillis()
            );

            // Log with appropriate level based on status
            switch (level) {
                case "ERROR" -> log.error(logMessage);
                case "WARN" -> log.warn(logMessage);
                default -> log.info(logMessage);
            }

            if (ex != null) {
                log.error("└─ [{}] Exception occurred: {}", requestId, ex.getMessage(), ex);
            }
        }
    }

    private String getClientIpAddress(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
            return xForwardedFor.split(",")[0].trim();
        }

        String xRealIp = request.getHeader("X-Real-IP");
        if (xRealIp != null && !xRealIp.isEmpty()) {
            return xRealIp;
        }

        return request.getRemoteAddr();
    }

    private String getStatusEmoji(int status) {
        if (status >= 200 && status < 300) return "✓";
        if (status >= 300 && status < 400) return "➜";
        if (status >= 400 && status < 500) return "⚠";
        if (status >= 500) return "✗";
        return "?";
    }

    private String getLogLevel(int status) {
        if (status >= 500) return "ERROR";
        if (status >= 400) return "WARN";
        return "INFO";
    }
}

