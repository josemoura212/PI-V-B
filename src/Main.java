package src;

import java.io.IOException;
import java.util.Random;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        boolean isRunning = true;

        while (true) {
            try {
                if (System.in.available() > 0) {
                    System.in.read(); 
                    System.in.skip(System.in.available()); // Limpa o buffer de entrada
                    isRunning = !isRunning;
                    if (isRunning) {
                        System.out.println("Sistema retomado.");
                    } else {
                        System.out.println("Sistema pausado. Pressione Enter para continuar...");
                        scanner.nextLine(); // Aguarda o usuário pressionar Enter
                    }
                }
            } catch (IOException e) {
                System.err.println("Erro ao acessar a entrada do sistema: " + e.getMessage());
            }

            if (isRunning) {
                double simulatedTemp = SensorSimulation.simulateTemperature();
                int simulatedHumidity = SensorSimulation.simulateHumidityPercentage();

                System.out.printf("Temperatura simulada: %.1f °C%n", simulatedTemp);
                System.out.println("Umidade simulada: " + simulatedHumidity + " % \n");
            }

            try {
                Thread.sleep(1000); // Aguarda 1 segundo antes de gerar novos dados
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}

class SensorSimulation {
    // Variável global para temperatura (em °C)
    private static double currentTemperature = 25.0;
    // Variável global para umidade (valor bruto do sensor, de 0 a 1023)
    private static int currentRawHumidity = 600;

    // Objeto Random para gerar variações aleatórias
    private static Random random = new Random();

    public static double simulateTemperature() {
        // Gera um delta aleatório entre -0,5 e +0,5
        double delta = (random.nextDouble() * 1.0) - 0.5;
        currentTemperature += delta;
        return currentTemperature;
    }

    public static int simulateHumidityPercentage() {

        int deltaRaw = random.nextInt(21) - 10;
        currentRawHumidity += deltaRaw;
        if (currentRawHumidity < 0) currentRawHumidity = 0;
        if (currentRawHumidity > 1023) currentRawHumidity = 1023;

        int humidityPercentage = mapValue(currentRawHumidity, 1023, 0, 100, 0);
        if (humidityPercentage < 0) humidityPercentage = 0;
        if (humidityPercentage > 100) humidityPercentage = 100;

        return humidityPercentage;
    }

    public static int mapValue(int x, int in_min, int in_max, int out_min, int out_max) {
        return (x - in_min) * (out_max - out_min) / (in_max - in_min) + out_min;
    }
}
