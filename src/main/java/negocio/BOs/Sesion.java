/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package negocio.BOs;

import negocio.DTOs.UsuarioDTO;

/**
 *
 * @author icoro
 */
public class Sesion {

    private static UsuarioDTO usuarioActual;

    public static void iniciarSesion(UsuarioDTO usuario) {
        usuarioActual = usuario;
    }

    public static UsuarioDTO getUsuarioActual() {
        return usuarioActual;
    }

    public static void cerrarSesion() {
        usuarioActual = null;
    }
}
