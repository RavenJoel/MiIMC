package com.joel.miimc.dao;

/**
 * Error al acceder a la base de datos (sin conexión, consulta fallida, etc.).
 * Permite distinguir un problema de la base de datos de un error del usuario.
 */
public class AccesoDatosException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public AccesoDatosException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
