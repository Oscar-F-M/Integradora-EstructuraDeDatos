package com.example.avanceestructuradedatos.structures;

public class Cola <T> {
    private Nodo<T> inicio;
    private Nodo<T> fin;
    private int size;

    public Cola() {
        inicio = null;
        fin = null;
        size = 0;
    }

    public boolean verificarColaVacia(){
        return inicio == null;
    }

    public void insertarElemento(T elemento){

        Nodo <T> nuevo = new Nodo<>(elemento);
        if(verificarColaVacia()){
            inicio = nuevo;
        }
        else{
            fin.siguiente = nuevo;
        }
        fin = nuevo;
        size++;

    }

    public T quitarElemento(){
        if(verificarColaVacia()){
            System.out.println("No hay elementos");
            return null;
        }else{
            T aux = inicio.dato;
            inicio =  inicio.siguiente;
            size--;
            return aux;
        }
    }
    public T verInicio(){
        if(verificarColaVacia()) {
            System.out.println("No hay elementos");
            return null;
        }
        return inicio.dato;
    }
    public void mostrarElementos(){
        if(verificarColaVacia()){
            System.out.println("No hay elementos");

        }

        System.out.print("Mostrando elementos... [");
        Nodo <T> actual = inicio;
        while(actual!= null){
            System.out.print(actual.dato + " ");
            actual = actual.siguiente;
        }
        System.out.print("/ ]");
    }
    public void borrarElementos(){
        inicio = fin = null;
        size = 0;
    }
    public int tamaño(){
        return size;
    }
}

