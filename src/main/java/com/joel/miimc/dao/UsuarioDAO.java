package com.joel.miimc.dao;

import com.joel.miimc.modelo.Usuario;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;

/**
 * Acceso a datos de la tabla usuario.
 */
public class UsuarioDAO {

    private final ConexionBD conexionBD = new ConexionBD();

    /**
     * Inserta al usuario y le asigna su id. Regresa false si el nombre de usuario ya existe.
     */
    public boolean registrar(Usuario usuario) {
        String sql = "INSERT INTO usuario (nombre_completo, nombre_usuario, contrasena_hash, edad, sexo, estatura) "
                + "VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection con = conexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, usuario.getNombreCompleto());
            ps.setString(2, usuario.getNombreUsuario());
            ps.setString(3, usuario.getContrasenaHash());
            ps.setInt(4, usuario.getEdad());
            ps.setString(5, String.valueOf(usuario.getSexo()));
            ps.setDouble(6, usuario.getEstatura());
            ps.executeUpdate();
            try (ResultSet llaves = ps.getGeneratedKeys()) {
                if (llaves.next()) {
                    usuario.setIdUsuario(llaves.getInt(1));
                }
            }
            return true;
        } catch (SQLIntegrityConstraintViolationException e) {
            return false; // nombre_usuario duplicado (UNIQUE)
        } catch (SQLException e) {
            throw new AccesoDatosException("No se pudo registrar al usuario.", e);
        }
    }

    public boolean existeNombreUsuario(String nombreUsuario) {
        String sql = "SELECT 1 FROM usuario WHERE nombre_usuario = ?";
        try (Connection con = conexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, nombreUsuario);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            throw new AccesoDatosException("No se pudo consultar el nombre de usuario.", e);
        }
    }

    /** Regresa el usuario o null si no existe. */
    public Usuario buscarPorNombreUsuario(String nombreUsuario) {
        String sql = "SELECT id_usuario, nombre_completo, nombre_usuario, contrasena_hash, edad, sexo, "
                + "estatura, fecha_registro FROM usuario WHERE nombre_usuario = ?";
        try (Connection con = conexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, nombreUsuario);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    return null;
                }
                Usuario u = new Usuario();
                u.setIdUsuario(rs.getInt("id_usuario"));
                u.setNombreCompleto(rs.getString("nombre_completo"));
                u.setNombreUsuario(rs.getString("nombre_usuario"));
                u.setContrasenaHash(rs.getString("contrasena_hash"));
                u.setEdad(rs.getInt("edad"));
                u.setSexo(rs.getString("sexo").charAt(0));
                u.setEstatura(rs.getDouble("estatura"));
                u.setFechaRegistro(rs.getTimestamp("fecha_registro").toLocalDateTime());
                return u;
            }
        } catch (SQLException e) {
            throw new AccesoDatosException("No se pudo consultar al usuario.", e);
        }
    }
}
