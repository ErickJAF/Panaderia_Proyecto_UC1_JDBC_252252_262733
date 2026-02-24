/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package persistencia.DAOs;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import persistencia.dominio.Cupon;
import persistencia.excepciones.PersistenciaException;
import persistencia.conexion.IConexionBD;
/**
 *
 * @author josed
 */
public class CuponDAO implements ICuponDAO {

    private IConexionBD conexion;


    public CuponDAO(IConexionBD conexion) {
        this.conexion = conexion;
    }

    @Override
    public Cupon buscarPorCodigo(String codigo) throws PersistenciaException {
        String sql = "SELECT * FROM CUPON WHERE codigo = ?";
        
        try (Connection conn = conexion.crearConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, codigo);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Cupon cupon = new Cupon();
                cupon.setIdCupon((int) rs.getLong("id_cupon"));
                cupon.setCodigo(rs.getString("codigo"));
                cupon.setDescuento(rs.getBigDecimal("descuento"));
                cupon.setUsosMaximos(rs.getInt("usos_maximos"));
                cupon.setUsosActuales(rs.getInt("usos_actuales"));
                cupon.setActivo(rs.getBoolean("activo"));
                cupon.setFechaInicio(rs.getDate("fecha_inicio").toLocalDate());
                cupon.setFechaFin(rs.getDate("fecha_fin").toLocalDate());

                return cupon;
            }

            return null;

        } catch (SQLException e) {
            throw new PersistenciaException("Error al buscar cupón", e);
        }
    }

    @Override
    public void incrementarUso(Long idCupon) throws PersistenciaException {
        String sql = "UPDATE CUPON SET usos_actuales = usos_actuales + 1 WHERE id_cupon = ?";

        try (Connection conn = conexion.crearConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, idCupon);
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new PersistenciaException("Error al actualizar usos del cupón", e);
        }
    }

    @Override
public Cupon buscarPorId(Long idCupon) throws PersistenciaException {

    String sql = "SELECT * FROM CUPON WHERE id_cupon = ?";

    try (Connection conn = conexion.crearConexion();
         PreparedStatement ps = conn.prepareStatement(sql)) {

        ps.setLong(1, idCupon);

        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            Cupon cupon = new Cupon();
            cupon.setIdCupon((int) rs.getLong("id_cupon"));
            cupon.setCodigo(rs.getString("codigo"));
            cupon.setDescuento(rs.getBigDecimal("descuento"));
            cupon.setUsosMaximos(rs.getInt("usos_maximos"));
            cupon.setUsosActuales(rs.getInt("usos_actuales"));
            cupon.setActivo(rs.getBoolean("activo"));
            cupon.setFechaInicio(rs.getDate("fecha_inicio").toLocalDate());
            cupon.setFechaFin(rs.getDate("fecha_fin").toLocalDate());

            return cupon;
        }

        return null;

    } catch (SQLException e) {
        throw new PersistenciaException("Error al buscar cupón por ID", e);
    }
}
}
