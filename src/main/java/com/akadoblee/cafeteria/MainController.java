package com.akadoblee.cafeteria;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class MainController {

    // Elementos de la interfaz
    @FXML private Button btnIniciar;
    @FXML private Button btnAñadirCliente;
    @FXML private ListView<String> listaEsperando;
    @FXML private ListView<String> listaAtendidos;
    @FXML private ListView<String> listaSeFueron;
    @FXML private Label lblEstado;

    // Cola de clientes y estado de la simulación
    Queue<Cliente> colaClientes = new ConcurrentLinkedQueue<>();
    boolean enMarcha = false;
    int contadorClientes = 0;

    // Inicia la simulación al pulsar el botón
    @FXML
    private void iniciarSimulacion() {
        btnIniciar.setDisable(true);
        listaEsperando.getItems().clear();
        listaAtendidos.getItems().clear();
        listaSeFueron.getItems().clear();
        lblEstado.setText("Simulación en curso...");
        contadorClientes = 0;

        enMarcha = true;

        // Crear camareros
        Camarero c1 = new Camarero("Camarero 1", colaClientes, this);
        Camarero c2 = new Camarero("Camarero 2", colaClientes, this);

        // Iniciar camareros
        c1.start();
        c2.start();

    }

    // Añade un nuevo cliente al pulsar el botón
    @FXML
    private void añadirCliente() {

        if (!enMarcha) return;

        String nombre = "Cliente " + contadorClientes;
        int tiempoEspera = (int) (Math.random() * 3000 + 2000);

        Cliente nuevoCliente = new Cliente(nombre, tiempoEspera, colaClientes, this);
        nuevoCliente.start();

        colaClientes.add(nuevoCliente);
        contadorClientes++;

    }

    // Detener la simulación con un botón
    @FXML
    private void detenerSimulacion() {

        enMarcha = false;
        btnIniciar.setDisable(false);
        lblEstado.setText("Simulación detenida.");

    }

    // Registra la llegada de un cliente
    public void clienteLlega(String nombre) {

        Platform.runLater(() -> {

            if (!listaEsperando.getItems().contains(nombre))
                listaEsperando.getItems().add(nombre);

        });

    }

    // Caso en que un cliente es atendido
    public void clienteAtendido(String nombre) {

        Platform.runLater(() -> {

            listaEsperando.getItems().remove(nombre);

            if (!listaAtendidos.getItems().contains(nombre))
                listaAtendidos.getItems().add(nombre);

        });

    }

    // Caso en que un cliente se va
    public void clienteSeVa(String nombre) {

        Platform.runLater(() -> {

            listaEsperando.getItems().remove(nombre);

            if (!listaSeFueron.getItems().contains(nombre))
                listaSeFueron.getItems().add(nombre);

        });

    }

}