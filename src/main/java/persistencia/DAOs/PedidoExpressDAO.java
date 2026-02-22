/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package persistencia.DAOs;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import negocio.DTOs.PedidoExpressDTO;
import persistencia.conexion.IConexionBD;
import persistencia.dominio.PedidoExpress;
import persistencia.excepciones.PersistenciaException;

/**
 *
 * @author ERICK
 */
public class PedidoExpressDAO implements IPedidoExpressDAO {

    private final IConexionBD conexionBD;
    private static final Logger LOG = Logger.getLogger(PedidoExpressDAO.class.getName());

    public PedidoExpressDAO(IConexionBD conexionBD) {
        this.conexionBD = conexionBD;
    }

    @Override
    public void insertar(PedidoExpress pedido) throws PersistenciaException {
        String sqlPedido = "INSERT INTO PEDIDO (fecha_creacion, estado, subtotal, descuento, total, id_empleado) "
                         + "VALUES (?, ?, ?, ?, ?, ?)";
        String sqlExpress = "INSERT INTO EXPRESS (id_pedido, folio, pin_seguridad, fecha_listo) "
                          + "VALUES (?, ?, ?, ?)";

        try (Connection conn = conexionBD.crearConexion()) {
            conn.setAutoCommit(false);

            try (PreparedStatement ps = conn.prepareStatement(sqlPedido, Statement.RETURN_GENERATED_KEYS)) {
                ps.setDate(1, java.sql.Date.valueOf(pedido.getFechaCreacion()));
                ps.setString(2, pedido.getEstado());
                ps.setDouble(3, 0);
                ps.setDouble(4, 0);
                ps.setDouble(5, pedido.getTotal());
                ps.setInt(6, 0);

                ps.executeUpdate();

                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    int idGenerado = rs.getInt(1);
                    pedido.setIdPedido(idGenerado);
                } else {
                    throw new PersistenciaException("No se pudo generar el ID del pedido");
                }
            }

            try (PreparedStatement psExp = conn.prepareStatement(sqlExpress)) {
                psExp.setInt(1, pedido.getIdPedido());
                psExp.setInt(2, Integer.parseInt(pedido.getFolio())); // si folio es String, convertir
                psExp.setString(3, pedido.getPinEncriptado());
                psExp.setTimestamp(4, Timestamp.valueOf(pedido.getFechaListo()));

                psExp.executeUpdate();
            }

            conn.commit();
            LOG.info("Pedido express insertado correctamente. ID: " + pedido.getIdPedido());

        } catch (SQLException ex) {
            LOG.log(Level.SEVERE, "Error al insertar pedido express", ex);
            throw new PersistenciaException("Error al insertar pedido express", ex);
        }
    }

    @Override
    public void actualizarEstado(int idPedido, String estado) throws PersistenciaException {
        String sql = "UPDATE PEDIDO SET estado = ? WHERE id_pedido = ?";

        try (Connection conn = conexionBD.crearConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, estado);
            ps.setInt(2, idPedido);

            if (ps.executeUpdate() == 0) {
                throw new PersistenciaException("No se encontró el pedido para actualizar");
            }

            LOG.info("Estado del pedido express actualizado a: " + estado);

        } catch (SQLException ex) {
            LOG.log(Level.SEVERE, "Error al actualizar estado del pedido express", ex);
            throw new PersistenciaException("Error al actualizar estado del pedido express", ex);
        }
    }

    @Override
    public PedidoExpress buscarPorId(int idPedido) throws PersistenciaException {
        String sql = "SELECT p.id_pedido, p.fecha_creacion, p.estado, p.total, "
                   + "e.folio, e.pin_seguridad, e.fecha_listo "
                   + "FROM PEDIDO p "
                   + "JOIN EXPRESS e ON p.id_pedido = e.id_pedido "
                   + "WHERE p.id_pedido = ?";

        try (Connection conn = conexionBD.crearConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idPedido);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    PedidoExpress p = new PedidoExpress();
                    p.setIdPedido(rs.getInt("id_pedido"));
                    p.setFechaCreacion(rs.getTimestamp("fecha_creacion").toLocalDateTime().toLocalDate());
                    p.setEstado(rs.getString("estado"));
                    p.setTotal(rs.getDouble("total"));
                    p.setFolio(String.valueOf(rs.getInt("folio")));
                    p.setPinEncriptado(rs.getString("pin_seguridad"));

                    // ✅ Manejo seguro de fechaListo
                    Timestamp ts = rs.getTimestamp("fecha_listo");
                    if (ts != null) {
                        p.setFechaListo(ts.toLocalDateTime());
                    } else {
                        p.setFechaListo(null);
                    }

                    return p;
                } else {
                    return null;
                }
            }

        } catch (SQLException ex) {
            LOG.log(Level.SEVERE, "Error al buscar pedido express por ID", ex);
            throw new PersistenciaException("Error al buscar pedido express por ID", ex);
        }
    }

    @Override
    public List<PedidoExpressDTO> obtenerPendientes() throws PersistenciaException {
        String sql = """
            SELECT e.id_pedido, e.folio, e.pin_seguridad, c.nombre_completo, t.numero,
                   p.subtotal, p.total, p.estado, p.fecha_creacion
            FROM PEDIDO p
            JOIN EXPRESS e ON p.id_pedido = e.id_pedido
            LEFT JOIN PROGRAMADO pr ON e.id_pedido = pr.id_pedido
            LEFT JOIN CLIENTE c ON pr.id_cliente = c.id_cliente
            LEFT JOIN TELEFONO t ON c.id_cliente = t.id_cliente
            WHERE p.estado = 'Pendiente'
        """;

        List<PedidoExpressDTO> lista = new ArrayList<>();
        try (Connection conn = conexionBD.crearConexion();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                PedidoExpressDTO dto = new PedidoExpressDTO();
                dto.setIdPedido(rs.getInt("id_pedido"));
                dto.setFolio(rs.getInt("folio"));
                dto.setPinEncriptado(rs.getString("pin_seguridad"));
                dto.setNombreCliente(rs.getString("nombre_completo"));
                dto.setTelefonoCliente(rs.getString("numero"));
                dto.setSubtotal(rs.getDouble("subtotal"));
                dto.setTotal(rs.getDouble("total"));
                dto.setEstado(rs.getString("estado"));
                dto.setFechaCreacion(rs.getTimestamp("fecha_creacion").toLocalDateTime());
                lista.add(dto);
            }

            return lista;

        } catch (SQLException ex) {
            throw new PersistenciaException("Error al obtener pedidos express pendientes", ex);
        }
    }

    @Override
    public void actualizarFechaListo(int idPedido, LocalDateTime fecha) throws PersistenciaException {
        String sql = "UPDATE EXPRESS SET fecha_listo = ? WHERE id_pedido = ?";

        try (Connection conn = conexionBD.crearConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setTimestamp(1, Timestamp.valueOf(fecha));
            ps.setInt(2, idPedido);

            if (ps.executeUpdate() == 0) {
                throw new PersistenciaException("No se encontró el pedido para actualizar fechaListo");
            }

            LOG.info("FechaListo del pedido express actualizada correctamente.");

        } catch (SQLException ex) {
            LOG.log(Level.SEVERE, "Error al actualizar fechaListo", ex);
            throw new PersistenciaException("Error al actualizar fechaListo", ex);
        }
    }
}
