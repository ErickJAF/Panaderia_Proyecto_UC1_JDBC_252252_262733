/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package negocio.DTOs;

/**
 *
 * @author josed
 */




import java.util.List;

import java.time.LocalDate;

import java.time.Period;

public class ClienteDTO {

    private Integer idCliente;
    private String nombreCompleto;
    private String correo;
    private LocalDate fechaNacimiento;
    private String contrasena;

    private List<TelefonoDTO> telefonos;

    public ClienteDTO() {
    }

    public ClienteDTO(String nombreCompleto,
                      String correo,
                      LocalDate fechaNacimiento,
                      String contrasena,
                      List<TelefonoDTO> telefonos) {

        this.nombreCompleto = nombreCompleto;
        this.correo = correo;
        this.fechaNacimiento = fechaNacimiento;
        this.contrasena = contrasena;
        this.telefonos = telefonos;
    }

    public Integer getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(Integer idCliente) {
        this.idCliente = idCliente;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public List<TelefonoDTO> getTelefonos() {
        return telefonos;
    }

    public void setTelefonos(List<TelefonoDTO> telefonos) {
        this.telefonos = telefonos;
    }

    // Edad calculada automáticamente
    public int getEdad() {
        return Period.between(fechaNacimiento, LocalDate.now()).getYears();
    }
}