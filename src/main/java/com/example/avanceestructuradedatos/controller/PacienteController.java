package com.example.avanceestructuradedatos.controller;

import com.example.avanceestructuradedatos.dtos.CreatePacienteDTO;
import com.example.avanceestructuradedatos.service.PacienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/integradora/pacientes")
@CrossOrigin(origins = "*")
public class PacienteController {

    @Autowired
    private PacienteService pacienteService;

    @PostMapping
    public ResponseEntity<Map<String, Object>> crearPaciente(
            @RequestBody CreatePacienteDTO dto) {
        return ResponseEntity.ok(pacienteService.createPaciente(dto));
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> obtenerPacientes() {
        return ResponseEntity.ok(pacienteService.getAllPacientes());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> obtenerPaciente(@PathVariable Integer id) {
        Map<String, Object> resp = pacienteService.getPacienteById(id);
        int status = (int) resp.getOrDefault("code", 200);
        return ResponseEntity.status(status).body(resp);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> actualizarPaciente(
            @PathVariable Integer id,
            @RequestBody CreatePacienteDTO dto) {
        Map<String, Object> resp = pacienteService.actualizarPaciente(id, dto);
        int status = (int) resp.getOrDefault("code", 200);
        return ResponseEntity.status(status).body(resp);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> eliminarPaciente(@PathVariable Integer id) {
        Map<String, Object> resp = pacienteService.eliminarPaciente(id);
        int status = (int) resp.getOrDefault("code", 200);
        return ResponseEntity.status(status).body(resp);
    }

    @GetMapping("/estadisticas")
    public ResponseEntity<Map<String, Object>> estadisticas() {
        return ResponseEntity.ok(pacienteService.getEstadisticas());
    }
}
