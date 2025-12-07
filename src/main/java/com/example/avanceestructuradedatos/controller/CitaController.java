package com.example.avanceestructuradedatos.controller;

import com.example.avanceestructuradedatos.dtos.CreateCitaDTO;
import com.example.avanceestructuradedatos.service.CitaService;
import com.example.avanceestructuradedatos.service.PacienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@RestController
@RequestMapping("/api/integradora/citas")
@CrossOrigin(origins = "*")
public class CitaController {

    @Autowired
    private CitaService citaService;

    @Autowired
    private PacienteService pacienteService;

    @PostMapping("/agregar")
    public ResponseEntity<Map<String, Object>> agregarCita(
            @RequestBody CreateCitaDTO cita) {

        Map<String, Object> respuesta = new HashMap<>();

        Map<String, Object> respPaciente =
                pacienteService.getPacienteById(cita.getPacienteId());

        int code = (int) respPaciente.getOrDefault("code", 200);
        if (code == 404) {
            respuesta.put("mensaje", "El paciente no existe");
            return ResponseEntity.badRequest().body(respuesta);
        }

        if (cita.getId() == 0) {
            cita.setId(new Random().nextInt(100000));
        }

        citaService.agregarACola(cita);
        respuesta.put("mensaje", "Cita agregada correctamente");

        return ResponseEntity.ok(respuesta);
    }

    @PostMapping("/atender")
    public ResponseEntity<CreateCitaDTO> atenderSiguiente() {
        CreateCitaDTO cita = citaService.atenderSiguiente();
        if (cita == null) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(cita);
    }

    @GetMapping("/siguiente")
    public ResponseEntity<CreateCitaDTO> verSiguiente() {
        CreateCitaDTO cita = citaService.verSiguiente();
        if (cita == null) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(cita);
    }

    @GetMapping
    public ResponseEntity<CreateCitaDTO[]> obtenerCola() {
        return ResponseEntity.ok(citaService.obtenerTodasEnOrden());
    }

    @GetMapping("/estadisticas")
    public ResponseEntity<Map<String, Object>> estadisticas() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("total", citaService.tamaño());
        stats.put("estructura", "Cola (FIFO)");
        stats.put("vacia", citaService.estaVacia());
        return ResponseEntity.ok(stats);
    }
}
