/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package persistencia.DAOs;

import negocio.DTOs.DetallePedidoDTO;
import java.sql.*;
import persistencia.conexion.IConexionBD;
import persistencia.excepciones.PersistenciaException;

/**
 *
 * @author icoro
 */
public class DetallePedidoDAO implements IDetallePedidoDAO{
      private final IConexionBD conexion;

    public DetallePedidoDAO(IConexionBD conexion) {
        this.conexion = conexion;
    }
    @Override
    public void insertar(int idPedido, DetallePedidoDTO d) throws PersistenciaException {

        String sql = """
            INSERT INTO DETALLE_PEDIDO
            (cantidad, nota, precio_unitario, id_pedido, id_producto)
            VALUES (?, ?, ?, ?, ?)
        """;
        
        try (Connection conn = conexion.crearConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, d.getCantidad());
            ps.setString(2, d.getNota());
            ps.setDouble(3, d.getPrecioUnitario());
            ps.setInt(4, idPedido);
            ps.setInt(5, d.getIdProducto());

            ps.executeUpdate();

        } catch (SQLException e) {
            throw new PersistenciaException("Error al insertar detalle", e);
        }
}
}
