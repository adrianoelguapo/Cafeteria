package com.akadoblee.cafeteria;

import java.util.Queue;

public class Cliente extends Thread {
    String nombre;
    int tiempoEspera;
    boolean atendido = false;
    long inicioEspera;
    Queue<Cliente> colaClientes;

    public Cliente(String nombre, int tiempoEspera, Queue<Cliente> colaClientes) {
        this.nombre = nombre;
        this.tiempoEspera = tiempoEspera;
        this.colaClientes = colaClientes;
    }

    @Override
    public void run() {
        try {
            Thread.sleep((int) (Math.random() * 3000));
        } catch (InterruptedException e) {
            return;
        }

        inicioEspera = System.currentTimeMillis();
        colaClientes.add(this);

        while (!atendido) {
            if (System.currentTimeMillis() - inicioEspera > tiempoEspera) {
                colaClientes.remove(this);
                return;
            }
            try { Thread.sleep(100); } catch (InterruptedException e) { return; }
        }
    }
}
