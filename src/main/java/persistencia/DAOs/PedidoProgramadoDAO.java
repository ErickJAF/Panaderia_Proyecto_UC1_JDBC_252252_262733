/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package persistencia.DAOs;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import persistencia.conexion.IConexionBD;
import persistencia.dominio.PedidoProgramado;
import persistencia.excepciones.PersistenciaException;
import java.sql.CallableStatement;
import negocio.DTOs.DetallePedidoDTO;
import negocio.DTOs.PedidoProgramadoDTO;

/**
 *
 * @author ERICK
 */
public class PedidoProgramadoDAO implements IPedidoProgramadoDAO {
    private final IConexionBD conexionBD;
    private Integer idCupon;
    private String codigoCupon;
    private static final Logger LOG =
            Logger.getLogger(PedidoProgramadoDAO.class.getName());

    public PedidoProgramadoDAO(IConexionBD conexionBD) {
        this.conexionBD = conexionBD;
    }
    
    

    @Override
    public List<PedidoProgramado> obtenerActivosPorCliente(int idCliente)
            throws PersistenciaException {

        String sql = """
                SELECT p.*, pr.id_cliente, pr.id_cupon
                FROM PEDIDO p
                JOIN PROGRAMADO pr ON p.id_pedido = pr.id_pedido
                WHERE pr.id_cliente = ? AND p.estado = 'Pendiente'
                """;

        List<PedidoProgramado> lista = new ArrayList<>();

        try (Connection conn = conexionBD.crearConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idCliente);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(extraerPedido(rs));
                }
            }

            return lista;

        } catch (SQLException ex) {
            LOG.log(Level.SEVERE,
                    "Error al obtener pedidos activos", ex);
            throw new PersistenciaException(
                    "Error al obtener pedidos activos", ex);
        }
    }

    @Override
    public List<PedidoProgramado> obtenerPendientes() throws PersistenciaException {
        String sql = """
            SELECT p.*, pr.id_cliente, pr.id_cupon
            FROM PEDIDO p
            JOIN PROGRAMADO pr ON p.id_pedido = pr.id_pedido
            WHERE p.estado = 'Pendiente'
        """;

        List<PedidoProgramado> lista = new ArrayList<>();
        try (Connection conn = conexionBD.crearConexion();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(extraerPedido(rs));
            }
            return lista;

        } catch (SQLException ex) {
            throw new PersistenciaException("Error al obtener pedidos pendientes", ex);
        }
    }
    
    @Override
    public void insertar(PedidoProgramado pedido) throws PersistenciaException {
        String sql = "{CALL sp_crear_pedido_programado(?,?,?,?,?,?,?,?,?)}";

        try (Connection conn = conexionBD.crearConexion();
             CallableStatement cs = conn.prepareCall(sql)) {

            cs.setDate(1, java.sql.Date.valueOf(pedido.getFechaCreacion()));
            cs.setString(2, pedido.getEstado());
            cs.setDouble(3, pedido.getSubtotal());
            cs.setDouble(4, pedido.getDescuento());
            cs.setDouble(5, pedido.getTotal());
            cs.setInt(6, pedido.getIdEmpleado());
            cs.setInt(7, pedido.getIdCliente());

            if (pedido.getIdCupon() != null) {
                cs.setInt(8, pedido.getIdCupon());
            } else {
                cs.setNull(8, Types.INTEGER);
            }

            cs.registerOutParameter(9, Types.INTEGER);

            cs.execute();

            int idGenerado = cs.getInt(9);
            pedido.setIdPedido(idGenerado);

            LOG.info("Pedido programado insertado correctamente. ID: " + idGenerado);

        } catch (SQLException e) {
            LOG.log(Level.SEVERE, "Error al insertar pedido programado", e);
            throw new PersistenciaException("Error al insertar pedido programado", e);
        }
    }

    @Override
    public void actualizarEstado(int idPedido, String estado) throws PersistenciaException {
        String sql = "UPDATE PEDIDO SET estado = ? WHERE id_pedido = ?";

        try (Connection conn = conexionBD.crearConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, estado);
            ps.setInt(2, idPedido);

            int filasAfectadas = ps.executeUpdate();
            if (filasAfectadas == 0) {
                throw new PersistenciaException("No se encontró el pedido para actualizar");
            }

            LOG.info("Estado del pedido programado actualizado correctamente. ID: " + idPedido + ", Nuevo Estado: " + estado);

        } catch (SQLException ex) {
            LOG.log(Level.SEVERE, "Error al actualizar estado del pedido programado. ID: " + idPedido, ex);
            throw new PersistenciaException("Error al actualizar estado del pedido programado", ex);
        }
    }

    @Override
    public PedidoProgramado buscarPorId(int idPedido) throws PersistenciaException {
        String sql = """
            SELECT p.id_pedido, p.fecha_creacion, p.estado, p.total, pr.id_cliente
            FROM PEDIDO p
            JOIN PROGRAMADO pr ON p.id_pedido = pr.id_pedido
            WHERE p.id_pedido = ?
        """;

        try (Connection conn = conexionBD.crearConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idPedido);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    PedidoProgramado pedido = new PedidoProgramado();
                    pedido.setIdPedido(rs.getInt("id_pedido"));
                    pedido.setFechaCreacion(rs.getDate("fecha_creacion").toLocalDate());
                    pedido.setEstado(rs.getString("estado"));
                    pedido.setTotal(rs.getDouble("total"));
                    pedido.setIdCliente(rs.getInt("id_cliente"));

                    LOG.info("Pedido programado encontrado. ID: " + idPedido + ", Estado: " + pedido.getEstado());
                    return pedido;
                } else {
                    LOG.warning("No se encontró pedido programado con ID: " + idPedido);
                    return null;
                }
            }

        } catch (SQLException ex) {
            LOG.log(Level.SEVERE, "Error al buscar pedido programado por ID: " + idPedido, ex);
            throw new PersistenciaException("Error al buscar pedido programado por ID", ex);
        }
    }
    
    private PedidoProgramado extraerPedido(ResultSet rs) throws SQLException {

        PedidoProgramado p = new PedidoProgramado();

        p.setIdPedido(rs.getInt("id_pedido"));
        p.setFechaCreacion(
                rs.getDate("fecha_creacion").toLocalDate());
        p.setEstado(rs.getString("estado"));
        p.setSubtotal(rs.getDouble("subtotal"));
        p.setDescuento(rs.getFloat("descuento"));
        p.setTotal(rs.getDouble("total"));
        p.setIdEmpleado(rs.getInt("id_empleado"));
        p.setIdCliente(rs.getInt("id_cliente"));

        int idCupon = rs.getInt("id_cupon");
        p.setIdCupon(rs.wasNull() ? null : idCupon);

        return p;
    }

    @Override
    public PedidoProgramadoDTO buscarPorIdDTO(int idPedido) throws PersistenciaException {

        String sql = """
            SELECT 
                pr.id_cliente,
                p.id_empleado,
                p.subtotal,
                p.descuento,
                p.total,
                pr.id_cupon
            FROM PEDIDO p
            JOIN PROGRAMADO pr ON p.id_pedido = pr.id_pedido
            WHERE p.id_pedido = ?
        """;

        try (Connection conn = conexionBD.crearConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idPedido);

            try (ResultSet rs = ps.executeQuery()) {

                if (!rs.next()) {
                    return null;
                }

                int idCliente = rs.getInt("id_cliente");
                int idEmpleado = rs.getInt("id_empleado");
                double subtotal = rs.getDouble("subtotal");
                double descuento = rs.getDouble("descuento");
                double total = rs.getDouble("total");

                Integer idCupon = rs.getObject("id_cupon") != null
                        ? rs.getInt("id_cupon")
                        : null;

                List<DetallePedidoDTO> detalles = obtenerDetalles(conn, idPedido);

                return new PedidoProgramadoDTO(
                        idCliente,
                        idEmpleado,
                        subtotal,
                        descuento,
                        total,
                        idCupon,
                        detalles
                );
            }

        } catch (SQLException ex) {
            throw new PersistenciaException("Error al buscar PedidoProgramadoDTO", ex);
        }
    }
    
    private List<DetallePedidoDTO> obtenerDetalles(Connection conn, int idPedido) throws SQLException {

        String sql = """
            SELECT id_producto,
                   cantidad,
                   precio_unitario,
                   nota
            FROM DETALLE_PEDIDO
            WHERE id_pedido = ?
        """;

        List<DetallePedidoDTO> lista = new ArrayList<>();

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idPedido);

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {

                    DetallePedidoDTO detalle = new DetallePedidoDTO(
                            rs.getInt("id_producto"),
                            rs.getInt("cantidad"),
                            rs.getDouble("precio_unitario"),
                            rs.getString("nota")
                    );

                    lista.add(detalle);
                }
            }
        }

        return lista;
    }
    
    @Override
    public List<PedidoProgramadoDTO> obtenerPorCliente(int idCliente)
            throws PersistenciaException {

        String sql = """
            SELECT p.id_pedido,
                   p.fecha_creacion,
                   p.estado,
                   p.subtotal,
                   p.descuento,
                   p.total
            FROM PEDIDO p
            JOIN PROGRAMADO pr ON p.id_pedido = pr.id_pedido
            WHERE pr.id_cliente = ?
            ORDER BY p.fecha_creacion DESC
        """;

        List<PedidoProgramadoDTO> lista = new ArrayList<>();

        try (Connection conn = conexionBD.crearConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idCliente);

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {

                    PedidoProgramadoDTO dto = new PedidoProgramadoDTO();

                    dto.setIdPedido(rs.getInt("id_pedido"));
                    dto.setFechaCreacion(
                            rs.getDate("fecha_creacion").toLocalDate()
                    );
                    dto.setEstado(rs.getString("estado"));
                    dto.setSubtotal(rs.getDouble("subtotal"));
                    dto.setDescuento(rs.getDouble("descuento"));
                    dto.setTotal(rs.getDouble("total"));

                    lista.add(dto);
                }
            }

            return lista;

        } catch (SQLException e) {
            throw new PersistenciaException("Error al obtener historial", e);
        }
    }
    
    @Override
    public int contarPedidosPorCliente(int idCliente) throws PersistenciaException {
        String sql = """
            SELECT COUNT(*) AS total_pedidos
            FROM PROGRAMADO pr
            JOIN PEDIDO p ON pr.id_pedido = p.id_pedido
            WHERE pr.id_cliente = ?
            AND p.estado IN ('Pendiente', 'Listo');
        """;

        try (Connection conexion = conexionBD.crearConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, idCliente);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("total_pedidos");
                }
            }

            return 0;

        } catch (SQLException e) {
            throw new PersistenciaException("Error al contar pedidos pendientes o listos del cliente", e);
        }
    }
}
