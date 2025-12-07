package com.example.avanceestructuradedatos.dtos;

import java.time.LocalDateTime;

public class CreateCitaDTO {

    private int id;
    private int pacienteId;   // SOLO el ID del paciente
    private String nombreMedico;
    private String especialidad;
    private LocalDateTime fechaHora;
    private String motivo;
    private String prioridad; // EMERGENCIA, URGENTE, NORMAL
    private String estado;    // PENDIENTE, EN_ATENCION, ATENDIDA, CANCELADA

    // ===== GETTERS & SETTERS =====

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public int getPacienteId() {
        return pacienteId;
    }
    public void setPacienteId(int pacienteId) {
        this.pacienteId = pacienteId;
    }

    public String getNombreMedico() {
        return nombreMedico;
    }
    public void setNombreMedico(String nombreMedico) {
        this.nombreMedico = nombreMedico;
    }

    public String getEspecialidad() {
        return especialidad;
    }
    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }
    public void setFechaHora(LocalDateTime fechaHora) {
        this.fechaHora = fechaHora;
    }

    public String getMotivo() {
        return motivo;
    }
    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public String getPrioridad() {
        return prioridad;
    }
    public void setPrioridad(String prioridad) {
        this.prioridad = prioridad;
    }

    public String getEstado() {
        return estado;
    }
    public void setEstado(String estado) {
        this.estado = estado;
    }
}
