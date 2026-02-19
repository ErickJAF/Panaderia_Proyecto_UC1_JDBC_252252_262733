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

/**
 *
 * @author ERICK
 */
public class PedidoProgramadoDAO implements IPedidoProgramadoDAO {
    private final IConexionBD conexionBD;
    private static final Logger LOG = Logger.getLogger(PedidoProgramadoDAO.class.getName());

    public PedidoProgramadoDAO(IConexionBD conexionBD) {
        this.conexionBD = conexionBD;
    }

    @Override
    public int obtenerUltimoNumeroPedido() throws PersistenciaException {

        String sql = """
                SELECT MAX(id_pedido) AS ultimo
                FROM PEDIDO
                """;

        try (Connection conn = conexionBD.crearConexion();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return rs.getInt("ultimo");
            }

            return 0;

        } catch (SQLException ex) {
            LOG.log(Level.SEVERE, "Error al obtener último número de pedido", ex);
            throw new PersistenciaException("Error al obtener el último número de pedido", ex);
        }
    }

    @Override
    public void insertar(PedidoProgramado pedido) throws PersistenciaException {

        String sqlPedido = """
            INSERT INTO PEDIDO
            (fecha_creacion, estado, subtotal, descuento, total, id_empleado)
            VALUES (?, ?, ?, ?, ?, ?)
            """;

        String sqlProgramado = """
            INSERT INTO PROGRAMADO
            (id_pedido, id_cliente, id_cupon)
            VALUES (?, ?, ?)
            """;

        try (Connection conn = conexionBD.crearConexion()) {

            PreparedStatement psPedido = conn.prepareStatement(sqlPedido, PreparedStatement.RETURN_GENERATED_KEYS);

            psPedido.setDate(1, java.sql.Date.valueOf(pedido.getFechaCreacion()));
            psPedido.setString(2, pedido.getEstado());
            psPedido.setDouble(3, pedido.getSubtotal());
            psPedido.setDouble(4, pedido.getDescuento());
            psPedido.setDouble(5, pedido.getTotal());
            psPedido.setInt(6, pedido.getIdEmpleado());

            psPedido.executeUpdate();

            ResultSet rs = psPedido.getGeneratedKeys();
            if (rs.next()) {
                pedido.setIdPedido(rs.getInt(1));
            }

            PreparedStatement psProg = conn.prepareStatement(sqlProgramado);

            psProg.setInt(1, pedido.getIdPedido());
            psProg.setInt(2, pedido.getIdCliente());

            if (pedido.getIdCupon() != null) {
                psProg.setInt(3, pedido.getIdCupon());
            } else {
                psProg.setNull(3, Types.INTEGER);
            }

            psProg.executeUpdate();

        } catch (SQLException ex) {
            throw new PersistenciaException("Error al insertar pedido programado", ex);
        }
    }

    @Override
    public List<PedidoProgramado> obtenerActivosPorCliente(int idCliente) throws PersistenciaException {

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
            LOG.log(Level.SEVERE, "Error al obtener pedidos activos", ex);
            throw new PersistenciaException("Error al obtener pedidos activos", ex);
        }
    }

    @Override
    public List<PedidoProgramado> obtenerPorTelefono(String telefono) throws PersistenciaException {

        String sql = """
                SELECT p.*, pr.id_cliente, pr.id_cupon
                FROM PEDIDO p
                JOIN PROGRAMADO pr ON p.id_pedido = pr.id_pedido
                JOIN TELEFONO t ON pr.id_cliente = t.id_cliente
                WHERE t.numero = ? AND p.estado = 'Pendiente'
                """;

        List<PedidoProgramado> lista = new ArrayList<>();

        try (Connection conn = conexionBD.crearConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, telefono);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(extraerPedido(rs));
                }
            }

            return lista;

        } catch (SQLException ex) {
            LOG.log(Level.SEVERE, "Error al obtener pedidos por teléfono", ex);
            throw new PersistenciaException("Error al obtener pedidos por teléfono", ex);
        }
    }

    @Override
    public void actualizarEstado(int idPedido, String estado) throws PersistenciaException {

        String sql = """
                UPDATE PEDIDO
                SET estado = ?
                WHERE id_pedido = ?
                """;

        try (Connection conn = conexionBD.crearConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, estado);
            ps.setInt(2, idPedido);

            if (ps.executeUpdate() == 0) {
                throw new PersistenciaException("No se encontró el pedido.");
            }

        } catch (SQLException ex) {
            LOG.log(Level.SEVERE, "Error al actualizar estado del pedido", ex);
            throw new PersistenciaException("Error al actualizar estado del pedido", ex);
        }
    }

    private PedidoProgramado extraerPedido(ResultSet rs) throws SQLException {

        PedidoProgramado p = new PedidoProgramado();

        p.setIdPedido(rs.getInt("id_pedido"));
        p.setIdCliente(rs.getInt("id_cliente"));
        p.setFechaCreacion(rs.getDate("fecha_creacion").toLocalDate());
        p.setTotal(rs.getDouble("total"));
        p.setEstado(rs.getString("estado"));

        int idCupon = rs.getInt("id_cupon");
        p.setIdCupon(rs.wasNull() ? null : idCupon);

        return p;
    }
}
