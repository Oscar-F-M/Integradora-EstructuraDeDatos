package com.example.avanceestructuradedatos.structures;

public class Nodo <T>{
    protected T dato;
    protected Nodo <T> siguiente;


    public Nodo(T dato){
        this.dato = dato;
        siguiente = null;
    }
}

