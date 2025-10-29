package com.akadoblee.cafeteria;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Main{
    static Queue<Cliente> colaClientes = new ConcurrentLinkedQueue<>();
    static boolean todosLlegaron = false;

    public static void main (String[] args){

        // Crear camareros
        Camarero c1 = new Camarero("Camarero 1", colaClientes);
        Camarero c2 = new Camarero("Camarero 1", colaClientes);

        // Crear clientes
        Cliente[] clientes = {
            new Cliente("Ana",3000,colaClientes),
            new Cliente("Luis",5000,colaClientes),
            new Cliente("Marta",2000,colaClientes),
            new Cliente("Carlos",4000,colaClientes),
            new Cliente("Sofia",6000,colaClientes),
        };

        // Iniciar camareros
        c1.start();
        c2.start();

        // Esperar a que todos los clientes terminen
        for (Cliente cliente : clientes){
            try {
                cliente.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // Marcar que ya no llegarán más clientes
        todosLlegaron = true;

        // Esperar a que todos los camareros terminen
        try{
            c1.join();
            c2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("\nTodos los clientes fueron atendidos o se fueron.");
    }
}