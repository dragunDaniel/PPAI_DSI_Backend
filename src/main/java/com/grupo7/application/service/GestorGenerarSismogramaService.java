package com.grupo7.application.service;

// Dependencies
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class GestorGenerarSismogramaService {


    @Autowired
    public GestorGenerarSismogramaService () {

    }

    public void generarSismograma() {
        System.out.println("Caso de Uso Generar Sismograma");
    }

}
