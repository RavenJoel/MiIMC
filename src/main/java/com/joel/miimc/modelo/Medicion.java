package com.joel.miimc.modelo;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Modelo: una medición del IMC de un usuario (tabla medicion).
 */
public class Medicion implements Serializable {

    private static final long serialVersionUID = 1L;

    private int idMedicion;
    private int idUsuario;
    private double peso;
    private double estatura;
    private double imc;
    private String clasificacion;
    private LocalDateTime fechaMedicion;

    public Medicion() {
    }

    public Medicion(int idUsuario, double peso, double estatura, double imc) {
        this.idUsuario = idUsuario;
        this.peso = peso;
        this.estatura = estatura;
        this.imc = imc;
    }

    public int getIdMedicion() { return idMedicion; }
    public void setIdMedicion(int idMedicion) { this.idMedicion = idMedicion; }

    public int getIdUsuario() { return idUsuario; }
    public void setIdUsuario(int idUsuario) { this.idUsuario = idUsuario; }

    public double getPeso() { return peso; }
    public void setPeso(double peso) { this.peso = peso; }

    public double getEstatura() { return estatura; }
    public void setEstatura(double estatura) { this.estatura = estatura; }

    public double getImc() { return imc; }
    public void setImc(double imc) { this.imc = imc; }

    public String getClasificacion() { return clasificacion; }
    public void setClasificacion(String clasificacion) { this.clasificacion = clasificacion; }

    public LocalDateTime getFechaMedicion() { return fechaMedicion; }
    public void setFechaMedicion(LocalDateTime fechaMedicion) { this.fechaMedicion = fechaMedicion; }
}
