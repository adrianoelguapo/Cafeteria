package com.akadoblee.cafeteria;

import java.util.Queue;

public class Cliente extends Thread {

    // Atributos
    String nombre;
    int tiempoEspera;
    boolean atendido = false;
    boolean activo = true;
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

        registrarLlegada();

        while (!atendido) {

            if (haEsperadoDemasiado()) {

                irsePorImpaciencia();
                return;

            }

            dormirHilo();

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

    // Caso en que el cliente se cansa y se va.
    private void irsePorImpaciencia() {

        if (!activo) return;

        activo = false;
        colaClientes.remove(this);
        controlador.clienteSeVa(nombre);

    }

    // Duerme un poco el hilo para evitar un bucles de espera.
    private void dormirHilo() {

        try {

            Thread.sleep(100);

        } catch (InterruptedException e) {

            interrupt();

        }

    }

}