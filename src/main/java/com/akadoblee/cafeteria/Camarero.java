package com.akadoblee.cafeteria;

import java.util.Queue;

public class Camarero extends Thread {
    String nombre;
    int cafesServidos = 0;
    Queue<Cliente> colaClientes;

    public Camarero(String nombre, Queue<Cliente> colaClientes) {
        this.nombre = nombre;
        this.colaClientes = colaClientes;
    }

    @Override
    public void run() {
        while (true) {
            Cliente cliente = colaClientes.poll();

            if (cliente == null) {
                if (Main.todosLlegaron && colaClientes.isEmpty()) break;
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    break;
                }
                continue;
            }

            int tiempoPreparacion = (int) (Math.random() * 3000 + 1000);
            System.out.println(nombre + " está preparando café para " + cliente.nombre + " (" + tiempoPreparacion + " ms)");

            try {
                Thread.sleep(tiempoPreparacion);
            } catch (InterruptedException e) {
                break;
            }

            synchronized(cliente) {
                if (!cliente.atendido) {
                    cliente.atendido = true;
                    cafesServidos++;
                    System.out.println(nombre + " terminó el café de " + cliente.nombre);
                }
            }
        }

        System.out.println(nombre + " terminó su turno con " + cafesServidos + " cafés servidos.");
    }
}
