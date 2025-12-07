package com.example.avanceestructuradedatos.service;

import com.example.avanceestructuradedatos.model.Historial;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class HistorialService {

    // ----- PILA PROPIA PARA HISTORIALES -----
    // Nodo de la pila: guarda un HistorialClinico y la referencia al siguiente nodo (debajo en la pila)
    private static class NodoPila {
        Historial dato;
        NodoPila siguiente;

        NodoPila(Historial dato) {
            this.dato = dato;
        }
    }

    // Implementación de una pila simple para registros de historial
    private static class PilaRegistros {
        private NodoPila cima;  // Apunta al elemento más reciente de la pila
        private int size;       // Cantidad de elementos en la pila

        // Apilar (push): inserta un nuevo registro en la cima
        void apilar(Historial registro) {
            NodoPila nuevo = new NodoPila(registro);
            nuevo.siguiente = cima; // El nuevo nodo apunta al que antes era la cima
            cima = nuevo;           // Actualizamos la cima
            size++;
        }

        // Desapilar (pop): elimina y devuelve el elemento de la cima
        Historial desapilar() {
            if (estaVacia()) {
                return null;
            }
            Historial aux = cima.dato; // Guardamos el dato de la cima
            cima = cima.siguiente;            // Movemos la cima al siguiente nodo
            size--;
            return aux;                       // Devolvemos el registro que estaba arriba
        }

        // Ver la cima sin eliminarla
        Historial verCima() {
            if (estaVacia()) {
                return null;
            }
            return cima.dato;
        }

        // Verifica si la pila está vacía
        boolean estaVacia() {
            return cima == null;
        }

        // Devuelve el tamaño de la pila
        int tamaño() {
            return size;
        }

        // Limpia por completo la pila
        void limpiar() {
            cima = null;
            size = 0;
        }

        // Devuelve un arreglo con todos los registros,
        // ordenados desde el más antiguo al más reciente
        Historial[] aArreglo() {
            Historial[] temp = new Historial[size];
            int i = 0;

            // Primero copiamos desde la cima hacia abajo: (último -> primero)
            for (NodoPila actual = cima; actual != null; actual = actual.siguiente) {
                temp[i++] = actual.dato;
            }

            // temp[0] = más reciente, temp[size-1] = más antiguo
            // Invertimos el arreglo para que quede: [más antiguo ... más reciente]
            for (int j = 0; j < size / 2; j++) {
                Historial aux = temp[j];
                temp[j] = temp[size - 1 - j];
                temp[size - 1 - j] = aux;
            }
            return temp;
        }
    }

    // ----- LISTA DE PACIENTES CON SU PILA -----
    // Cada nodo de esta lista representa a un paciente con su pila de registros
    private static class NodoHistorialPaciente {
        String pacienteId;          // ID del paciente
        PilaRegistros pila;        // Pila de registros de ese paciente
        NodoHistorialPaciente siguiente; // Siguiente nodo en la lista

        NodoHistorialPaciente(String pacienteId) {
            this.pacienteId = pacienteId;
            this.pila = new PilaRegistros();
        }
    }

    // Cabeza de la lista de pacientes (primer nodo)
    private NodoHistorialPaciente cabeza;

    // Constructor: lista inicialmente vacía
    public HistorialService() {
        this.cabeza = null;
    }

    // Buscar un nodo de paciente por su ID en la lista enlazada
    private NodoHistorialPaciente buscarNodo(String pacienteId) {
        NodoHistorialPaciente actual = cabeza;
        while (actual != null) {
            if (actual.pacienteId.equals(pacienteId)) {
                return actual; // Encontramos el nodo del paciente
            }
            actual = actual.siguiente;
        }
        return null; // No se encontró
    }

    // Obtener el nodo de un paciente, o crearlo si no existe
    private NodoHistorialPaciente obtenerONuevoNodo(String pacienteId) {
        NodoHistorialPaciente nodo = buscarNodo(pacienteId);
        if (nodo != null) {
            return nodo; // Ya existía, se regresa
        }
        // Si no existe, se crea uno nuevo y se inserta al inicio de la lista
        NodoHistorialPaciente nuevo = new NodoHistorialPaciente(pacienteId);
        nuevo.siguiente = cabeza;
        cabeza = nuevo;
        return nuevo;
    }

    // ----- MÉTODOS PÚBLICOS -----

    /**
     * Agrega un nuevo registro clínico al historial de un paciente.
     * Si el paciente no tiene aún historial, se crea su pila.
     */
    public void agregarRegistro(Historial registro) {

        // Si no viene id, se genera automáticamente
        if (registro.getId() == null || registro.getId().isEmpty()) {
            registro.setId(UUID.randomUUID().toString());
        }

        // Si no viene fecha, usamos la fecha/hora actual
        if (registro.getFecha() == null) {
            registro.setFecha(LocalDateTime.now());
        }

        String pacienteId = registro.getPacienteId();

        // Obtenemos o creamos el nodo del paciente
        NodoHistorialPaciente nodo = obtenerONuevoNodo(pacienteId);

        // Apilamos el nuevo registro en la pila de ese paciente
        nodo.pila.apilar(registro);

        System.out.println("Registro agregado al historial del paciente: " + pacienteId);
        System.out.println("Diagnóstico: " + registro.getDiagnostico());
        System.out.println("Total de registros: " + nodo.pila.tamaño());
    }


    /**
     * Elimina el último registro (más reciente) del historial de un paciente.
     */
    public Historial removerUltimoRegistro(String pacienteId) {
        NodoHistorialPaciente nodo = buscarNodo(pacienteId);
        if (nodo == null || nodo.pila.estaVacia()) {
            System.out.println("No hay registros para el paciente: " + pacienteId);
            return null;
        }
        Historial registroRemovido = nodo.pila.desapilar();
        System.out.println("Registro removido (desapilado)");
        return registroRemovido;
    }

    /**
     * Devuelve el último registro del historial de un paciente
     * sin eliminarlo de la pila.
     */
    public Historial verUltimoRegistro(String pacienteId) {
        NodoHistorialPaciente nodo = buscarNodo(pacienteId);
        if (nodo == null || nodo.pila.estaVacia()) {
            return null;
        }
        return nodo.pila.verCima();
    }

    /*
     * Indica si un paciente tiene al menos un registro en su historial.
     */
    public boolean tieneHistorial(String pacienteId) {
        NodoHistorialPaciente nodo = buscarNodo(pacienteId);
        return nodo != null && !nodo.pila.estaVacia();
    }

    /*
     * Devuelve todo el historial de un paciente en un arreglo,
     * ordenado desde el registro más antiguo hasta el más reciente.
     */
    public Historial[] obtenerHistorialCompleto(String pacienteId) {
        NodoHistorialPaciente nodo = buscarNodo(pacienteId);
        if (nodo == null || nodo.pila.estaVacia()) {
            return new Historial[0];
        }
        return nodo.pila.aArreglo();
    }

    /**
     * Devuelve los últimos 'cantidad' registros del historial de un paciente.
     * Si tiene menos registros, se devuelven todos.
     */
    public Historial[] obtenerUltimosRegistros(String pacienteId, int cantidad) {
        Historial[] todos = obtenerHistorialCompleto(pacienteId);
        if (todos.length == 0) {
            return new Historial[0];
        }

        // Calcula desde qué índice tomar los últimos 'cantidad' registros
        int desde = Math.max(0, todos.length - cantidad);
        int len = todos.length - desde;

        Historial[] resultado = new Historial[len];
        for (int i = 0; i < len; i++) {
            resultado[i] = todos[desde + i];
        }
        return resultado;
    }

    /**
     * Cuenta cuántos registros tiene un paciente en su historial.
     */
    public int contarRegistros(String pacienteId) {
        NodoHistorialPaciente nodo = buscarNodo(pacienteId);
        if (nodo == null) {
            return 0;
        }
        return nodo.pila.tamaño();
    }

    /**
     * Busca registros de un paciente cuyo diagnóstico contenga
     * la cadena indicada (búsqueda por texto).
     */
    public Historial[] buscarPorDiagnostico(String pacienteId, String diagnostico) {
        Historial[] todos = obtenerHistorialCompleto(pacienteId);
        String busqueda = diagnostico.toLowerCase();

        //Cuenta cuántos registros coinciden
        int coincidencias = 0;
        for (Historial r : todos) {
            if (r.getDiagnostico().toLowerCase().contains(busqueda)) {
                coincidencias++;
            }
        }

        //Arreglo solo con los que coinciden
        Historial[] resultados = new Historial[coincidencias];
        int idx = 0;
        for (Historial r : todos) {
            if (r.getDiagnostico().toLowerCase().contains(busqueda)) {
                resultados[idx++] = r;
            }
        }
        return resultados;
    }

    /**
     * Elimina todos los registros del historial de un paciente.
     */
    public void limpiarHistorial(String pacienteId) {
        NodoHistorialPaciente nodo = buscarNodo(pacienteId);
        if (nodo != null) {
            nodo.pila.limpiar();
            System.out.println("Historial limpiado para paciente: " + pacienteId);
        }
    }

    /**
     * Muestra por consola estadísticas del historial de un paciente:
     * total de registros y datos del último registro, si existe.
     */
    public void mostrarEstadisticas(String pacienteId) {
        System.out.println("\nHISTORIAL CLÍNICO");
        System.out.println("Paciente ID: " + pacienteId);
        System.out.println("Total de registros: " + contarRegistros(pacienteId));
        Historial ultimo = verUltimoRegistro(pacienteId);
        if (ultimo != null) {
            System.out.println("Último registro:");
            System.out.println("  Fecha: " + ultimo.getFecha());
            System.out.println("  Médico: " + ultimo.getNombreMedico());
            System.out.println("  Diagnóstico: " + ultimo.getDiagnostico());
        } else {
            System.out.println("Sin registros médicos");
        }
    }

    // Clase auxiliar para devolver un pequeño resumen de historial por paciente
    public static class ResumenHistorial {
        private final String pacienteId;
        private final int totalRegistros;

        public ResumenHistorial(String pacienteId, int totalRegistros) {
            this.pacienteId = pacienteId;
            this.totalRegistros = totalRegistros;
        }

        public String getPacienteId() {
            return pacienteId;
        }

        public int getTotalRegistros() {
            return totalRegistros;
        }
    }

    /**
     * Devuelve un arreglo con un resumen general:
     * cada elemento indica el ID de un paciente y cuántos registros tiene.
     */
    public ResumenHistorial[] obtenerResumenGeneral() {
        // Primero contamos cuántos pacientes (nodos) hay en la lista
        int count = 0;
        for (NodoHistorialPaciente actual = cabeza; actual != null; actual = actual.siguiente) {
            count++;
        }

        // Creamos el arreglo del tamaño exacto
        ResumenHistorial[] resumen = new ResumenHistorial[count];
        int i = 0;
        NodoHistorialPaciente actual = cabeza;

        // Recorremos la lista y llenamos el arreglo
        while (actual != null) {
            resumen[i++] = new ResumenHistorial(
                    actual.pacienteId,
                    actual.pila.tamaño()
            );
            actual = actual.siguiente;
        }
        return resumen;
    }
}
