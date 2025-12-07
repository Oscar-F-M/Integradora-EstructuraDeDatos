package com.example.avanceestructuradedatos.service;

import com.example.avanceestructuradedatos.dtos.CreatePacienteDTO;
import com.example.avanceestructuradedatos.model.Paciente;
import com.example.avanceestructuradedatos.repository.PacienteRepository;
import com.example.avanceestructuradedatos.structures.Cola;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class PacienteService {

    private final PacienteRepository pacienteRepository;

    // Estructura de datos propia (cola) para almacenar pacientes también en memoria
    private final Cola<Paciente> colaPacientes = new Cola<>();

    // Inyección por constructor (como en PersonaService)
    public PacienteService(PacienteRepository pacienteRepository) {
        this.pacienteRepository = pacienteRepository;
    }

    // ==========================
    //      CREAR PACIENTE
    // ==========================
    public Map<String, Object> createPaciente(CreatePacienteDTO dto) {
        Map<String, Object> respuesta = new HashMap<>();

        // Mapear DTO -> Entidad Paciente
        Paciente paciente = new Paciente();
        paciente.setNombre(dto.getNombre());
        paciente.setApellido(dto.getApellido());
        paciente.setCedula(dto.getCedula());
        paciente.setFechaNacimiento(dto.getFechaNacimiento());
        paciente.setTelefono(dto.getTelefono());
        paciente.setEmail(dto.getEmail());
        paciente.setDireccion(dto.getDireccion());
        paciente.setTipoSangre(dto.getTipoSangre());

        // Guardar en BD (Spring genera el ID)
        Paciente guardado = pacienteRepository.save(paciente);

        // También lo metemos en la cola para usar la estructura de datos
        colaPacientes.insertarElemento(guardado);

        respuesta.put("mensaje", "Paciente creado");
        respuesta.put("paciente", guardado);
        return respuesta;
    }

    // ==========================
    //   OBTENER TODOS
    // ==========================
    public Map<String, Object> getAllPacientes() {
        Map<String, Object> mapResponse = new HashMap<>();

        List<Paciente> listaPacientes = pacienteRepository.findAll();

        mapResponse.put("listaPacientes", listaPacientes);
        mapResponse.put("mensaje", "Resultado exitoso.");
        return mapResponse;
    }

    // ==========================
    //   OBTENER POR ID
    // ==========================
    public Map<String, Object> getPacienteById(Integer id) {
        Map<String, Object> mapResponse = new HashMap<>();

        Optional<Paciente> optPaciente = pacienteRepository.findById(id);
        if (optPaciente.isPresent()) {
            mapResponse.put("paciente", optPaciente.get());
            mapResponse.put("mensaje", "Paciente encontrado");
            mapResponse.put("code", 200);
        } else {
            mapResponse.put("mensaje", "Paciente no encontrado");
            mapResponse.put("code", 404);
        }
        return mapResponse;
    }

    // ==========================
    //   ACTUALIZAR PACIENTE
    // ==========================
    public Map<String, Object> actualizarPaciente(Integer id, CreatePacienteDTO dto) {
        Map<String, Object> mapResponse = new HashMap<>();

        Optional<Paciente> optPaciente = pacienteRepository.findById(id);
        if (optPaciente.isPresent()) {
            Paciente pacienteExistente = optPaciente.get();

            // Actualizar campos desde el DTO
            pacienteExistente.setNombre(dto.getNombre());
            pacienteExistente.setApellido(dto.getApellido());
            pacienteExistente.setCedula(dto.getCedula());
            pacienteExistente.setFechaNacimiento(dto.getFechaNacimiento());
            pacienteExistente.setTelefono(dto.getTelefono());
            pacienteExistente.setEmail(dto.getEmail());
            pacienteExistente.setDireccion(dto.getDireccion());
            pacienteExistente.setTipoSangre(dto.getTipoSangre());

            pacienteRepository.save(pacienteExistente);

            mapResponse.put("mensaje", "Paciente actualizado");
            mapResponse.put("paciente", pacienteExistente);
            mapResponse.put("code", 202);

            // Opcional: actualizar también en la cola (simple versión)
            actualizarEnCola(pacienteExistente);

        } else {
            mapResponse.put("mensaje", "Paciente no encontrado");
            mapResponse.put("code", 404);
        }

        return mapResponse;
    }

    // ==========================
    //   ELIMINAR PACIENTE
    // ==========================
    public Map<String, Object> eliminarPaciente(Integer id) {
        Map<String, Object> mapResponse = new HashMap<>();

        Optional<Paciente> optPaciente = pacienteRepository.findById(id);
        if (optPaciente.isPresent()) {
            Paciente pacienteExistente = optPaciente.get();

            // Eliminar de BD
            pacienteRepository.delete(pacienteExistente);

            // Eliminar también de la cola
            eliminarDeCola(id);

            mapResponse.put("mensaje", "Paciente eliminado");
            mapResponse.put("paciente", pacienteExistente);
            mapResponse.put("code", 202);
        } else {
            mapResponse.put("mensaje", "Paciente no encontrado");
            mapResponse.put("code", 404);
        }
        return mapResponse;
    }


        // ==========================
    //   ESTADÍSTICAS PACIENTES
    // ==========================
        public Map<String, Object> getEstadisticas() {

            // Reutilizamos el método que ya devuelve todos los pacientes
            Map<String, Object> respuestaPacientes = getAllPacientes();
            Object lista = respuestaPacientes.get("listaPacientes");

            int total = 0;
            if (lista instanceof List<?>) {
                total = ((List<?>) lista).size();
            }

            Map<String, Object> stats = new HashMap<>();
            stats.put("total", total);
            stats.put("estructura", "Lista (Base de datos + estructuras propias)");
            stats.put("complejidadAgregar", "O(1)");
            stats.put("complejidadBuscar", "O(n)");

            return stats;
        }





    // ==========================
    //   MÉTODOS EXTRA (COLA)
    // ==========================

    // Actualiza un paciente dentro de la cola
    private void actualizarEnCola(Paciente actualizado) {
        int n = colaPacientes.tamaño();

        for (int i = 0; i < n; i++) {
            Paciente p = colaPacientes.quitarElemento();

            if (p.getId() == actualizado.getId()) {
                colaPacientes.insertarElemento(actualizado);
            } else {
                colaPacientes.insertarElemento(p);
            }
        }
    }


    // Elimina un paciente de la cola según su id
    private void eliminarDeCola(Integer id) {
        int n = colaPacientes.tamaño();

        for (int i = 0; i < n; i++) {
            Paciente p = colaPacientes.quitarElemento();

            if (p.getId() != id) {
                colaPacientes.insertarElemento(p);
            }
        }
    }


    // Por si sigues ocupando esto en estadísticas
    public int contarPacientesEnCola() {
        return colaPacientes.tamaño();
    }
}
