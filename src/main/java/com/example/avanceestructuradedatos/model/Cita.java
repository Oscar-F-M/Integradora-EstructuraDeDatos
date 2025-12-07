package com.example.avanceestructuradedatos.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "cita")
public class Cita {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    // Relación MUCHAS citas -> UN paciente
    @ManyToOne
    @JoinColumn(name = "paciente_id", nullable = false)
    private Paciente paciente;

    private String nombreMedico;
    private String especialidad;
    private LocalDateTime fechaHora;
    private String motivo;

    private String prioridad; // EMERGENCIA, URGENTE, NORMAL
    private String estado;    // PENDIENTE, EN_ATENCION, ATENDIDA, CANCELADA

    // ===== CONSTRUCTOR VACÍO =====
    public Cita() {
    }

    // ===== CONSTRUCTOR COMPLETO =====
    public Cita(int id, Paciente paciente, String nombreMedico,
                String especialidad, LocalDateTime fechaHora, String motivo,
                String prioridad, String estado) {
        this.id = id;
        this.paciente = paciente;
        this.nombreMedico = nombreMedico;
        this.especialidad = especialidad;
        this.fechaHora = fechaHora;
        this.motivo = motivo;
        this.prioridad = prioridad;
        this.estado = estado;
    }

    // ===== GETTERS & SETTERS =====

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public Paciente getPaciente() {
        return paciente;
    }
    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
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

    // ===== TOSTRING LIMPIO =====
    @Override
    public String toString() {
        return "Cita{" +
                "id=" + id +
                ", paciente=" + (paciente != null ? paciente.getNombreCompleto() : "Sin paciente") +
                ", medico='" + nombreMedico + '\'' +
                ", prioridad='" + prioridad + '\'' +
                ", estado='" + estado + '\'' +
                '}';
    }
}
