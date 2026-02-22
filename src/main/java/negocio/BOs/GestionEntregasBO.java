/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package negocio.BOs;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Logger;
import persistencia.DAOs.PedidoDAO;
import persistencia.conexion.IConexionBD;
import persistencia.excepciones.PersistenciaException;

/**
 *
 * @author ERICK
 */
public class GestionEntregasBO implements IGestionEntregasBO{
    
    private final IConexionBD conexionBD;
    private static final Logger LOG = Logger.getLogger(PedidoDAO.class.getName());

    public GestionEntregasBO(IConexionBD conexionBD) {
        this.conexionBD = conexionBD;
    }

    @Override
    public void cambiarEstadoPedido(int idPedido, String nuevoEstado)
            throws PersistenciaException {

        String sql = """
            UPDATE PEDIDO
            SET estado = ?
            WHERE id_pedido = ?
            """;

        try (Connection conn = conexionBD.crearConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, nuevoEstado);
            ps.setInt(2, idPedido);
            ps.executeUpdate();

        } catch (SQLException ex) {
            throw new PersistenciaException(
                    "Error al cambiar estado del pedido", ex);
        }
    }
    
}
