/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package persistencia.dominio;

import java.time.LocalDate;

/**
 *
 * @author ERICK
 */
public class Cliente extends Usuario {

    private int idCliente;
    private String nombreCompleto;
    private LocalDate fechaNacimiento;
    private String calle;
    private String colonia;
    private int numero;

    public Cliente() {
    }

    public Cliente(String nombreCompleto, LocalDate fechaNacimiento,
                   String calle, String colonia, int numero) {
        this.nombreCompleto = nombreCompleto;
        this.fechaNacimiento = fechaNacimiento;
        this.calle = calle;
        this.colonia = colonia;
        this.numero = numero;
    }

    public Cliente(int idCliente, String nombreCompleto,
                   LocalDate fechaNacimiento,
                   String calle, String colonia, int numero) {

        this.idCliente = idCliente;
        this.nombreCompleto = nombreCompleto;
        this.fechaNacimiento = fechaNacimiento;
        this.calle = calle;
        this.colonia = colonia;
        this.numero = numero;
    }

    public int getIdCliente() {
        return idCliente;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    public String getCalle() {
        return calle;
    }

    public String getColonia() {
        return colonia;
    }

    public int getNumero() {
        return numero;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public void setCalle(String calle) {
        this.calle = calle;
    }

    public void setColonia(String colonia) {
        this.colonia = colonia;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    @Override
    public String toString() {
        return "Cliente{" + "idCliente=" + idCliente + ", nombreCompleto=" + nombreCompleto + ", fechaNacimiento=" + fechaNacimiento + ", calle=" + calle + ", colonia=" + colonia + ", numero=" + numero + '}';
    }
}
