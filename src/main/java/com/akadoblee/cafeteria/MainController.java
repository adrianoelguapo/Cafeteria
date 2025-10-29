package com.akadoblee.cafeteria;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.util.*;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class MainController {

    @FXML private Button btnIniciar;
    @FXML private ListView<String> listaEsperando;
    @FXML private ListView<String> listaAtendidos;
    @FXML private ListView<String> listaSeFueron;
    @FXML private Label lblEstado;

    Queue<Cliente> colaClientes = new ConcurrentLinkedQueue<>();
    volatile boolean monitorRunning = false;

    @FXML
    private void iniciarSimulacion() {
        btnIniciar.setDisable(true);
        listaEsperando.getItems().clear();
        listaAtendidos.getItems().clear();
        listaSeFueron.getItems().clear();
        lblEstado.setText("Simulación en curso...");

        new Thread(() -> {
            Camarero c1 = new Camarero("Camarero 1", colaClientes);
            Camarero c2 = new Camarero("Camarero 2", colaClientes);

            Cliente[] clientes = {
                new Cliente("Ana", 3000, colaClientes),
                new Cliente("Luis", 5000, colaClientes),
                new Cliente("Marta", 2000, colaClientes),
                new Cliente("Carlos", 4000, colaClientes),
                new Cliente("Sofía", 6000, colaClientes)
            };

            c1.start();
            c2.start();

            monitorRunning = true;
            Thread monitor = new Thread(() -> {
                while (monitorRunning) {
                    List<String> names = new ArrayList<>();
                    for (Cliente cl : colaClientes) {
                        names.add(cl.nombre);
                    }
                    Platform.runLater(() -> listaEsperando.getItems().setAll(names));
                    try { Thread.sleep(200); } catch (InterruptedException ignored) {}
                }
            });
            monitor.setDaemon(true);
            monitor.start();

            // iniciar clientes
            for (Cliente cliente : clientes) {
                cliente.start();
            }

            // esperar fin de todos los clientes
            for (Cliente cliente : clientes) {
                try { cliente.join(); } catch (InterruptedException ignored) {}
            }

            monitorRunning = false;
            try { monitor.join(); } catch (InterruptedException ignored) {}

            // actualizar listas finales
            for (Cliente cliente : clientes) {
                if (cliente.atendido) {
                    Platform.runLater(() -> {
                        listaEsperando.getItems().remove(cliente.nombre);
                        listaAtendidos.getItems().add(cliente.nombre);
                    });
                } else {
                    Platform.runLater(() -> {
                        listaEsperando.getItems().remove(cliente.nombre);
                        listaSeFueron.getItems().add(cliente.nombre);
                    });
                }
            }

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
}
