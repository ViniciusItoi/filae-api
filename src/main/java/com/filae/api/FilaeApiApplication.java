package com.filae.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Main Spring Boot Application entry point for Filae API
 * Virtual Queue Management System
 */
@SpringBootApplication
public class FilaeApiApplication {

    private static final Logger log = LoggerFactory.getLogger(FilaeApiApplication.class);

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(FilaeApiApplication.class);
        Environment env = app.run(args).getEnvironment();

        logApplicationStartup(env);
    }

    private static void logApplicationStartup(Environment env) {
        String protocol = "http";
        String serverPort = env.getProperty("server.port", "8080");
        String contextPath = env.getProperty("server.servlet.context-path", "");
        String hostAddress = "localhost";

        try {
            hostAddress = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            log.warn("Unable to determine host address", e);
        }

        log.info("""
            
            ----------------------------------------------------------
            🚀 Application '{}' is running!
            ----------------------------------------------------------
            ✓ Local:      {}://localhost:{}{}
            ✓ External:   {}://{}:{}{}
            ✓ Profile(s): {}
            ✓ Swagger:    {}://localhost:{}{}/swagger-ui.html
            ----------------------------------------------------------
            """,
            env.getProperty("spring.application.name"),
            protocol, serverPort, contextPath,
            protocol, hostAddress, serverPort, contextPath,
            env.getActiveProfiles().length == 0 ? env.getDefaultProfiles() : env.getActiveProfiles(),
            protocol, serverPort, contextPath
        );
    }
}

