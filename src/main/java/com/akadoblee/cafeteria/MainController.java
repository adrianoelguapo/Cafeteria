package com.akadoblee.cafeteria;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class MainController {

    @FXML private Button btnIniciar;
    @FXML private ListView<String> listaEsperando;
    @FXML private ListView<String> listaAtendidos;
    @FXML private ListView<String> listaSeFueron;
    @FXML private Label lblEstado;

    Queue<Cliente> colaClientes = new ConcurrentLinkedQueue<>();
    volatile boolean enMarcha = false;

    @FXML
    private void iniciarSimulacion() {
        btnIniciar.setDisable(true);
        listaEsperando.getItems().clear();
        listaAtendidos.getItems().clear();
        listaSeFueron.getItems().clear();
        lblEstado.setText("Simulación en curso...");

        enMarcha = true;

        // Crear camareros
        Camarero c1 = new Camarero("Camarero 1", colaClientes, this);
        Camarero c2 = new Camarero("Camarero 2", colaClientes, this);

        // Crear clientes
        Cliente[] clientes = {
            new Cliente("Ana", 3000, colaClientes, this),
            new Cliente("Luis", 5000, colaClientes, this),
            new Cliente("Marta", 2000, colaClientes, this),
            new Cliente("Carlos", 4000, colaClientes, this),
            new Cliente("Sofía", 6000, colaClientes, this)
        };

        // Iniciar camareros
        c1.start();
        c2.start();

        // Iniciar clientes
        for (Cliente cliente : clientes) {
            cliente.start();
        }

        // Esperar finalización sin hilos extra
        new Thread(() -> {
            for (Cliente cliente : clientes) {
                try {
                    cliente.join();
                } catch (InterruptedException ignored) {}
            }

            enMarcha = false;
            c1.interrupt();
            c2.interrupt();

            try {
                c1.join(); 
                c2.join(); 
            } catch (InterruptedException ignored) {}

            Platform.runLater(() -> {
                lblEstado.setText("Todos los clientes fueron atendidos o se fueron.");
                btnIniciar.setDisable(false);
            });
        }).start();
    }

    public void clienteLlega(String nombre) {
        Platform.runLater(() -> {
            if (!listaEsperando.getItems().contains(nombre))
                listaEsperando.getItems().add(nombre);
        });
    }

    public void clienteAtendido(String nombre) {
        Platform.runLater(() -> {
            listaEsperando.getItems().remove(nombre);
            if (!listaAtendidos.getItems().contains(nombre))
                listaAtendidos.getItems().add(nombre);
        });
    }

    public void clienteSeVa(String nombre) {
        Platform.runLater(() -> {
            listaEsperando.getItems().remove(nombre);
            if (!listaSeFueron.getItems().contains(nombre))
                listaSeFueron.getItems().add(nombre);
        });
    }
}
