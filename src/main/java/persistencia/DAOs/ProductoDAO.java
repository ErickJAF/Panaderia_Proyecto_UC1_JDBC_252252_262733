/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package persistencia.DAOs;

import dto.ProductoDTO;
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
    public List<ProductoDTO> obtenerDisponibles() throws PersistenciaException {

      String sql = """
        SELECT id_producto, nombre, precio
        FROM PRODUCTO
        WHERE disponible = 1
    """;

    List<ProductoDTO> lista = new ArrayList<>();

    try(Connection conn = conexion.crearConexion();
        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery()){

        while(rs.next()){

            ProductoDTO p = new ProductoDTO(
                    rs.getInt("id_producto"),
                    rs.getString("nombre"),
                    rs.getDouble("precio")
            );

            lista.add(p);
        }

        return lista;

    }catch(SQLException ex){
        throw new PersistenciaException("Error al obtener productos", ex);
    }
}
}
