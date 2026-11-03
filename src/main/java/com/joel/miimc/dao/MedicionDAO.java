package com.joel.miimc.dao;

import com.joel.miimc.modelo.Medicion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Acceso a datos de la tabla medicion. La clasificación se toma del
 * catálogo clasificacion_imc según el rango en el que cae el IMC.
 */
public class MedicionDAO {

    private static final String SELECT = "SELECT m.id_medicion, m.id_usuario, m.peso, m.estatura, m.imc, "
            + "c.nombre AS clasificacion, m.fecha_medicion FROM medicion m "
            + "JOIN clasificacion_imc c ON c.id_clasificacion = m.id_clasificacion ";

    private final ConexionBD conexionBD = new ConexionBD();

    public boolean guardar(Medicion medicion) {
        String sql = "INSERT INTO medicion (id_usuario, peso, estatura, imc, id_clasificacion) "
                + "VALUES (?, ?, ?, ?, (SELECT id_clasificacion FROM clasificacion_imc "
                + "WHERE ? >= imc_minimo AND (imc_maximo IS NULL OR ? <= imc_maximo)))";
        try (Connection con = conexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, medicion.getIdUsuario());
            ps.setDouble(2, medicion.getPeso());
            ps.setDouble(3, medicion.getEstatura());
            ps.setDouble(4, medicion.getImc());
            ps.setDouble(5, medicion.getImc());
            ps.setDouble(6, medicion.getImc());
            int filas = ps.executeUpdate();
            try (ResultSet llaves = ps.getGeneratedKeys()) {
                if (llaves.next()) {
                    medicion.setIdMedicion(llaves.getInt(1));
                }
            }
            return filas == 1;
        } catch (SQLException e) {
            throw new AccesoDatosException("No se pudo guardar la medición.", e);
        }
    }

    /** Histórico del usuario, de la medición más reciente a la más antigua. */
    public List<Medicion> listarPorUsuario(int idUsuario) {
        String sql = SELECT + "WHERE m.id_usuario = ? ORDER BY m.fecha_medicion DESC, m.id_medicion DESC";
        List<Medicion> lista = new ArrayList<>();
        try (Connection con = conexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idUsuario);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(leer(rs));
                }
            }
            return lista;
        } catch (SQLException e) {
            throw new AccesoDatosException("No se pudo consultar el historial.", e);
        }
    }

    /** Última medición del usuario o null si todavía no tiene. */
    public Medicion obtenerUltima(int idUsuario) {
        List<Medicion> lista = listarPorUsuario(idUsuario);
        return lista.isEmpty() ? null : lista.get(0);
    }

    /** Una medición del usuario por su id, o null si no existe o es de otro usuario. */
    public Medicion buscarPorId(int idUsuario, int idMedicion) {
        String sql = SELECT + "WHERE m.id_usuario = ? AND m.id_medicion = ?";
        try (Connection con = conexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idUsuario);
            ps.setInt(2, idMedicion);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? leer(rs) : null;
            }
        } catch (SQLException e) {
            throw new AccesoDatosException("No se pudo consultar la medición.", e);
        }
    }

    private Medicion leer(ResultSet rs) throws SQLException {
        Medicion m = new Medicion();
        m.setIdMedicion(rs.getInt("id_medicion"));
        m.setIdUsuario(rs.getInt("id_usuario"));
        m.setPeso(rs.getDouble("peso"));
        m.setEstatura(rs.getDouble("estatura"));
        m.setImc(rs.getDouble("imc"));
        m.setClasificacion(rs.getString("clasificacion"));
        m.setFechaMedicion(rs.getTimestamp("fecha_medicion").toLocalDateTime());
        return m;
    }
}
