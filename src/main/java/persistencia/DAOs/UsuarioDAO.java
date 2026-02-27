/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package persistencia.DAOs;


import java.sql.*;
import persistencia.conexion.IConexionBD;
import persistencia.dominio.Usuario;
import persistencia.excepciones.PersistenciaException;

/**
 *
 * @author icoro
 */
public class UsuarioDAO implements IUsuarioDAO{
     private final IConexionBD conexionBD;

    public UsuarioDAO(IConexionBD conexionBD) {
        this.conexionBD = conexionBD;
    }

    @Override
public Usuario autenticar(String nombreUsuario, String contrasena)
        throws PersistenciaException {

    String sql = """
        SELECT id_usuario, nombreUsuario, rol
        FROM USUARIO
        WHERE nombreUsuario = ?
        AND contrasena = SHA2(?, 256)
        AND activo = TRUE
    """;

    try (Connection conexion = conexionBD.crearConexion();
         PreparedStatement ps = conexion.prepareStatement(sql)) {

        ps.setString(1, nombreUsuario);
        ps.setString(2, contrasena);

        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            return new Usuario(
                    rs.getInt("id_usuario"),
                    rs.getString("nombreUsuario"),
                    rs.getString("rol")
            );
        }

        return null;

    } catch (SQLException e) {
        throw new PersistenciaException("Error al autenticar usuario", e);
    }
}
    
    @Override
public int insertar(String nombreUsuario, String rol, String contrasena)
        throws PersistenciaException {

    String sql = """
        INSERT INTO USUARIO (nombreUsuario, rol, contrasena)
        VALUES (?, ?, SHA2(?,256))
    """;

    try (Connection conexion = conexionBD.crearConexion();
         PreparedStatement ps = conexion.prepareStatement(
                 sql, Statement.RETURN_GENERATED_KEYS)) {

        ps.setString(1, nombreUsuario);
        ps.setString(2, rol);
        ps.setString(3, contrasena);

        ps.executeUpdate();

        ResultSet rs = ps.getGeneratedKeys();
        if (rs.next()) {
            return rs.getInt(1);
        }

        throw new PersistenciaException("No se pudo obtener ID generado.");

    } catch (SQLException e) {
        throw new PersistenciaException("Error al insertar usuario", e);
    }
    
}
@Override
public void actualizarPerfil(int idUsuario,
                             String nombre,
                             String telefono,
                             String correo,
                             String password)
        throws PersistenciaException {

    String sql = """
        UPDATE USUARIO
        SET nombre_usuario = ?,
            telefono = ?,
            correo = ?,
            password = ?
        WHERE id_usuario = ?
    """;

    try (Connection conn = conexionBD.crearConexion();
         PreparedStatement ps = conn.prepareStatement(sql)) {

        ps.setString(1, nombre);
        ps.setString(2, telefono);
        ps.setString(3, correo);
        ps.setString(4, password);
        ps.setInt(5, idUsuario);

        ps.executeUpdate();

    } catch (SQLException e) {
        throw new PersistenciaException("Error al actualizar perfil", e);
    }
}

    /**
     *
     * @param idUsuario
     * @throws PersistenciaException
     */
    @Override
     public void desactivarUsuario(int idUsuario) throws PersistenciaException {

    String sql = """
        UPDATE USUARIO
        SET activo = FALSE
        WHERE id_usuario = ?
    """;

    try (Connection con = conexionBD.crearConexion();
         PreparedStatement ps = con.prepareStatement(sql)) {

        ps.setInt(1, idUsuario);
        ps.executeUpdate();

    } catch (SQLException e) {
        throw new PersistenciaException(
                "Error al desactivar usuario", e);
    }
}
}
