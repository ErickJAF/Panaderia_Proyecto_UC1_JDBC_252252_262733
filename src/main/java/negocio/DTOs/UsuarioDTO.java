/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package negocio.DTOs;

/**
 *
 * @author icoro
 */
public class UsuarioDTO {
      private int id;
    private String nombreUsuario;
    private String rol;

    public UsuarioDTO(int id, String nombreUsuario, String rol) {
        this.id = id;
        this.nombreUsuario = nombreUsuario;
        this.rol = rol;
    }

    public int getId() {
        return id;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public String getRol() {
        return rol;
    }
}
