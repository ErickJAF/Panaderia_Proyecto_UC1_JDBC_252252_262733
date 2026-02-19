/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package persistencia.DAOs;

import java.sql.*;
import java.util.*;
import persistencia.conexion.IConexionBD;
import persistencia.dominio.Producto;
import persistencia.excepciones.PersistenciaException;

/**
 *
 * @author icoro
 */
public class ProductoDAO implements IProductoDAO{
     private final IConexionBD conexion;

    public ProductoDAO(IConexionBD conexion) {
        this.conexion = conexion;
    }
    @Override
    public List<Producto> obtenerDisponibles() throws PersistenciaException {

        List<Producto> lista = new ArrayList<>();

        String sql = "SELECT * FROM PRODUCTO WHERE disponible = TRUE";

        try (Connection conn = conexion.crearConexion();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Producto p = new Producto();
                p.setIdProducto(rs.getInt("id_producto"));
                p.setNombre(rs.getString("nombre"));
                p.setPrecio(rs.getDouble("precio"));
                lista.add(p);
            }

        } catch (SQLException e) {
            throw new PersistenciaException("Error al obtener productos", e);
        }

        return lista;
    }
}
