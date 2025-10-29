package com.akadoblee.cafeteria;

import java.util.Queue;

public class Camarero extends Thread {
    String nombre;
    Queue<Cliente> colaClientes;
    MainController controlador;

    public Camarero(String nombre, Queue<Cliente> colaClientes, MainController controlador) {
        this.nombre = nombre;
        this.colaClientes = colaClientes;
        this.controlador = controlador;
    }

    @Override
    public void run() {
        while (!interrupted()) {
            Cliente cliente = colaClientes.peek();
            if (cliente == null) {
                try { Thread.sleep(200); } catch (InterruptedException e) { break; }
                continue;
            }

            long espera = System.currentTimeMillis() - cliente.inicioEspera;
            if (espera > cliente.tiempoEspera) {
                colaClientes.poll();
                controlador.clienteSeVa(cliente.nombre);
                continue;
            }

            int tiempoPreparacion = (int) (Math.random() * 3000 + 1000);
            try { Thread.sleep(tiempoPreparacion); } catch (InterruptedException e) { break; }

            cliente.atendido = true;
            colaClientes.poll();
            controlador.clienteAtendido(cliente.nombre);
        }
    }
}
