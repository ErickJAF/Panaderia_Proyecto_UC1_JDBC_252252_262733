package persistencia.DAOs;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import negocio.DTOs.DetallePedidoDTO;
import negocio.DTOs.PedidoExpressDTO;
import persistencia.conexion.IConexionBD;
import persistencia.excepciones.PersistenciaException;

/**
 * @author Isaias
 */
public class PedidoExpressDAO implements IPedidoExpressDAO {

    private final IConexionBD conexionBD;

    public PedidoExpressDAO(IConexionBD conexionBD) {
        this.conexionBD = conexionBD;
    }

    // 🔹 NUEVO: Obtener empleado automáticamente
    @Override
    public int obtenerEmpleadoDisponible() throws PersistenciaException {

        String sql = "SELECT id_empleado FROM EMPLEADO LIMIT 1";

        try (Connection conn = conexionBD.crearConexion();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return rs.getInt("id_empleado");
            }

            return 0;

        } catch (SQLException e) {
            throw new PersistenciaException(
                    "Error al obtener empleado disponible", e);
        }
    }

    @Override
    public int obtenerSiguienteFolio() throws PersistenciaException {

        String sql =
                "SELECT IFNULL(MAX(folio),0)+1 AS siguiente FROM EXPRESS";

        try (Connection conn = conexionBD.crearConexion();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) return rs.getInt("siguiente");
            return 1;

        } catch (SQLException e) {
            throw new PersistenciaException(
                    "Error al obtener folio", e);
        }
    }

    @Override
    public void insertar(PedidoExpressDTO pedido,
                         int idEmpleado)
            throws PersistenciaException {

        String sqlPedido = """
            INSERT INTO PEDIDO
            (fecha_creacion, estado, subtotal, descuento, total, id_empleado)
            VALUES (?, ?, ?, 0, ?, ?)
        """;

        String sqlExpress = """
            INSERT INTO EXPRESS
            (id_pedido, folio, pin_seguridad, fecha_listo)
            VALUES (?, ?, SHA2(?,256), NULL)
        """;

        String sqlDetalle = """
            INSERT INTO DETALLE_PEDIDO
            (cantidad, nota, precio_unitario, id_pedido, id_producto)
            VALUES (?, ?, ?, ?, ?)
        """;

        Connection conn = null;

        try {

            conn = conexionBD.crearConexion();
            conn.setAutoCommit(false);

            int idGenerado;

            try (PreparedStatement ps =
                    conn.prepareStatement(sqlPedido,
                            Statement.RETURN_GENERATED_KEYS)) {

                ps.setTimestamp(1,
                        Timestamp.valueOf(pedido.getFechaCreacion()));
                ps.setString(2, pedido.getEstado());
                ps.setDouble(3, pedido.getSubtotal());
                ps.setDouble(4, pedido.getTotal());
                ps.setInt(5, idEmpleado);

                ps.executeUpdate();

                ResultSet rs = ps.getGeneratedKeys();
                rs.next();
                idGenerado = rs.getInt(1);
            }

            try (PreparedStatement psExp =
                    conn.prepareStatement(sqlExpress)) {

                psExp.setInt(1, idGenerado);
                psExp.setInt(2, pedido.getFolio());
                psExp.setString(3, pedido.getPin());
                psExp.executeUpdate();
            }

            for (DetallePedidoDTO d : pedido.getDetalles()) {

                try (PreparedStatement psDet =
                        conn.prepareStatement(sqlDetalle)) {

                    psDet.setInt(1, d.getCantidad());
                    psDet.setString(2, d.getNota());
                    psDet.setDouble(3, d.getPrecioUnitario());
                    psDet.setInt(4, idGenerado);
                    psDet.setInt(5, d.getIdProducto());
                    psDet.executeUpdate();
                }
            }

            conn.commit();

        } catch (SQLException e) {

            if (conn != null) {
                try { conn.rollback(); } catch (SQLException ex) { }
            }

            throw new PersistenciaException(
                    "Error al insertar pedido express", e);

        } finally {
            if (conn != null) {
                try { conn.close(); } catch (SQLException ex) { }
            }
        }
    }

    @Override
    public void actualizarEstado(int idPedido,
                                 String estado)
            throws PersistenciaException {

        String sql =
                "UPDATE PEDIDO SET estado = ? WHERE id_pedido = ?";

        try (Connection conn = conexionBD.crearConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, estado);
            ps.setInt(2, idPedido);
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new PersistenciaException(
                    "Error al actualizar estado", e);
        }
    }

    @Override
    public void actualizarFechaListo(int idPedido,
                                     LocalDateTime fecha)
            throws PersistenciaException {

        String sql =
                "UPDATE EXPRESS SET fecha_listo = ? WHERE id_pedido = ?";

        try (Connection conn = conexionBD.crearConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setTimestamp(1, Timestamp.valueOf(fecha));
            ps.setInt(2, idPedido);
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new PersistenciaException(
                    "Error al actualizar fechaListo", e);
        }
    }

    @Override
    public PedidoExpressDTO buscarPorId(int idPedido)
            throws PersistenciaException {

        String sql = """
            SELECT p.id_pedido, p.subtotal, p.total,
                   p.estado, p.fecha_creacion,
                   e.folio, e.pin_seguridad,
                   e.fecha_listo
            FROM PEDIDO p
            JOIN EXPRESS e ON p.id_pedido = e.id_pedido
            WHERE p.id_pedido = ?
        """;

        try (Connection conn = conexionBD.crearConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idPedido);

            try (ResultSet rs = ps.executeQuery()) {

                if (!rs.next()) return null;

                PedidoExpressDTO dto =
                        new PedidoExpressDTO();

                dto.setIdPedido(rs.getInt("id_pedido"));
                dto.setFolio(rs.getInt("folio"));
                dto.setSubtotal(rs.getDouble("subtotal"));
                dto.setTotal(rs.getDouble("total"));
                dto.setEstado(rs.getString("estado"));
                dto.setFechaCreacion(
                        rs.getTimestamp("fecha_creacion")
                                .toLocalDateTime());
                dto.setPin(rs.getString("pin_seguridad"));

                Timestamp ts = rs.getTimestamp("fecha_listo");
                if (ts != null) {
                    dto.setFechaListo(ts.toLocalDateTime());
                }

                return dto;
            }

        } catch (SQLException e) {
            throw new PersistenciaException(
                    "Error al buscar pedido", e);
        }
    }

    @Override
    public boolean validarPin(int idPedido,
                              String pinIngresado)
            throws PersistenciaException {

        String sql = """
            SELECT 1
            FROM EXPRESS
            WHERE id_pedido = ?
            AND pin_seguridad = SHA2(?,256)
        """;

        try (Connection conn = conexionBD.crearConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idPedido);
            ps.setString(2, pinIngresado);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }

        } catch (SQLException e) {
            throw new PersistenciaException(
                    "Error al validar PIN", e);
        }
    }

    @Override
public List<PedidoExpressDTO> obtenerPendientes()
        throws PersistenciaException {

    String sql = """
        SELECT p.id_pedido,
               e.folio,
               p.subtotal,
               p.total,
               p.estado,
               p.fecha_creacion
        FROM PEDIDO p
        JOIN EXPRESS e ON p.id_pedido = e.id_pedido
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
            dto.setSubtotal(rs.getDouble("subtotal"));
            dto.setTotal(rs.getDouble("total"));
            dto.setEstado(rs.getString("estado"));
            dto.setFechaCreacion(
                    rs.getTimestamp("fecha_creacion")
                      .toLocalDateTime());

            lista.add(dto);
        }

        return lista;

    } catch (SQLException e) {
        throw new PersistenciaException(
                "Error al obtener pedidos pendientes", e);
    }
}
}