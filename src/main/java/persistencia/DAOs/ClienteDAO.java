/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package persistencia.DAOs;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
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
    public void insertar(Cliente cliente)
            throws PersistenciaException {

        String sqlCliente = """
            INSERT INTO CLIENTE
            (id_cliente, nombre_completo, fecha_nacimiento, calle, colonia)
            VALUES (?, ?, ?, ?, ?)
        """;

        String sqlTelefono = """
            INSERT INTO TELEFONO_CLIENTE
            (id_cliente, numero)
            VALUES (?, ?)
        """;

        Connection con = null;

        try {

            con = conexion.crearConexion();
            con.setAutoCommit(false);

            try (PreparedStatement ps =
                    con.prepareStatement(sqlCliente)) {

                ps.setInt(1, cliente.getIdCliente());
                ps.setString(2, cliente.getNombreCompleto());
                ps.setDate(3,
                        java.sql.Date.valueOf(
                                cliente.getFechaNacimiento()));
                ps.setString(4, cliente.getCalle());
                ps.setString(5, cliente.getColonia());

                ps.executeUpdate();
            }

            if (cliente.getTelefonos() != null) {

                for (String telefono : cliente.getTelefonos()) {

                    try (PreparedStatement psTel =
                            con.prepareStatement(sqlTelefono)) {

                        psTel.setInt(1, cliente.getIdCliente());
                        psTel.setString(2, telefono);

                        psTel.executeUpdate();
                    }
                }
            }

            con.commit();

        } catch (SQLException e) {

            if (con != null) {
                try { con.rollback(); } catch (SQLException ex) { }
            }

            throw new PersistenciaException(
                    "Error al insertar cliente", e);

        } finally {

            if (con != null) {
                try { con.close(); } catch (SQLException ex) { }
            }
        }
    }

    @Override
    public void actualizar(Cliente cliente) throws PersistenciaException {

        String sqlCliente = """
            UPDATE CLIENTE SET
                nombre_completo = ?,
                fecha_nacimiento = ?,
                calle = ?,
                colonia = ?
            WHERE id_cliente = ?
        """;

        String sqlEliminarTelefonos = """
            DELETE FROM TELEFONO_CLIENTE
            WHERE id_cliente = ?
        """;

        String sqlInsertarTelefono = """
            INSERT INTO TELEFONO_CLIENTE
            (id_cliente, numero)
            VALUES (?, ?)
        """;

        Connection con = null;

        try {

            con = conexion.crearConexion();
            con.setAutoCommit(false);

            try (PreparedStatement ps =
                    con.prepareStatement(sqlCliente)) {

                ps.setString(1, cliente.getNombreCompleto());
                ps.setDate(2,
                        java.sql.Date.valueOf(
                                cliente.getFechaNacimiento()));
                ps.setString(3, cliente.getCalle());
                ps.setString(4, cliente.getColonia());
                ps.setInt(5, cliente.getIdCliente());

                ps.executeUpdate();
            }

            try (PreparedStatement psDel =
                    con.prepareStatement(sqlEliminarTelefonos)) {

                psDel.setInt(1, cliente.getIdCliente());
                psDel.executeUpdate();
            }

            if (cliente.getTelefonos() != null) {

                for (String telefono : cliente.getTelefonos()) {

                    try (PreparedStatement psTel =
                            con.prepareStatement(sqlInsertarTelefono)) {

                        psTel.setInt(1, cliente.getIdCliente());
                        psTel.setString(2, telefono);
                        psTel.executeUpdate();
                    }
                }
            }

            con.commit();

        } catch (SQLException e) {

            if (con != null) {
                try { con.rollback(); } catch (SQLException ex) { }
            }

            throw new PersistenciaException(
                    "Error al actualizar cliente", e);

        } finally {

            if (con != null) {
                try { con.close(); } catch (SQLException ex) { }
            }
        }
    }

    @Override
    public Cliente buscarPorId(int idCliente)
            throws PersistenciaException {

        String sqlCliente = """
            SELECT id_cliente,
                   nombre_completo,
                   fecha_nacimiento,
                   calle,
                   colonia
            FROM CLIENTE
            WHERE id_cliente = ?
        """;

        String sqlTelefonos = """
            SELECT numero
            FROM TELEFONO_CLIENTE
            WHERE id_cliente = ?
        """;

        try (Connection con = conexion.crearConexion();
             PreparedStatement ps =
                     con.prepareStatement(sqlCliente)) {

            ps.setInt(1, idCliente);

            try (ResultSet rs = ps.executeQuery()) {

                if (!rs.next()) {
                    return null;
                }

                Cliente cliente = new Cliente();

                cliente.setIdCliente(
                        rs.getInt("id_cliente"));
                cliente.setNombreCompleto(
                        rs.getString("nombre_completo"));
                cliente.setFechaNacimiento(
                        rs.getDate("fecha_nacimiento")
                          .toLocalDate());
                cliente.setCalle(
                        rs.getString("calle"));
                cliente.setColonia(
                        rs.getString("colonia"));

                List<String> telefonos =
                        new ArrayList<>();

                try (PreparedStatement psTel =
                        con.prepareStatement(sqlTelefonos)) {

                    psTel.setInt(1, idCliente);

                    try (ResultSet rsTel =
                            psTel.executeQuery()) {

                        while (rsTel.next()) {
                            telefonos.add(
                                    rsTel.getString("numero"));
                        }
                    }
                }

                cliente.setTelefonos(telefonos);

                return cliente;
            }

        } catch (SQLException e) {
            throw new PersistenciaException(
                    "Error al buscar cliente", e);
        }
    }
}
