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
        
        String sql = "{CALL sp_crear_pedido_programado(?,?,?,?,?,?,?,?)}";
     
        try(Connection conn = conexionBD.crearConexion()){
            
            conn.setAutoCommit(false); //iniciar transaccion
            try(CallableStatement cs = conn.prepareCall(sql)){
            
             cs.setDate(1, java.sql.Date.valueOf(pedido.getFechaCreacion()));
            cs.setString(2, pedido.getEstado());
            cs.setDouble(3, pedido.getSubtotal());
            cs.setDouble(4, pedido.getDescuento());
            cs.setDouble(5, pedido.getTotal());
            cs.setInt(6, pedido.getIdEmpleado());
            cs.setInt(7, pedido.getIdCliente());
            
                if (pedido.getIdCupon() != null) {
                    cs.setInt(8, pedido.getIdCupon());
                }else{
                    cs.setNull(8, Types.INTEGER);
                  }
                cs.execute();
                conn.commit(); //confirmar transaccion
                
            }catch(SQLException e){
                conn.rollback();
                throw e;
            }
                
            }catch(SQLException ex){
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
