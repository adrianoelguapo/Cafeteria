package com.akadoblee.cafeteria;

import java.util.Queue;

public class Cliente extends Thread {
    String nombre;
    int tiempoEspera;
    boolean atendido = false;
    long inicioEspera;
    Queue<Cliente> colaClientes;
    MainController controlador;

    public Cliente(String nombre, int tiempoEspera, Queue<Cliente> colaClientes, MainController controlador) {
        this.nombre = nombre;
        this.tiempoEspera = tiempoEspera;
        this.colaClientes = colaClientes;
        this.controlador = controlador;
    }

    @Override
    public void run() {
        try {
            Thread.sleep((int) (Math.random() * 3000)); // llegada aleatoria
        } catch (InterruptedException e) { return; }

        inicioEspera = System.currentTimeMillis();
        colaClientes.add(this);
        controlador.clienteLlega(nombre);

        while (!atendido) {
            if (System.currentTimeMillis() - inicioEspera > tiempoEspera) {
                colaClientes.remove(this);
                controlador.clienteSeVa(nombre);
                return;
            }
            try { Thread.sleep(100); } catch (InterruptedException e) { return; }
        }
    }
}
