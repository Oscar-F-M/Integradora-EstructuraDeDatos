package com.example.avanceestructuradedatos.model;

import java.time.LocalDateTime;
public class Historial {
    private String id;
    private String pacienteId;
    private LocalDateTime fecha;
    private String nombreMedico;
    private String diagnostico;
    private String tratamiento;
    private String observaciones;
    private String medicamentosRecetados;

    public Historial() {
        this.fecha = LocalDateTime.now();
    }
    public Historial(String id, String pacienteId, String nombreMedico,
                     String diagnostico, String tratamiento,
                     String observaciones, String medicamentosRecetados) {
        this.id = id;
        this.pacienteId = pacienteId;
        this.fecha = LocalDateTime.now();
        this.nombreMedico = nombreMedico;
        this.diagnostico = diagnostico;
        this.tratamiento = tratamiento;
        this.observaciones = observaciones;
        this.medicamentosRecetados = medicamentosRecetados;
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getPacienteId() {
        return pacienteId;
    }
    public void setPacienteId(String pacienteId) {
        this.pacienteId = pacienteId;
    }
    public LocalDateTime getFecha() {
        return fecha;
    }
    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }
    public String getNombreMedico() {
        return nombreMedico;
    }
    public void setNombreMedico(String nombreMedico) {
        this.nombreMedico = nombreMedico;
    }
    public String getDiagnostico() {
        return diagnostico;
    }
    public void setDiagnostico(String diagnostico) {
        this.diagnostico = diagnostico;
    }
    public String getTratamiento() {
        return tratamiento;
    }
    public void setTratamiento(String tratamiento) {
        this.tratamiento = tratamiento;
    }
    public String getObservaciones() {
        return observaciones;
    }
    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
    public String getMedicamentosRecetados() {
        return medicamentosRecetados;
    }
    public void setMedicamentosRecetados(String medicamentosRecetados) {
        this.medicamentosRecetados = medicamentosRecetados;
    }
    @Override
    public String toString() {
        return "HistorialClinico{" +
                "id='" + id + '\'' +
                ", pacienteId='" + pacienteId + '\'' +
                ", fecha=" + fecha +
                ", medico='" + nombreMedico + '\'' +
                ", diagnostico='" + diagnostico + '\'' +
                '}';
    }
}