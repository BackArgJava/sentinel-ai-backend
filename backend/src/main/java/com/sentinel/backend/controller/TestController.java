package com.sentinel.backend.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;

@RestController
public class TestController {

    @GetMapping("/users")
    public Map<String, String> testEndpoint() {
        // Map.of() convierte esto automáticamente en un formato JSON perfecto
        return Map.of(
                "estado", "Exitoso",
                "mensaje", "¡Hola Santi! Sentinel AI está conectado y funcionando perfectamente en Docker. 🚀"
        );
    }
}