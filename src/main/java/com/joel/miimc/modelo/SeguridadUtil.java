package com.joel.miimc.modelo;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

/**
 * Modelo: cifrado de contraseñas con PBKDF2 (SHA-256) y una sal aleatoria.
 * En la base de datos se guarda "iteraciones:sal:hash", nunca la contraseña.
 */
public class SeguridadUtil {

    private static final String ALGORITMO = "PBKDF2WithHmacSHA256";
    private static final int ITERACIONES = 210_000;
    private static final int BYTES_SAL = 16;
    private static final int BITS_HASH = 256;
    private static final SecureRandom ALEATORIO = new SecureRandom();

    public String generarHash(String contrasena) {
        byte[] sal = new byte[BYTES_SAL];
        ALEATORIO.nextBytes(sal);
        byte[] hash = pbkdf2(contrasena.toCharArray(), sal, ITERACIONES);
        Base64.Encoder b64 = Base64.getEncoder();
        return ITERACIONES + ":" + b64.encodeToString(sal) + ":" + b64.encodeToString(hash);
    }

    public boolean verificar(String contrasena, String hashGuardado) {
        if (contrasena == null || hashGuardado == null) {
            return false;
        }
        String[] partes = hashGuardado.split(":");
        if (partes.length != 3) {
            return false;
        }
        int iteraciones = Integer.parseInt(partes[0]);
        byte[] sal = Base64.getDecoder().decode(partes[1]);
        byte[] esperado = Base64.getDecoder().decode(partes[2]);
        byte[] calculado = pbkdf2(contrasena.toCharArray(), sal, iteraciones);
        // Comparación en tiempo constante
        return MessageDigest.isEqual(esperado, calculado);
    }

    private byte[] pbkdf2(char[] contrasena, byte[] sal, int iteraciones) {
        try {
            PBEKeySpec spec = new PBEKeySpec(contrasena, sal, iteraciones, BITS_HASH);
            return SecretKeyFactory.getInstance(ALGORITMO).generateSecret(spec).getEncoded();
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new IllegalStateException("No se pudo cifrar la contraseña", e);
        }
    }
}
