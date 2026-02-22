/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package persistencia.DAOs;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import persistencia.conexion.IConexionBD;
import persistencia.dominio.HistorialEstado;
import persistencia.excepciones.PersistenciaException;

/**
 *
 * @author ERICK
 */
public class HistorialEstadoDAO implements IHistorialEstadoDAO {

    private final IConexionBD conexionBD;
    private static final Logger LOG =
            Logger.getLogger(HistorialEstadoDAO.class.getName());

    public HistorialEstadoDAO(IConexionBD conexionBD) {
        this.conexionBD = conexionBD;
    }

    @Override
    public List<HistorialEstado> obtenerHistorialPorPedido(int idPedido)
            throws PersistenciaException {

        String sql = """
            SELECT id_historial, id_pedido, estado_anterior,
                   estado_nuevo, fecha_cambio
            FROM HISTORIAL_ESTADO
            WHERE id_pedido = ?
            ORDER BY fecha_cambio ASC
            """;

        List<HistorialEstado> lista = new ArrayList<>();

        try (Connection conn = conexionBD.crearConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idPedido);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(extraerHistorial(rs));
                }
            }

            return lista;

        } catch (SQLException ex) {
            LOG.log(Level.SEVERE,
                    "Error al obtener historial del pedido", ex);
            throw new PersistenciaException(
                    "Error al obtener historial del pedido", ex);
        }
    }

    private HistorialEstado extraerHistorial(ResultSet rs)
            throws SQLException {

        HistorialEstado h = new HistorialEstado();

        h.setIdHistorial(rs.getInt("id_historial"));
        h.setIdPedido(rs.getInt("id_pedido"));
        h.setEstadoAnterior(rs.getString("estado_anterior"));
        h.setEstadoNuevo(rs.getString("estado_nuevo"));
        h.setFechaCambio(
                rs.getTimestamp("fecha_cambio").toLocalDateTime()
        );

        return h;
    }
}