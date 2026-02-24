/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package persistencia.DAOs;

import persistencia.dominio.Usuario;
import persistencia.excepciones.PersistenciaException;

/**
 *
 * @author icoro
 */


public interface IUsuarioDAO {

    Usuario autenticar(String nombreUsuario, String contrasena)
            throws PersistenciaException;
    int insertar(String nombreUsuario, String rol, String contrasena)
        throws PersistenciaException;
    void actualizarPerfil(int idUsuario,
                      String nombre,
                      String telefono,
                      String correo,
                      String password)
        throws PersistenciaException;
}