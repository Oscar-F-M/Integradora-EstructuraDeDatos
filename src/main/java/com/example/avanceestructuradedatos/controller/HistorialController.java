package com.example.avanceestructuradedatos.controller;

import com.example.avanceestructuradedatos.model.Historial;
import com.example.avanceestructuradedatos.service.HistorialService;
import com.example.avanceestructuradedatos.service.PacienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/integradora/historial")
@CrossOrigin(origins = "*")
public class HistorialController {

    @Autowired
    private HistorialService historialService;

    @Autowired
    private PacienteService pacienteService;

    // ==========================
    //   AGREGAR REGISTRO
    // ==========================
    @PostMapping
    public ResponseEntity<Map<String, Object>> agregarHistorial(
            @RequestBody Historial registro) {

        Map<String, Object> respuesta = new HashMap<>();

        // El Historial tiene pacienteId como String y se convierte a int para validar
        Integer pacienteId = Integer.parseInt(registro.getPacienteId());
        Map<String, Object> respPaciente = pacienteService.getPacienteById(pacienteId);
        int code = (int) respPaciente.getOrDefault("code", 200);

        if (code == 404) {
            respuesta.put("mensaje", "El paciente no existe en el sistema");
            return ResponseEntity.badRequest().body(respuesta);
        }

        historialService.agregarRegistro(registro);
        respuesta.put("mensaje", "Registro agregado al historial clínico");
        return ResponseEntity.ok(respuesta);
    }

    // ==========================
    //   HISTORIAL COMPLETO
    // ==========================
    @GetMapping("/{pacienteId}")
    public ResponseEntity<Historial[]> obtenerHistorial(@PathVariable String pacienteId) {
        Historial[] historial = historialService.obtenerHistorialCompleto(pacienteId);
        return ResponseEntity.ok(historial);
    }

    // ==========================
    //   ÚLTIMO REGISTRO (CIMA)
    // ==========================
    @GetMapping("/{pacienteId}/ultimo")
    public ResponseEntity<Historial> verUltimo(@PathVariable String pacienteId) {
        Historial ultimo = historialService.verUltimoRegistro(pacienteId);
        if (ultimo == null) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(ultimo);
    }

    // ==========================
    //   ÚLTIMOS N REGISTROS
    // ==========================
    @GetMapping("/{pacienteId}/ultimos/{cantidad}")
    public ResponseEntity<Historial[]> obtenerUltimos(
            @PathVariable String pacienteId,
            @PathVariable int cantidad) {

        Historial[] registros =
                historialService.obtenerUltimosRegistros(pacienteId, cantidad);
        return ResponseEntity.ok(registros);
    }

    // ==========================
    //   ESTADÍSTICAS DE HISTORIAL
    // ==========================
    @GetMapping("/{pacienteId}/estadisticas")
    public ResponseEntity<Map<String, Object>> estadisticas(@PathVariable String pacienteId) {

        Map<String, Object> stats = new HashMap<>();
        stats.put("totalRegistros", historialService.contarRegistros(pacienteId));
        stats.put("estructura", "Pila (Stack - LIFO)");
        stats.put("complejidadApilar", "O(1)");
        stats.put("complejidadDesapilar", "O(1)");

        Historial ultimo = historialService.verUltimoRegistro(pacienteId);
        if (ultimo != null) {
            stats.put("ultimoRegistro", Map.of(
                    "fecha", ultimo.getFecha(),
                    "medico", ultimo.getNombreMedico(),
                    "diagnostico", ultimo.getDiagnostico()
            ));
        }
        return ResponseEntity.ok(stats);
    }
}
