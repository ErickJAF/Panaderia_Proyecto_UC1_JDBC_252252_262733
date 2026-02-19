/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package persistencia.dominio;

/**
 *
 * @author ERICK
 */
public class Usuario {
    private int idUsuario;
    private String nombreCompleto;
    private String rol; // Cliente o Empleado

    public Usuario(int idUsuario, String nombreCompleto, String rol) {
        this.idUsuario = idUsuario;
        this.nombreCompleto = nombreCompleto;
        this.rol = rol;
    }

    public Usuario() {
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public String getRol() {
        return rol;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    @Override
    public String toString() {
        return "Usuario{" + "idUsuario=" + idUsuario + ", nombreCompleto=" + nombreCompleto + ", rol=" + rol + '}';
    }
}
