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
            int tiempoLlegada = (int) (Math.random() * 3000);
            Thread.sleep(tiempoLlegada);
        } catch (InterruptedException e) {
            return;
        }

        System.out.println(nombre + " ha llegado a la cafetería (esperará " + tiempoEspera + " ms)");
        inicioEspera = System.currentTimeMillis();
        colaClientes.add(this);

        while (!atendido) {
            if (System.currentTimeMillis() - inicioEspera > tiempoEspera) {
                if (!atendido) {
                    System.out.println(nombre + " se cansó de esperar y se fue");
                    colaClientes.remove(this);
                }
                return;
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                return;
            }
        }

        System.out.println(nombre + " recibió su café y está feliz");
    }
}
