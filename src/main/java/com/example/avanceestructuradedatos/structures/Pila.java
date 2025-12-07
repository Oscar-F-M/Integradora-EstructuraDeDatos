package com.example.avanceestructuradedatos.structures;

public class Pila <T> {

    private Nodo<T> cima;
    private int size;

    public void push(T valor) {
        Nodo<T> nuevo = new Nodo<>(valor);
        nuevo.siguiente = cima;
        cima = nuevo;
        size++;
    }

    public T pop() {
        if (pilaVacia()) {
            System.out.println("Pila vacia");
            return null;
        }
        T aux = cima.dato;
        cima = cima.siguiente;
        size--;
        return aux;
    }

    public boolean pilaVacia() {
        return cima == null;
    }

    public void mostrarPila(){
        if(pilaVacia()){
            System.out.println("Pila vacia");
            return;
        }

        Nodo <T> aux = cima;
        System.out.print("Contenido de la pila: ");
        while(aux != null){
            System.out.print(aux.dato + " ");
            aux = aux.siguiente;
        }

        System.out.println();

    }

    public void reverso(T dato){
        push(dato);
    }

}

