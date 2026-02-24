/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package negocio.BOs;

import negocio.DTOs.UsuarioDTO;
import negocio.excepciones.NegocioException;
import persistencia.DAOs.IUsuarioDAO;
import persistencia.dominio.Usuario;
import persistencia.excepciones.PersistenciaException;

/**
 *
 * @author icoro
 */
public class UsuarioBO implements IUsuarioBO {

    private final IUsuarioDAO usuarioDAO;

    public UsuarioBO(IUsuarioDAO usuarioDAO) {
        this.usuarioDAO = usuarioDAO;
    }

    @Override
    public UsuarioDTO autenticar(String nombreUsuario, String contrasena)
            throws NegocioException {

        if (nombreUsuario == null || nombreUsuario.isBlank()
                || contrasena == null || contrasena.isBlank()) {
            throw new NegocioException("Debe ingresar usuario y contraseña");
        }

        try {
            Usuario usuario = usuarioDAO.autenticar(nombreUsuario, contrasena);

            if (usuario == null) {
                throw new NegocioException("Usuario o contraseña incorrectos");
            }

            return new UsuarioDTO(
                    usuario.getIdUsuario(),
                    usuario.getNombreCompleto(),
                    usuario.getRol()
            );

        } catch (PersistenciaException e) {
    e.printStackTrace();
    throw new NegocioException("Error al iniciar sesión: " + e.getMessage());
}
    }
}