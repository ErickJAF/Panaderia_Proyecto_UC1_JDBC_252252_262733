/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package negocio.BOs;

import negocio.DTOs.UsuarioDTO;
import negocio.excepciones.NegocioException;

/**
 * Interfaz que define las operaciones de negocio relacionadas
 * con la gestión y autenticación de usuarios del sistema.
 * <p>
 * Establece los métodos necesarios para validar credenciales
 * de acceso y permitir la edición de información del perfil
 * de un usuario.
 * </p>
 * 
 * @author icoro
 */
public interface IUsuarioBO {

    /**
     * Autentica a un usuario mediante su nombre de usuario y contraseña.
     *
     * @param nombreUsuario Nombre de usuario ingresado
     * @param contrasena Contraseña ingresada
     * @return UsuarioDTO con la información del usuario autenticado
     * @throws NegocioException Si las credenciales son inválidas
     *                          o ocurre un error en el proceso de autenticación
     */
    UsuarioDTO autenticar(String nombreUsuario, String contrasena)
            throws NegocioException;

    /**
     * Permite editar la información del perfil de un usuario.
     *
     * @param idUsuario Identificador del usuario
     * @param nombre Nuevo nombre del usuario
     * @param telefono Nuevo número de teléfono
     * @param correo Nuevo correo electrónico
     * @param password Nueva contraseña
     * @throws NegocioException Si ocurre un error durante la actualización
     *                          de la información del usuario
     */
    void editarPerfil(int idUsuario,
                      String nombre,
                      String telefono,
                      String correo,
                      String password)
        throws NegocioException;
}