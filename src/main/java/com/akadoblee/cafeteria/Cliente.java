package com.akadoblee.cafeteria;

import java.util.Queue;

public class Cliente extends Thread {

    // Atributos
    String nombre;
    int tiempoEspera;
    boolean atendido = false;
    long inicioEspera;
    Queue<Cliente> colaClientes;
    MainController controlador;

    // Constructor
    public Cliente(String nombre, int tiempoEspera, Queue<Cliente> colaClientes, MainController controlador) {

        this.nombre = nombre;
        this.tiempoEspera = tiempoEspera;
        this.colaClientes = colaClientes;
        this.controlador = controlador;

    }

    // Método principal del hilo
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

    // Retraso de llegada aleatoria del cliente
    private void esperarLlegadaAleatoria() {
        
        try {

            Thread.sleep((int) (Math.random() * 3000));

        } catch (InterruptedException e) {

            interrupt();

        }

    }

    // Registra el momento de llegada y lo comunica al controlador.
    private void registrarLlegada() {

        inicioEspera = System.currentTimeMillis();
        colaClientes.add(this);
        controlador.clienteLlega(nombre);

    }

    // Comprueba si el cliente ha esperado más de su tiempo permitido.
    private boolean haEsperadoDemasiado() {

        return System.currentTimeMillis() - inicioEspera > tiempoEspera;

    }

    // Caso en que el cliente se cansa y abandona la cafetería.
    private void irsePorImpaciencia() {

        colaClientes.remove(this);
        controlador.clienteSeVa(nombre);

    }

    // Duerme un poco el hilo para evitar un bucles de espera.
    private void dormirUnMomento() {

        try {

            Thread.sleep(100);

        } catch (InterruptedException e) {

            interrupt();

        }

    }

}