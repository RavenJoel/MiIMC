package com.joel.miimc.modelo;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Modelo: reglas de validación de la aplicación.
 * Las mismas reglas están en la base de datos como restricciones CHECK.
 */
public class Validador {

    public static final double ESTATURA_MINIMA = 1.00;
    public static final double ESTATURA_MAXIMA = 2.50;
    public static final int EDAD_MINIMA = 15;
    public static final int EDAD_MAXIMA = 120;
    public static final double PESO_MAXIMO = 999.99;
    private static final Pattern USUARIO = Pattern.compile("[A-Za-z0-9._]{4,30}");

    public boolean estaturaValida(double estatura) {
        return estatura >= ESTATURA_MINIMA && estatura <= ESTATURA_MAXIMA;
    }

    public boolean edadValida(int edad) {
        return edad >= EDAD_MINIMA && edad <= EDAD_MAXIMA;
    }

    public boolean pesoValido(double peso) {
        return peso > 0 && peso <= PESO_MAXIMO;
    }

    /**
     * Revisa todos los datos del registro y regresa la lista de errores
     * (vacía si todo es correcto).
     */
    public List<String> validarRegistro(Usuario usuario, String contrasena, String confirmacion) {
        List<String> errores = new ArrayList<>();
        String nombre = usuario.getNombreCompleto();
        if (nombre == null || nombre.isBlank() || nombre.length() > 100) {
            errores.add("Escribe tu nombre completo (máximo 100 caracteres).");
        }
        String nombreUsuario = usuario.getNombreUsuario();
        if (nombreUsuario == null || !USUARIO.matcher(nombreUsuario).matches()) {
            errores.add("El nombre de usuario debe tener de 4 a 30 caracteres: letras, números, punto o guion bajo.");
        }
        if (contrasena == null || contrasena.length() < 8) {
            errores.add("La contraseña debe tener al menos 8 caracteres.");
        } else if (!contrasena.equals(confirmacion)) {
            errores.add("Las contraseñas no coinciden.");
        }
        if (!edadValida(usuario.getEdad())) {
            errores.add("La edad mínima es de " + EDAD_MINIMA + " años.");
        }
        if (usuario.getSexo() != 'F' && usuario.getSexo() != 'M') {
            errores.add("Selecciona tu sexo.");
        }
        if (!estaturaValida(usuario.getEstatura())) {
            errores.add("La estatura debe estar entre 1.00 y 2.50 metros.");
        }
        return errores;
    }
}
