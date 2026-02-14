package com.filae.api.infrastructure.logging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utility class for consistent logging across the application
 */
public class LogHelper {

    /**
     * Log method entry with parameters
     */
    public static void logMethodEntry(Logger logger, String methodName, Object... params) {
        if (logger.isDebugEnabled()) {
            StringBuilder sb = new StringBuilder();
            sb.append("→ Entering ").append(methodName).append("(");

            if (params != null && params.length > 0) {
                for (int i = 0; i < params.length; i++) {
                    if (i > 0) sb.append(", ");
                    sb.append(params[i]);
                }
            }
            sb.append(")");

            logger.debug(sb.toString());
        }
    }

    /**
     * Log method exit with result
     */
    public static void logMethodExit(Logger logger, String methodName, Object result) {
        if (logger.isDebugEnabled()) {
            logger.debug("← Exiting {} - Result: {}", methodName, result);
        }
    }

    /**
     * Log method exit without result
     */
    public static void logMethodExit(Logger logger, String methodName) {
        if (logger.isDebugEnabled()) {
            logger.debug("← Exiting {}", methodName);
        }
    }

    /**
     * Log business operation
     */
    public static void logOperation(Logger logger, String operation, Object... details) {
        if (logger.isInfoEnabled()) {
            StringBuilder sb = new StringBuilder();
            sb.append("▶ ").append(operation);

            if (details != null && details.length > 0) {
                sb.append(" - ");
                for (int i = 0; i < details.length; i++) {
                    if (i > 0) sb.append(", ");
                    sb.append(details[i]);
                }
            }

            logger.info(sb.toString());
        }
    }

    /**
     * Log error with context
     */
    public static void logError(Logger logger, String operation, Exception e, Object... context) {
        StringBuilder sb = new StringBuilder();
        sb.append("✗ Error in ").append(operation);

        if (context != null && context.length > 0) {
            sb.append(" - Context: ");
            for (int i = 0; i < context.length; i++) {
                if (i > 0) sb.append(", ");
                sb.append(context[i]);
            }
        }

        logger.error(sb.toString(), e);
    }

    /**
     * Log validation error
     */
    public static void logValidationError(Logger logger, String field, String message) {
        logger.warn("⚠ Validation failed - Field: {}, Message: {}", field, message);
    }

    /**
     * Log database operation
     */
    public static void logDatabaseOperation(Logger logger, String operation, Object entityId) {
        if (logger.isDebugEnabled()) {
            logger.debug("⚙ Database: {} - Entity ID: {}", operation, entityId);
        }
    }

    /**
     * Log security event
     */
    public static void logSecurityEvent(Logger logger, String event, String username) {
        logger.info("🔐 Security: {} - User: {}", event, username);
    }

    /**
     * Create a logger for a class
     */
    public static Logger getLogger(Class<?> clazz) {
        return LoggerFactory.getLogger(clazz);
    }
}

