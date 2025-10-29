package com.akadoblee.cafeteria;

import java.util.Queue;

public class Camarero extends Thread {

    // Atributos
    String nombre;
    Queue<Cliente> colaClientes;
    MainController controlador;

    // Constructor
    public Camarero(String nombre, Queue<Cliente> colaClientes, MainController controlador) {
        this.nombre = nombre;
        this.colaClientes = colaClientes;
        this.controlador = controlador;
    }

    // Método principal del hilo
    @Override
    public void run() {

        while (!interrupted()) {

            Cliente cliente = obtenerSiguienteCliente();
            if (cliente == null) continue;

            if (!cliente.activo || cliente.atendido) {

                colaClientes.poll();
                continue;

            }

            if (clienteHaEsperadoDemasiado(cliente)) {

                manejarClienteImpaciente(cliente);
                continue;

            }

            prepararCafe(cliente);

        }

    }

    // Obtiene el siguiente cliente de la cola y si no espera
    private Cliente obtenerSiguienteCliente() {

        Cliente cliente = colaClientes.peek();

        if (cliente == null) {

            try {

                Thread.sleep(200);

            } 
            catch (InterruptedException e) {

                interrupt();

            }

        }

        return cliente;

    }

    // Comprueba si el cliente ya esperó más de su tiempo permitido.
    private boolean clienteHaEsperadoDemasiado(Cliente cliente) {

        long espera = System.currentTimeMillis() - cliente.inicioEspera;
        return espera > cliente.tiempoEspera;

    }

    // Caso en que el cliente se cansa y se va.
    private void manejarClienteImpaciente(Cliente cliente) {

        if (!cliente.activo || cliente.atendido) return;

        cliente.activo = false;
        colaClientes.poll();
        controlador.clienteSeVa(cliente.nombre);

    }

     // Preparación del café para el cliente.
    private void prepararCafe(Cliente cliente) {

        if (!cliente.activo || cliente.atendido) return;

        int tiempoPreparacion = (int) (Math.random() * 3000 + 1000);

        try {

            Thread.sleep(tiempoPreparacion);

        } catch (InterruptedException e) {

            interrupt();
            return;

        }

        if (!cliente.activo) return;

        cliente.atendido = true;
        cliente.activo = false;
        colaClientes.poll();
        controlador.clienteAtendido(cliente.nombre);

    }

}