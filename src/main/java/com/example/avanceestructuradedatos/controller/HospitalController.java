package com.example.avanceestructuradedatos.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/integradora")
public class HospitalController {

    @GetMapping("/")
    public ResponseEntity<Map<String, String>> info() {
        Map<String, String> info = new HashMap<>();
        info.put("nombre", "Sistema de Gestión Hospitalaria");
        info.put("version", "1.0.0");
        info.put("estructuras", "Lista, Cola, Pila, Árbol");
        return ResponseEntity.ok(info);
    }
}
