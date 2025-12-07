package com.example.avanceestructuradedatos.service;

import com.example.avanceestructuradedatos.dtos.CreateCitaDTO;
import com.example.avanceestructuradedatos.structures.Cola;
import org.springframework.stereotype.Service;

@Service
public class CitaService {

    // Cola para citas normales (prioridad más baja)
    private Cola<CreateCitaDTO> colaPrincipal;
    // Cola para citas de emergencia (prioridad más alta)
    private Cola<CreateCitaDTO> colaEmergencias;
    // Cola para citas urgentes (prioridad intermedia)
    private Cola<CreateCitaDTO> colaUrgentes;

    public CitaService() {
        this.colaPrincipal   = new Cola<>();
        this.colaEmergencias = new Cola<>();
        this.colaUrgentes    = new Cola<>();
    }

    /**
     * Agrega una cita a la cola correspondiente según su prioridad.
     * prioridad: "EMERGENCIA", "URGENTE", "NORMAL" (o null).
     */
    public void agregarACola(CreateCitaDTO cita) {

        String prioridad = cita.getPrioridad();
        if (prioridad == null) {
            prioridad = "NORMAL";
        }

        switch (prioridad.toUpperCase()) {
            case "EMERGENCIA":
                colaEmergencias.insertarElemento(cita);
                System.out.println("EMERGENCIA agregada a cola. PacienteId = "
                        + cita.getPacienteId());
                break;

            case "URGENTE":
                colaUrgentes.insertarElemento(cita);
                System.out.println("Cita URGENTE agregada a cola. PacienteId = "
                        + cita.getPacienteId());
                break;

            default:
                colaPrincipal.insertarElemento(cita);
                System.out.println("Cita NORMAL agregada a cola. PacienteId = "
                        + cita.getPacienteId());
                break;
        }

        // cuando se agrega una cita nueva su estado normal es PENDIENTE
        if (cita.getEstado() == null || cita.getEstado().isEmpty()) {
            cita.setEstado("PENDIENTE");
        }
    }

    /**
     * Atiende la siguiente cita según prioridad.
     * 1) emergencias, 2) urgentes, 3) normales.
     */
    public CreateCitaDTO atenderSiguiente() {
        CreateCitaDTO citaAtendida = null;

        if (!colaEmergencias.verificarColaVacia()) {
            citaAtendida = colaEmergencias.quitarElemento();
            System.out.println("Atendiendo EMERGENCIA. PacienteId = "
                    + citaAtendida.getPacienteId());

        } else if (!colaUrgentes.verificarColaVacia()) {
            citaAtendida = colaUrgentes.quitarElemento();
            System.out.println("Atendiendo URGENTE. PacienteId = "
                    + citaAtendida.getPacienteId());

        } else if (!colaPrincipal.verificarColaVacia()) {
            citaAtendida = colaPrincipal.quitarElemento();
            System.out.println("Atendiendo NORMAL. PacienteId = "
                    + citaAtendida.getPacienteId());

        } else {
            System.out.println("No hay pacientes en cola de espera");
        }

        if (citaAtendida != null) {
            citaAtendida.setEstado("EN_ATENCION");
        }

        return citaAtendida;
    }

    /**
     * Devuelve el número total de citas pendientes en todas las colas.
     */
    public int contarCitasEnEspera() {
        return colaEmergencias.tamaño()
                + colaUrgentes.tamaño()
                + colaPrincipal.tamaño();
    }

    /**
     * Devuelve un arreglo con todas las citas, ordenadas por prioridad:
     *   [ Emergencias ... Urgentes ... Normales ]
     */
    public CreateCitaDTO[] obtenerTodasLasCitas() {
        int total = contarCitasEnEspera();
        CreateCitaDTO[] todas = new CreateCitaDTO[total];
        int index = 0;

        index = copiarColaEnArreglo(colaEmergencias, todas, index);
        index = copiarColaEnArreglo(colaUrgentes, todas, index);
        index = copiarColaEnArreglo(colaPrincipal, todas, index);

        return todas;
    }

    private int copiarColaEnArreglo(Cola<CreateCitaDTO> cola,
                                    CreateCitaDTO[] destino,
                                    int indiceInicio) {

        int n = cola.tamaño();
        for (int i = 0; i < n; i++) {
            CreateCitaDTO c = cola.quitarElemento();
            destino[indiceInicio + i] = c;
            cola.insertarElemento(c);   // volvemos a dejarla igual
        }
        return indiceInicio + n;
    }

    /**
     * Limpia por completo todas las colas de citas.
     */
    public void limpiarColas() {
        colaPrincipal.borrarElementos();
        colaEmergencias.borrarElementos();
        colaUrgentes.borrarElementos();
        System.out.println("Todas las colas han sido limpiadas");
    }

    // ========= MÉTODOS QUE ESPERA EL CONTROLADOR =========

    /** Devuelve la siguiente cita a atender, sin sacarla de la cola. */
    public CreateCitaDTO verSiguiente() {
        CreateCitaDTO[] todas = obtenerTodasLasCitas();
        if (todas.length == 0) {
            return null;
        }
        return todas[0]; // la de mayor prioridad
    }

    /** Nombre que usa el controlador para obtener todas las citas en orden. */
    public CreateCitaDTO[] obtenerTodasEnOrden() {
        return obtenerTodasLasCitas();
    }

    /** Nombre que usa el controlador para tamaño total de la cola. */
    public int tamaño() {
        return contarCitasEnEspera();
    }

    /** Nombre que usa el controlador para saber si está vacía. */
    public boolean estaVacia() {
        return contarCitasEnEspera() == 0;
    }
}
