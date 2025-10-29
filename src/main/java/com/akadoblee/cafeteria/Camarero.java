package com.akadoblee.cafeteria;

import java.util.Queue;

public class Camarero extends Thread {
    String nombre;
    Queue<Cliente> colaClientes;

    public Camarero(String nombre, Queue<Cliente> colaClientes) {
        this.nombre = nombre;
        this.colaClientes = colaClientes;
    }

    @Override
    public void run() {
        while (true) {
            if (Thread.interrupted()) break;

            Cliente cliente = colaClientes.peek();
            if (cliente == null) {
                try { Thread.sleep(200); } catch (InterruptedException e) { break; }
                continue;
            }

            long espera = System.currentTimeMillis() - cliente.inicioEspera;
            if (espera > cliente.tiempoEspera) {
                colaClientes.poll();
                continue;
            }

            int tiempoPreparacion = (int) (Math.random() * 3000 + 1000);
            try { Thread.sleep(tiempoPreparacion); } catch (InterruptedException e) { break; }

            cliente.atendido = true;
            colaClientes.poll();
        }
    }
}
