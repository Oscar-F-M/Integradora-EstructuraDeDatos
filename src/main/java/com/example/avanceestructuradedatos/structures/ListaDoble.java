package com.example.avanceestructuradedatos.structures;

public class ListaDoble {
    static class Nodo {
        int dato;
        Nodo siguiente;
        Nodo anterior;
        Nodo(int dato) {
            this.dato = dato;
        }
    }

    private Nodo cabeza, cola;

    public void agregarInicio(int dato) {
        Nodo nuevo = new Nodo(dato);
        if (cabeza == null) {
            cabeza = cola = nuevo;
        } else {
            nuevo.siguiente = cabeza;
            cabeza.anterior = nuevo;
            cabeza = nuevo;
        }
    }

    public void agregarFinal(int dato) {
        Nodo nuevo = new Nodo(dato);
        if (cabeza == null) {
            cabeza = cola = nuevo;
        } else {
            cola.siguiente = nuevo;
            nuevo.anterior = cola;
            cola = nuevo;
        }
    }

    public boolean buscar(int valor) {
        Nodo actual = cabeza;
        while (actual != null) {
            if (actual.dato == valor)
                return true;
            actual = actual.siguiente;
        }
        return false;
    }

    public void eliminar(int valor) {
        Nodo actual = cabeza;
        while (actual != null) {
            if (actual.dato == valor) {
                if (actual.anterior != null){
                    actual.anterior.siguiente = actual.siguiente;
                }else{
                    cabeza = actual.siguiente;
                }

                if (actual.siguiente != null){
                    actual.siguiente.anterior = actual.anterior;
                }else{
                    cola = actual.anterior;
                    return;
                }
            }
            actual = actual.siguiente;
        }
    }

    public void ordenar() {
        if (cabeza == null) return;
        Nodo actual = cabeza;
        while (actual != null) {
            Nodo siguiente = actual.siguiente;
            while (siguiente != null) {
                if (actual.dato > siguiente.dato) {
                    int temp = actual.dato;
                    actual.dato = siguiente.dato;
                    siguiente.dato = temp;
                }
                siguiente = siguiente.siguiente;
            }
            actual = actual.siguiente;
        }
    }

    public void mostrar() {
        Nodo actual = cabeza;
        while (actual != null) {
            System.out.print(actual.dato + " , ");
            actual = actual.siguiente;
        }
        System.out.println("null");
    }
}

