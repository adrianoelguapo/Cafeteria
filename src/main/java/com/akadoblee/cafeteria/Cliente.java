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
        esperarLlegadaAleatoria();
        registrarLlegada();

        while (!atendido) {
            if (haEsperadoDemasiado()) {
                irsePorImpaciencia();
                return;
            }
            dormirUnMomento();
        }
    }

    /** Simula el retraso de llegada aleatoria del cliente a la cafetería. */
    private void esperarLlegadaAleatoria() {
        try {
            Thread.sleep((int) (Math.random() * 3000)); // entre 0–3s
        } catch (InterruptedException e) {
            interrupt();
        }
    }

    /** Registra el momento de llegada y lo comunica al controlador. */
    private void registrarLlegada() {
        inicioEspera = System.currentTimeMillis();
        colaClientes.add(this);
        controlador.clienteLlega(nombre);
    }

    /** Comprueba si el cliente ha esperado más de su tiempo permitido. */
    private boolean haEsperadoDemasiado() {
        return System.currentTimeMillis() - inicioEspera > tiempoEspera;
    }

    /** Maneja el caso en que el cliente se cansa y abandona la cafetería. */
    private void irsePorImpaciencia() {
        colaClientes.remove(this);
        controlador.clienteSeVa(nombre);
    }

    /** Duerme un poco para evitar un bucle de espera activo. */
    private void dormirUnMomento() {
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            interrupt();
        }
    }
}
