package org.example;

import java.util.*;

class Proceso {
    String nombre;
    int rafagaCPU;
    int tiempoLlegada;
    int prioridad;

    public Proceso(String nombre, int rafagaCPU, int tiempoLlegada, int prioridad) {
        this.nombre = nombre;
        this.rafagaCPU = rafagaCPU;
        this.tiempoLlegada = tiempoLlegada;
        this.prioridad = prioridad;
    }
}

public class PlanificacionProcesos {

    // FIFO
    public static void fifo(Proceso[] procesos) {
        int tiempoActual = 0, tiempoEsperaTotal = 0, tiempoRetornoTotal = 0;
        System.out.println("FIFO:");
        for (Proceso p : procesos) {
            int tiempoEspera = tiempoActual - p.tiempoLlegada;
            int tiempoRetorno = tiempoEspera + p.rafagaCPU;
            tiempoEsperaTotal += tiempoEspera;
            tiempoRetornoTotal += tiempoRetorno;
            tiempoActual += p.rafagaCPU;
            System.out.println(p.nombre + " -> Espera: " + tiempoEspera + ", Retorno: " + tiempoRetorno);
        }
        System.out.println("Tiempo medio de espera: " + (double) tiempoEsperaTotal / procesos.length);
        System.out.println("Tiempo medio de retorno: " + (double) tiempoRetornoTotal / procesos.length);
    }

    // SJF (No Apropietivo)
    public static void sjf(Proceso[] procesos) {
        Arrays.sort(procesos, Comparator.comparingInt(p -> p.rafagaCPU));
        fifo(procesos);
    }

    // Round Robin
    public static void roundRobin(Proceso[] procesos, int quantum) {
        int tiempoActual = 0, tiempoEsperaTotal = 0, tiempoRetornoTotal = 0;
        Queue<Proceso> cola = new LinkedList<>(Arrays.asList(procesos));
        int[] tiemposRestantes = Arrays.stream(procesos).mapToInt(p -> p.rafagaCPU).toArray();
        int[] tiemposInicio = new int[procesos.length];

        System.out.println("Round Robin:");
        while (!cola.isEmpty()) {
            Proceso p = cola.poll();
            int index = Arrays.asList(procesos).indexOf(p);
            if (tiemposInicio[index] == 0) {
                tiemposInicio[index] = Math.max(tiempoActual, p.tiempoLlegada);
            }
            if (tiemposRestantes[index] > quantum) {
                tiempoActual += quantum;
                tiemposRestantes[index] -= quantum;
                cola.add(p);
            } else {
                tiempoActual += tiemposRestantes[index];
                int tiempoEspera = tiemposInicio[index] - p.tiempoLlegada;
                int tiempoRetorno = tiempoActual - p.tiempoLlegada;
                tiempoEsperaTotal += tiempoEspera;
                tiempoRetornoTotal += tiempoRetorno;
                System.out.println(p.nombre + " -> Espera: " + tiempoEspera + ", Retorno: " + tiempoRetorno);
            }
        }
        System.out.println("Tiempo medio de espera: " + (double) tiempoEsperaTotal / procesos.length);
        System.out.println("Tiempo medio de retorno: " + (double) tiempoRetornoTotal / procesos.length);
    }

    // Prioridad
    public static void prioridad(Proceso[] procesos) {
        Arrays.sort(procesos, Comparator.comparingInt(p -> p.prioridad));
        fifo(procesos);
    }

    public static void main(String[] args) {
        Proceso[] procesosFIFO = {
                new Proceso("A", 3, 2, 2),
                new Proceso("B", 1, 4, 3),
                new Proceso("C", 3, 0, 1),
                new Proceso("D", 4, 1, 3),
                new Proceso("E", 2, 3, 4)
        };

        Proceso[] procesosSJF = {
                new Proceso("A", 3, 2, 2),
                new Proceso("B", 1, 4, 3),
                new Proceso("C", 3, 0, 1),
                new Proceso("D", 4, 1, 3),
                new Proceso("E", 2, 3, 4)
        };

        Proceso[] procesosRoundRobin = {
                new Proceso("A", 3, 2, 2),
                new Proceso("B", 1, 4, 3),
                new Proceso("C", 3, 0, 1),
                new Proceso("D", 4, 1, 3),
                new Proceso("E", 2, 3, 4)
        };

        Proceso[] procesosPrioridad = {
                new Proceso("A", 3, 2, 2),
                new Proceso("B", 1, 4, 3),
                new Proceso("C", 3, 0, 1),
                new Proceso("D", 4, 1, 3),
                new Proceso("E", 2, 3, 4)
        };

        // Ejecuciones
        fifo(procesosFIFO);
        System.out.println();
        sjf(procesosSJF);
        System.out.println();
        roundRobin(procesosRoundRobin, 3); // Quantum de 3 unidades de tiempo
        System.out.println();
        prioridad(procesosPrioridad);
    }
}

