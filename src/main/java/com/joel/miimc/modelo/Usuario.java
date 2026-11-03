package com.joel.miimc.modelo;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Modelo: representa a un usuario registrado (tabla usuario).
 * Se guarda en la sesión al iniciar sesión, por eso es Serializable.
 */
public class Usuario implements Serializable {

    private static final long serialVersionUID = 1L;

    private int idUsuario;
    private String nombreCompleto;
    private String nombreUsuario;
    private String contrasenaHash;
    private int edad;
    private char sexo;
    private double estatura;
    private LocalDateTime fechaRegistro;

    public Usuario() {
    }

    public Usuario(String nombreCompleto, String nombreUsuario, int edad, char sexo, double estatura) {
        this.nombreCompleto = nombreCompleto;
        this.nombreUsuario = nombreUsuario;
        this.edad = edad;
        this.sexo = sexo;
        this.estatura = estatura;
    }

    /** Primer nombre, para saludar al usuario. */
    public String getPrimerNombre() {
        if (nombreCompleto == null || nombreCompleto.isBlank()) {
            return "";
        }
        return nombreCompleto.trim().split("\\s+")[0];
    }

    /** Sexo como texto ("F" o "M"), para usarlo en las vistas. */
    public String getSexoTexto() {
        return String.valueOf(sexo).trim();
    }

    public int getIdUsuario() { return idUsuario; }
    public void setIdUsuario(int idUsuario) { this.idUsuario = idUsuario; }

    public String getNombreCompleto() { return nombreCompleto; }
    public void setNombreCompleto(String nombreCompleto) { this.nombreCompleto = nombreCompleto; }

    public String getNombreUsuario() { return nombreUsuario; }
    public void setNombreUsuario(String nombreUsuario) { this.nombreUsuario = nombreUsuario; }

    public String getContrasenaHash() { return contrasenaHash; }
    public void setContrasenaHash(String contrasenaHash) { this.contrasenaHash = contrasenaHash; }

    public int getEdad() { return edad; }
    public void setEdad(int edad) { this.edad = edad; }

    public char getSexo() { return sexo; }
    public void setSexo(char sexo) { this.sexo = sexo; }

    public double getEstatura() { return estatura; }
    public void setEstatura(double estatura) { this.estatura = estatura; }

    public LocalDateTime getFechaRegistro() { return fechaRegistro; }
    public void setFechaRegistro(LocalDateTime fechaRegistro) { this.fechaRegistro = fechaRegistro; }
}
