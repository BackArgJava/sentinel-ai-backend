package com.sentinel.backend.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    // This creates a professional logger to record errors in the terminal
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleAllExceptions(Exception ex) {
        // 1. Log the full error on the server side so YOU can fix it
        logger.error("⚠️ SENTINEL CRITICAL ERROR DETECTED: ", ex);

        // 2. Return a clean, "Mainframe" themed message to the User
        // We use a Map to send a nice JSON object back to React
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "CRITICAL: Neural link instability detected. Check system logs, Agent."));
    }
}