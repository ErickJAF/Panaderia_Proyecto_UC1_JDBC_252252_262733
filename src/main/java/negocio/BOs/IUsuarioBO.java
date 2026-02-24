/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package negocio.BOs;

import negocio.DTOs.UsuarioDTO;
import negocio.excepciones.NegocioException;


/**
 *
 * @author icoro
 */
public interface IUsuarioBO {
       UsuarioDTO autenticar(String nombreUsuario, String contrasena)
            throws NegocioException;
}
