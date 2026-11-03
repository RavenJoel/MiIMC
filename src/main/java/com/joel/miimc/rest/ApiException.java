package com.joel.miimc.rest;

/**
 * Error del servicio REST con su código HTTP (401, 404, etc.).
 */
public class ApiException extends RuntimeException {

    private static final long serialVersionUID = 1L;
    private final int estado;

    public ApiException(int estado, String mensaje) {
        super(mensaje);
        this.estado = estado;
    }

    public int getEstado() {
        return estado;
    }
}
