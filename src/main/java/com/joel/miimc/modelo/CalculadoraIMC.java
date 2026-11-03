package com.joel.miimc.modelo;

/**
 * Modelo (lógica de negocio): fórmula del IMC y clasificación de la OMS.
 */
public class CalculadoraIMC {

    /**
     * IMC = peso (kg) / estatura² (m), redondeado a un decimal.
     */
    public double calcular(double peso, double estatura) {
        if (peso <= 0 || estatura <= 0) {
            throw new IllegalArgumentException("El peso y la estatura deben ser mayores que cero.");
        }
        double imc = peso / (estatura * estatura);
        return Math.round(imc * 10.0) / 10.0;
    }

    /**
     * Clasificación de la OMS para adultos. Usa los mismos rangos
     * que la tabla clasificacion_imc de la base de datos.
     */
    public String clasificar(double imc) {
        if (imc < 18.5) {
            return "Bajo peso";
        } else if (imc < 25.0) {
            return "Peso normal";
        } else if (imc < 30.0) {
            return "Sobrepeso";
        } else if (imc < 35.0) {
            return "Obesidad grado I";
        } else if (imc < 40.0) {
            return "Obesidad grado II";
        }
        return "Obesidad grado III";
    }
}
