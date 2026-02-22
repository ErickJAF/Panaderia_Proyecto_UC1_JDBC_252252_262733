/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package persistencia.DAOs;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import persistencia.conexion.IConexionBD;
import persistencia.dominio.Cliente;
import persistencia.excepciones.PersistenciaException;

/**
 *
 * @author ERICK
 */
public class ClienteDAO implements IClienteDAO {

    private final IConexionBD conexion;

    public ClienteDAO(IConexionBD conexion) {
        this.conexion = conexion;
    }

    @Override
    public void insertar(Cliente cliente) throws PersistenciaException {

        String sql = "INSERT INTO clientes "
                + "(nombre_completo, fecha_nacimiento, calle, colonia, numero) "
                + "VALUES (?, ?, ?, ?, ?)";

        try (Connection con = conexion.crearConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, cliente.getNombreCompleto());
            ps.setDate(2, java.sql.Date.valueOf(cliente.getFechaNacimiento()));
            ps.setString(3, cliente.getCalle());
            ps.setString(4, cliente.getColonia());
            ps.setInt(5, cliente.getNumero());

            ps.executeUpdate();

        } catch (SQLException e) {
            throw new PersistenciaException("Error al insertar cliente", e);
        }
    }

    @Override
    public void actualizar(Cliente cliente) throws PersistenciaException {

        String sql = "UPDATE clientes SET "
                + "nombre_completo = ?, "
                + "fecha_nacimiento = ?, "
                + "calle = ?, "
                + "colonia = ?, "
                + "numero = ? "
                + "WHERE id_cliente = ?";

        try (Connection con = conexion.crearConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, cliente.getNombreCompleto());
            ps.setDate(2, java.sql.Date.valueOf(cliente.getFechaNacimiento()));
            ps.setString(3, cliente.getCalle());
            ps.setString(4, cliente.getColonia());
            ps.setInt(5, cliente.getNumero());
            ps.setInt(6, cliente.getIdCliente());

            ps.executeUpdate();

        } catch (SQLException e) {
            throw new PersistenciaException("Error al actualizar cliente", e);
        }
    }

    @Override
    public Cliente buscarPorId(int idCliente) throws PersistenciaException {

        String sql = "SELECT * FROM clientes WHERE id_cliente = ?";

        try (Connection con = conexion.crearConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idCliente);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                Cliente cliente = new Cliente();

                cliente.setIdCliente(rs.getInt("id_cliente"));
                cliente.setNombreCompleto(rs.getString("nombre_completo"));
                cliente.setFechaNacimiento(
                        rs.getDate("fecha_nacimiento").toLocalDate()
                );
                cliente.setCalle(rs.getString("calle"));
                cliente.setColonia(rs.getString("colonia"));
                cliente.setNumero(rs.getInt("numero"));

                return cliente;
            }

            return null;

        } catch (SQLException e) {
            throw new PersistenciaException("Error al buscar cliente", e);
        }
    }
}
